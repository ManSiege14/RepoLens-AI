package com.repolens.ai.gemini;

import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.web.client.RestClient;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;

class DefaultGeminiClientTest {

    private MockWebServer mockWebServer;

    @AfterEach
    void tearDown() throws IOException {

        if (mockWebServer != null) {
            mockWebServer.shutdown();
        }
    }

    @Test
    void shouldGenerateContentFromGeminiResponse()
            throws IOException, InterruptedException {

        mockWebServer = new MockWebServer();

        mockWebServer.enqueue(
                new MockResponse()
                        .setResponseCode(200)
                        .addHeader(
                                "Content-Type",
                                "application/json"
                        )
                        .setBody("""
                                {
                                  "candidates": [
                                    {
                                      "content": {
                                        "parts": [
                                          {
                                            "text": "This is a test summary."
                                          }
                                        ]
                                      }
                                    }
                                  ]
                                }
                                """)
        );

        mockWebServer.start();

        GeminiProperties properties =
                new GeminiProperties(
                        true,
                        "test-api-key",
                        "gemini-2.5-flash",
                        mockWebServer.url("/").toString()
                );

        DefaultGeminiClient client =
                new DefaultGeminiClient(
                        RestClient.builder(),
                        properties
                );

        String result =
                client.generateContent(
                        "Summarize this repository."
                );

        assertEquals(
                "This is a test summary.",
                result
        );

        var request =
                mockWebServer.takeRequest();

        assertEquals(
                "POST",
                request.getMethod()
        );

        assertEquals(
                "/v1beta/models/gemini-2.5-flash:generateContent",
                request.getPath()
        );

        assertEquals(
                "test-api-key",
                request.getHeader("x-goog-api-key")
        );
    }
}