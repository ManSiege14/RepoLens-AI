package com.repolens.analysis.health;

public record HealthFinding(
        String category,
        int points,
        String message
) {
}