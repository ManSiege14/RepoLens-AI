package com.repolens.ai.gemini;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "repolens.ai.gemini")
public record GeminiProperties(
        boolean enabled,
        String apiKey,
        String model,
        String baseUrl
) {
}