package com.repolens.ai.gemini;

import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import java.util.List;

@Component
public class DefaultGeminiClient implements GeminiClient {

    private final RestClient restClient;
    private final GeminiProperties properties;

    public DefaultGeminiClient(
            RestClient.Builder restClientBuilder,
            GeminiProperties properties
    ) {
        this.restClient = restClientBuilder
                .baseUrl(properties.baseUrl())
                .build();

        this.properties = properties;
    }

    @Override
    public String generateContent(String prompt) {

        GeminiRequest request =
                new GeminiRequest(
                        List.of(
                                new GeminiContent(
                                        "user",
                                        List.of(
                                                new GeminiPart(prompt)
                                        )
                                )
                        )
                );

        GeminiResponse response =
                restClient.post()
                        .uri(
                                "/v1beta/models/{model}:generateContent",
                                properties.model()
                        )
                        .header(
                                "x-goog-api-key",
                                properties.apiKey()
                        )
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(request)
                        .retrieve()
                        .body(GeminiResponse.class);

        if (response == null
                || response.candidates() == null
                || response.candidates().isEmpty()
                || response.candidates().getFirst().content() == null
                || response.candidates().getFirst().content().parts() == null
                || response.candidates().getFirst().content().parts().isEmpty()) {

            throw new IllegalStateException(
                    "Gemini returned an empty response"
            );
        }

        return response.candidates()
                .getFirst()
                .content()
                .parts()
                .getFirst()
                .text();
    }

    private record GeminiRequest(
            List<GeminiContent> contents
    ) {
    }

    private record GeminiContent(
            String role,
            List<GeminiPart> parts
    ) {
    }

    private record GeminiPart(
            String text
    ) {
    }

    private record GeminiResponse(
            List<GeminiCandidate> candidates
    ) {
    }

    private record GeminiCandidate(
            GeminiResponseContent content
    ) {
    }

    private record GeminiResponseContent(
            List<GeminiPart> parts
    ) {
    }
}