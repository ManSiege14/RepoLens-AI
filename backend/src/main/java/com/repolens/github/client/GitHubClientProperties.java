package com.repolens.github.client;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "github.api")
public record GitHubClientProperties(
        String baseUrl,
        String token
) {
}