package com.padmavatimedicals.backend.controller;

import com.padmavatimedicals.backend.exception.BadRequestException;
import com.padmavatimedicals.backend.service.RequestStorageService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class PharmacyApiController {
    private final RequestStorageService requestStorageService;

    public PharmacyApiController(RequestStorageService requestStorageService) {
        this.requestStorageService = requestStorageService;
    }

    @GetMapping("/health")
    public Map<String, String> health() {
        return Map.of("status", "ok");
    }

    @PostMapping(value = "/inquiries", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Map<String, String>> createInquiry(
        @RequestParam String medicine,
        @RequestParam(required = false) String phone
    ) throws IOException {
        requireText(medicine, "Medicine name is required");

        String requestId = requestStorageService.saveInquiry(medicine, phone);
        return ResponseEntity.status(HttpStatus.CREATED).body(
            Map.of(
                "status", "saved",
                "message", "Inquiry saved successfully",
                "id", requestId
            )
        );
    }

    @PostMapping(value = "/orders", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Map<String, String>> createOrder(
        @RequestParam String name,
        @RequestParam String phone,
        @RequestParam String medicine,
        @RequestParam String address,
        @RequestParam(required = false) MultipartFile prescription
    ) throws IOException {
        requireText(name, "Name is required");
        requireText(phone, "Phone number is required");
        requireText(medicine, "Medicine name is required");
        requireText(address, "Address is required");

        String requestId = requestStorageService.saveOrder(name, phone, medicine, address, prescription);
        return ResponseEntity.status(HttpStatus.CREATED).body(
            Map.of(
                "status", "saved",
                "message", "Order saved successfully",
                "id", requestId
            )
        );
    }

    private void requireText(String value, String message) {
        if (!StringUtils.hasText(value)) {
            throw new BadRequestException(message);
        }
    }
}
