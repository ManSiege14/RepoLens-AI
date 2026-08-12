package com.repolens.ai.web.dto;

import com.repolens.ai.AiProviderStatus;
import com.repolens.ai.AiProviderType;

import java.util.List;

public record AiStatusResponse(
        boolean aiAvailable,
        List<ProviderStatusResponse> providers
) {

    public record ProviderStatusResponse(
            AiProviderType type,
            AiProviderStatus status
    ) {
    }
}