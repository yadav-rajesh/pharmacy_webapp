package backend.medicinerequest;

import java.io.IOException;
import java.time.Instant;
import java.util.concurrent.ConcurrentHashMap;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.servlet.HandlerExceptionResolver;

@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
public class MedicineRequestRateLimitFilter extends OncePerRequestFilter {

	private final ConcurrentHashMap<String, RequestWindow> requestWindows = new ConcurrentHashMap<>();
	private final boolean enabled;
	private final int maxRequests;
	private final int windowSeconds;
	private final HandlerExceptionResolver handlerExceptionResolver;

	public MedicineRequestRateLimitFilter(
		@Value("${medicine-requests.rate-limit.enabled:true}") boolean enabled,
		@Value("${medicine-requests.rate-limit.max-requests:10}") int maxRequests,
		@Value("${medicine-requests.rate-limit.window-seconds:600}") int windowSeconds,
		@Qualifier("handlerExceptionResolver") HandlerExceptionResolver handlerExceptionResolver
	) {
		this.enabled = enabled;
		this.maxRequests = maxRequests;
		this.windowSeconds = windowSeconds;
		this.handlerExceptionResolver = handlerExceptionResolver;
	}

	@Override
	protected boolean shouldNotFilter(HttpServletRequest request) {
		return !enabled
			|| !"POST".equalsIgnoreCase(request.getMethod())
			|| !"/api/medicine-requests".equals(request.getRequestURI());
	}

	@Override
	protected void doFilterInternal(
		HttpServletRequest request,
		HttpServletResponse response,
		FilterChain filterChain
	) throws ServletException, IOException {
		var now = Instant.now().toEpochMilli();
		var windowMillis = windowSeconds * 1000L;
		cleanupExpiredWindows(now, windowMillis);

		var clientKey = resolveClientKey(request);
		var updatedWindow = requestWindows.compute(clientKey, (key, currentWindow) -> {
			if (currentWindow == null || (now - currentWindow.windowStartedAt()) >= windowMillis) {
				return new RequestWindow(now, 1);
			}

			return new RequestWindow(currentWindow.windowStartedAt(), currentWindow.requestCount() + 1);
		});

		if (updatedWindow != null && updatedWindow.requestCount() > maxRequests) {
			handlerExceptionResolver.resolveException(
				request,
				response,
				null,
				new RateLimitExceededException(
					"Too many medicine requests from this address. Please try again shortly."
				)
			);
			return;
		}

		filterChain.doFilter(request, response);
	}

	private void cleanupExpiredWindows(long now, long windowMillis) {
		requestWindows.entrySet().removeIf(
			entry -> (now - entry.getValue().windowStartedAt()) >= windowMillis
		);
	}

	private String resolveClientKey(HttpServletRequest request) {
		var forwardedFor = request.getHeader("X-Forwarded-For");

		if (forwardedFor != null && !forwardedFor.isBlank()) {
			var firstHop = forwardedFor.split(",")[0].trim();

			if (!firstHop.isBlank()) {
				return firstHop;
			}
		}

		return request.getRemoteAddr();
	}

	private record RequestWindow(
		long windowStartedAt,
		int requestCount
	) {
	}
}
