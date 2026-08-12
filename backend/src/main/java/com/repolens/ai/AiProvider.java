package com.repolens.ai;

public interface AiProvider {

    AiProviderType type();

    AiProviderStatus status();

    boolean isAvailable();

    String generate(String prompt);
}