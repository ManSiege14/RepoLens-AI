package com.repolens.ai.summary;

import java.util.List;

public record AiRepositorySummary(
        String summary,
        List<String> strengths,
        List<String> concerns,
        List<String> recommendations
) {
}