package com.oshibi.oshibi.controller;

import com.oshibi.oshibi.client.AnthropicVisionClient;
import com.oshibi.oshibi.dto.live.LiveInfoExtractDto;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.nio.charset.StandardCharsets;
import java.util.Set;

@RestController
@RequiredArgsConstructor
public class LiveApiController {

    private static final long MAX_FILE_SIZE = 5 * 1024 * 1024;
    private static final Set<String> ALLOWED_CONTENT_TYPES = Set.of(
            "image/jpeg", "image/png", "image/webp", "image/gif"
    );

    private final AnthropicVisionClient anthropicVisionClient;

    @PostMapping("/api/lives/extract-from-image")
    public ResponseEntity<LiveInfoExtractDto> extractFromImage(@RequestParam("image") MultipartFile image) {
        if (image.getSize() > MAX_FILE_SIZE) {
            return ResponseEntity.badRequest().build();
        }

        String contentType = image.getContentType();
        if (contentType == null || !ALLOWED_CONTENT_TYPES.contains(contentType)) {
            return ResponseEntity.badRequest().build();
        }

        try {
            ClassPathResource resource = new ClassPathResource("prompts/extract-live-info.txt");
            String prompt = new String(resource.getInputStream().readAllBytes(), StandardCharsets.UTF_8);

            LiveInfoExtractDto dto = anthropicVisionClient.extractLiveInfo(image.getBytes(), contentType, prompt);
            return ResponseEntity.ok(dto);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().build();
        }
    }
}
