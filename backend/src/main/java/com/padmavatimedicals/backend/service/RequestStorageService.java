package com.padmavatimedicals.backend.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.padmavatimedicals.backend.exception.BadRequestException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.UUID;

@Service
public class RequestStorageService {
    private static final ZoneId BUSINESS_ZONE = ZoneId.of("Asia/Kolkata");
    private static final long MAX_UPLOAD_BYTES = 8L * 1024L * 1024L;

    private final ObjectMapper objectMapper;
    private final Path inquiriesDir;
    private final Path ordersDir;
    private final Path uploadsDir;

    public RequestStorageService(
        ObjectMapper objectMapper,
        @Value("${app.storage-root:../backend-data}") String storageRoot
    ) throws IOException {
        this.objectMapper = objectMapper;

        Path root = Path.of(storageRoot).toAbsolutePath().normalize();
        this.inquiriesDir = root.resolve("inquiries");
        this.ordersDir = root.resolve("orders");
        this.uploadsDir = root.resolve("uploads");

        Files.createDirectories(this.inquiriesDir);
        Files.createDirectories(this.ordersDir);
        Files.createDirectories(this.uploadsDir);
    }

    public String saveInquiry(String medicine, String phone) throws IOException {
        String requestId = requestId("INQ");

        Map<String, Object> payload = new LinkedHashMap<>();
        payload.put("id", requestId);
        payload.put("createdAt", timestamp());
        payload.put("medicine", medicine.trim());
        payload.put("phone", phone == null ? "" : phone.trim());

        writeJson(inquiriesDir.resolve(requestId + ".json"), payload);
        return requestId;
    }

    public String saveOrder(
        String name,
        String phone,
        String medicine,
        String address,
        MultipartFile prescription
    ) throws IOException {
        String requestId = requestId("ORD");

        Map<String, Object> payload = new LinkedHashMap<>();
        payload.put("id", requestId);
        payload.put("createdAt", timestamp());
        payload.put("name", name.trim());
        payload.put("phone", phone.trim());
        payload.put("medicine", medicine.trim());
        payload.put("address", address.trim());

        if (prescription != null && !prescription.isEmpty()) {
            if (prescription.getSize() > MAX_UPLOAD_BYTES) {
                throw new BadRequestException("Prescription upload is too large");
            }

            String storedFileName = requestId + "-" + sanitizeFileName(prescription.getOriginalFilename());
            Path target = uploadsDir.resolve(storedFileName).normalize();
            if (!target.startsWith(uploadsDir)) {
                throw new BadRequestException("Invalid prescription file name");
            }

            try (var inputStream = prescription.getInputStream()) {
                Files.copy(inputStream, target, StandardCopyOption.REPLACE_EXISTING);
            }

            payload.put("prescriptionOriginalName", prescription.getOriginalFilename());
            payload.put("prescriptionContentType", prescription.getContentType());
            payload.put("prescriptionStoredAt", target.toString());
        } else {
            payload.put("prescriptionOriginalName", "");
            payload.put("prescriptionContentType", "");
            payload.put("prescriptionStoredAt", "");
        }

        writeJson(ordersDir.resolve(requestId + ".json"), payload);
        return requestId;
    }

    private void writeJson(Path path, Map<String, Object> payload) throws IOException {
        objectMapper.writerWithDefaultPrettyPrinter().writeValue(path.toFile(), payload);
    }

    private String requestId(String prefix) {
        String timestamp = ZonedDateTime.now(BUSINESS_ZONE).format(DateTimeFormatter.ofPattern("yyyyMMdd-HHmmss"));
        String suffix = UUID.randomUUID().toString().substring(0, 8);
        return prefix + "-" + timestamp + "-" + suffix;
    }

    private String timestamp() {
        return ZonedDateTime.now(BUSINESS_ZONE).format(DateTimeFormatter.ISO_OFFSET_DATE_TIME);
    }

    private String sanitizeFileName(String value) {
        if (value == null || value.isBlank()) {
            return "prescription.bin";
        }

        return value
            .replaceAll("[^a-zA-Z0-9._-]", "_")
            .replaceAll("_+", "_");
    }
}

