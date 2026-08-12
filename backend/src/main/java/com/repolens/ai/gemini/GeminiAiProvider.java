package com.repolens.ai.gemini;

import com.repolens.ai.AiProvider;
import com.repolens.ai.AiProviderStatus;
import com.repolens.ai.AiProviderType;
import org.springframework.stereotype.Component;

@Component
public class GeminiAiProvider implements AiProvider {

    private final GeminiProperties properties;
    private final GeminiClient client;

    public GeminiAiProvider(
            GeminiProperties properties,
            GeminiClient client
    ) {
        this.properties = properties;
        this.client = client;
    }

    @Override
    public AiProviderType type() {
        return AiProviderType.GEMINI;
    }

    @Override
    public AiProviderStatus status() {

        if (!properties.enabled()) {
            return AiProviderStatus.DISABLED;
        }

        if (properties.apiKey() == null
                || properties.apiKey().isBlank()) {
            return AiProviderStatus.UNAVAILABLE;
        }

        return AiProviderStatus.AVAILABLE;
    }

    @Override
    public boolean isAvailable() {
        return status() == AiProviderStatus.AVAILABLE;
    }

    @Override
    public String generate(String prompt) {

        if (!isAvailable()) {
            throw new IllegalStateException(
                    "Gemini provider is not available"
            );
        }

        return client.generateContent(prompt);
    }
}