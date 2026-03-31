package backend.config;

import java.util.Arrays;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class BackendWebConfig implements WebMvcConfigurer {

	private final String[] allowedOrigins;
	private final String[] allowedMethods;
	private final String[] allowedHeaders;
	private final boolean allowCredentials;
	private final long maxAgeSeconds;

	public BackendWebConfig(
		@Value("${backend.cors.allowed-origins:http://localhost:5173,http://127.0.0.1:5173}") String allowedOrigins,
		@Value("${backend.cors.allowed-methods:GET,POST,PATCH,OPTIONS}") String allowedMethods,
		@Value("${backend.cors.allowed-headers:*}") String allowedHeaders,
		@Value("${backend.cors.allow-credentials:false}") boolean allowCredentials,
		@Value("${backend.cors.max-age-seconds:3600}") long maxAgeSeconds
	) {
		this.allowedOrigins = splitList(allowedOrigins);
		this.allowedMethods = splitList(allowedMethods);
		this.allowedHeaders = splitList(allowedHeaders);
		this.allowCredentials = allowCredentials;
		this.maxAgeSeconds = maxAgeSeconds;
	}

	@Override
	public void addCorsMappings(CorsRegistry registry) {
		registry.addMapping("/api/**")
			.allowedOrigins(allowedOrigins)
			.allowedMethods(allowedMethods)
			.allowedHeaders(allowedHeaders)
			.allowCredentials(allowCredentials)
			.maxAge(maxAgeSeconds);
	}

	private String[] splitList(String rawValue) {
		return Arrays.stream(rawValue.split(","))
			.map(String::trim)
			.filter(value -> !value.isBlank())
			.toArray(String[]::new);
	}
}
