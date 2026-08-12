package com.repolens.ai;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DefaultAiService implements AiService {

    private final List<AiProvider> providers;

    public DefaultAiService(List<AiProvider> providers) {
        this.providers = providers;
    }

    @Override
    public List<AiProviderType> getAvailableProviders() {

        return providers.stream()
                .filter(AiProvider::isAvailable)
                .map(AiProvider::type)
                .toList();
    }

    @Override
    public boolean isAiAvailable() {
        return providers.stream()
                .anyMatch(AiProvider::isAvailable);
    }

    @Override
    public AiProvider getProvider(AiProviderType type) {

        return providers.stream()
                .filter(provider -> provider.type() == type)
                .findFirst()
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "AI provider not found: " + type
                        )
                );
    }
}