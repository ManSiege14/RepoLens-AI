package com.repolens.ai;

import java.util.List;

public interface AiService {

    List<AiProviderType> getAvailableProviders();

    boolean isAiAvailable();

    AiProvider getProvider(AiProviderType type);
}