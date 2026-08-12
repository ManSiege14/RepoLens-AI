package com.repolens.ai.web;

import com.repolens.ai.AiProvider;
import com.repolens.ai.AiProviderStatus;
import com.repolens.ai.AiProviderType;
import com.repolens.ai.AiService;
import com.repolens.ai.web.dto.AiStatusResponse;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class AiControllerTest {

    @Test
    void shouldReturnAiProviderStatus() {

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
                return "test";
            }
        };

        AiProvider openAi = new AiProvider() {

            @Override
            public AiProviderType type() {
                return AiProviderType.OPENAI;
            }

            @Override
            public AiProviderStatus status() {
                return AiProviderStatus.DISABLED;
            }

            @Override
            public boolean isAvailable() {
                return false;
            }

            @Override
            public String generate(String prompt) {
                return "test";
            }
        };

        AiService aiService = new AiService() {

            @Override
            public List<AiProviderType> getAvailableProviders() {
                return List.of(AiProviderType.GEMINI);
            }

            @Override
            public boolean isAiAvailable() {
                return true;
            }

            @Override
            public AiProvider getProvider(
                    AiProviderType type
            ) {
                return gemini;
            }
        };

        AiController controller =
                new AiController(
                        aiService,
                        List.of(gemini, openAi)
                );

        AiStatusResponse response =
                controller.getStatus();

        assertTrue(response.aiAvailable());

        assertEquals(
                2,
                response.providers().size()
        );

        assertEquals(
                AiProviderType.GEMINI,
                response.providers().get(0).type()
        );

        assertEquals(
                AiProviderStatus.AVAILABLE,
                response.providers().get(0).status()
        );

        assertEquals(
                AiProviderType.OPENAI,
                response.providers().get(1).type()
        );

        assertEquals(
                AiProviderStatus.DISABLED,
                response.providers().get(1).status()
        );
    }
}