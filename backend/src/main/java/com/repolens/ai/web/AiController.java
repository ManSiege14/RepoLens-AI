package com.repolens.ai.web;

import com.repolens.ai.AiProvider;
import com.repolens.ai.AiService;
import com.repolens.ai.web.dto.AiStatusResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/ai")
public class AiController {

    private final AiService aiService;
    private final List<AiProvider> providers;

    public AiController(
            AiService aiService,
            List<AiProvider> providers
    ) {
        this.aiService = aiService;
        this.providers = providers;
    }

    @GetMapping("/status")
    public AiStatusResponse getStatus() {

        List<AiStatusResponse.ProviderStatusResponse> providerStatuses =
                providers.stream()
                        .map(provider ->
                                new AiStatusResponse.ProviderStatusResponse(
                                        provider.type(),
                                        provider.status()
                                )
                        )
                        .toList();

        return new AiStatusResponse(
                aiService.isAiAvailable(),
                providerStatuses
        );
    }
}