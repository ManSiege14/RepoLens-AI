package com.repolens.ai.gemini;

import com.repolens.ai.AiProviderStatus;
import com.repolens.ai.AiProviderType;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class GeminiAiProviderTest {

    @Test
    void shouldBeDisabledWhenGeminiIsDisabled() {

        GeminiProperties properties =
                new GeminiProperties(
                        false,
                        "test-key",
                        "gemini-2.5-flash",
                        "https://generativelanguage.googleapis.com"
                );

        GeminiClient client =
                prompt -> "unused";

        GeminiAiProvider provider =
                new GeminiAiProvider(
                        properties,
                        client
                );

        assertEquals(
                AiProviderType.GEMINI,
                provider.type()
        );

        assertEquals(
                AiProviderStatus.DISABLED,
                provider.status()
        );

        assertFalse(provider.isAvailable());
    }

    @Test
    void shouldBeUnavailableWhenApiKeyIsMissing() {

        GeminiProperties properties =
                new GeminiProperties(
                        true,
                        "",
                        "gemini-2.5-flash",
                        "https://generativelanguage.googleapis.com"
                );

        GeminiClient client =
                prompt -> "unused";

        GeminiAiProvider provider =
                new GeminiAiProvider(
                        properties,
                        client
                );

        assertEquals(
                AiProviderStatus.UNAVAILABLE,
                provider.status()
        );

        assertFalse(provider.isAvailable());
    }

    @Test
    void shouldBeAvailableWhenEnabledAndApiKeyExists() {

        GeminiProperties properties =
                new GeminiProperties(
                        true,
                        "test-key",
                        "gemini-2.5-flash",
                        "https://generativelanguage.googleapis.com"
                );

        GeminiClient client =
                prompt -> "test response";

        GeminiAiProvider provider =
                new GeminiAiProvider(
                        properties,
                        client
                );

        assertEquals(
                AiProviderStatus.AVAILABLE,
                provider.status()
        );

        assertTrue(provider.isAvailable());
    }

    @Test
    void shouldGenerateUsingGeminiClient() {

        GeminiProperties properties =
                new GeminiProperties(
                        true,
                        "test-key",
                        "gemini-2.5-flash",
                        "https://generativelanguage.googleapis.com"
                );

        GeminiClient client =
                prompt -> "Generated summary";

        GeminiAiProvider provider =
                new GeminiAiProvider(
                        properties,
                        client
                );

        String result =
                provider.generate(
                        "Summarize this repository."
                );

        assertEquals(
                "Generated summary",
                result
        );
    }
}