package com.oshibi.oshibi.client;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.oshibi.oshibi.dto.live.LiveInfoExtractDto;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Base64;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Component
public class AnthropicVisionClient {

    private static final String API_URL = "https://api.anthropic.com/v1/messages";
    private static final String MODEL = "claude-sonnet-4-6";
    private static final int MAX_TOKENS = 1024;
    private static final String ANTHROPIC_VERSION = "2023-06-01";

    @Value("${anthropic.api-key}")
    private String apiKey;

    private final HttpClient httpClient = HttpClient.newHttpClient();
    private final ObjectMapper objectMapper = new ObjectMapper();

    public LiveInfoExtractDto extractLiveInfo(byte[] imageBytes, String mediaType, String prompt)
            throws IOException, InterruptedException {

        String today = java.time.LocalDate.now().toString();
        prompt = prompt.replace("{{TODAY}}", today);

        String base64Image = Base64.getEncoder().encodeToString(imageBytes);

        Map<String, Object> imageSource = new LinkedHashMap<>();
        imageSource.put("type", "base64");
        imageSource.put("media_type", mediaType);
        imageSource.put("data", base64Image);

        Map<String, Object> imageContent = new LinkedHashMap<>();
        imageContent.put("type", "image");
        imageContent.put("source", imageSource);

        Map<String, Object> textContent = new LinkedHashMap<>();
        textContent.put("type", "text");
        textContent.put("text", prompt);

        Map<String, Object> message = new LinkedHashMap<>();
        message.put("role", "user");
        message.put("content", List.of(imageContent, textContent));

        Map<String, Object> requestBody = new LinkedHashMap<>();
        requestBody.put("model", MODEL);
        requestBody.put("max_tokens", MAX_TOKENS);
        requestBody.put("messages", List.of(message));

        String requestJson = objectMapper.writeValueAsString(requestBody);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(API_URL))
                .header("Content-Type", "application/json")
                .header("x-api-key", apiKey)
                .header("anthropic-version", ANTHROPIC_VERSION)
                .POST(HttpRequest.BodyPublishers.ofString(requestJson))
                .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() != 200) {
            throw new RuntimeException("Anthropic API request failed: status=" + response.statusCode()
                    + ", body=" + response.body());
        }

        JsonNode responseJson = objectMapper.readTree(response.body());
        String text = responseJson.path("content").get(0).path("text").asText();

        text = text.trim();
        if (text.startsWith("```")) {
            text = text.replaceAll("^```[a-zA-Z]*\\n?", "").replaceAll("```$", "").trim();
        }

        return objectMapper.readValue(text, LiveInfoExtractDto.class);
    }
}
