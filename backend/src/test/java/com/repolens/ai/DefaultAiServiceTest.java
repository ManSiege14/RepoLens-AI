package com.repolens.ai;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class DefaultAiServiceTest {

    @Test
    void shouldReportAvailableProviders() {

        AiProvider gemini = new AiProvider() {

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
                return "Gemini response";
            }
        };

        AiProvider openAi = new AiProvider() {

            @Override
            public AiProviderType type() {
                return AiProviderType.OPENAI;
            }

            @Override
            public AiProviderStatus status() {
                return AiProviderStatus.UNAVAILABLE;
            }

            @Override
            public boolean isAvailable() {
                return false;
            }

            @Override
            public String generate(String prompt) {
                return "unused";
            }
        };

        AiService service =
                new DefaultAiService(
                        List.of(gemini, openAi)
                );

        assertEquals(
                List.of(AiProviderType.GEMINI),
                service.getAvailableProviders()
        );

        assertTrue(service.isAiAvailable());
    }

    @Test
    void shouldReportAiUnavailableWhenNoProviderIsAvailable() {

        AiProvider unavailableProvider = new AiProvider() {

            @Override
            public AiProviderType type() {
                return AiProviderType.GEMINI;
            }

            @Override
            public AiProviderStatus status() {
                return AiProviderStatus.UNAVAILABLE;
            }

            @Override
            public boolean isAvailable() {
                return false;
            }

            @Override
            public String generate(String prompt) {
                return "unused";
            }
        };

        AiService service =
                new DefaultAiService(
                        List.of(unavailableProvider)
                );

        assertTrue(
                service.getAvailableProviders().isEmpty()
        );

        assertFalse(service.isAiAvailable());
    }
}