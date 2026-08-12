package com.repolens.ai;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class AiProviderTest {

    @Test
    void shouldExposeProviderTypeAndStatus() {

        AiProvider provider = new AiProvider() {

            @Override
            public AiProviderType type() {
                return AiProviderType.GEMINI;
            }

            @Override
            public AiProviderStatus status() {
                return AiProviderStatus.AVAILABLE;
            }

            @Override
            public boolean isAvailable() {
                return true;
            }

            @Override
            public String generate(String prompt) {
                return "test response";
            }
        };

        assertEquals(
                AiProviderType.GEMINI,
                provider.type()
        );

        assertEquals(
                AiProviderStatus.AVAILABLE,
                provider.status()
        );

        assertTrue(provider.isAvailable());

        assertEquals(
                "test response",
                provider.generate("test prompt")
        );
    }
}