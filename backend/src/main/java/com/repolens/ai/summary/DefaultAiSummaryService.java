package com.repolens.ai.summary;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.repolens.ai.AiProvider;
import com.repolens.ai.AiProviderType;
import com.repolens.ai.AiService;
import com.repolens.analysis.snapshot.RepositorySnapshotAnalysis;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DefaultAiSummaryService
        implements AiSummaryService {

    private final AiService aiService;
    private final List<AiProvider> providers;
    private final ObjectMapper objectMapper;

    public DefaultAiSummaryService(
            AiService aiService,
            List<AiProvider> providers,
            ObjectMapper objectMapper
    ) {
        this.aiService = aiService;
        this.providers = providers;
        this.objectMapper = objectMapper;
    }

    @Override
    public AiRepositorySummary generateSummary(
            RepositorySnapshotAnalysis analysis
    ) {
        return generateSummary(
                analysis,
                null
        );
    }

    public AiRepositorySummary generateSummary(
            RepositorySnapshotAnalysis analysis,
            AiProviderType providerType
    ) {

        if (!aiService.isAiAvailable()) {
            throw new IllegalStateException(
                    "AI is currently unavailable"
            );
        }

        AiProvider provider = resolveProvider(
                providerType
        );

        String prompt = buildPrompt(analysis);

        String response =
                provider.generate(prompt);

        try {
            return objectMapper.readValue(
                    response,
                    AiRepositorySummary.class
            );
        } catch (Exception exception) {
            throw new IllegalStateException(
                    "AI provider returned an invalid summary",
                    exception
            );
        }
    }

    private AiProvider resolveProvider(
            AiProviderType providerType
    ) {

        // null means AUTO mode
        if (providerType == null) {

            return providers.stream()
                    .filter(AiProvider::isAvailable)
                    .findFirst()
                    .orElseThrow(() ->
                            new IllegalStateException(
                                    "No AI provider is available"
                            )
                    );
        }

        AiProvider provider =
                aiService.getProvider(providerType);

        if (!provider.isAvailable()) {
            throw new IllegalStateException(
                    "AI provider is unavailable: "
                            + providerType
            );
        }

        return provider;
    }

    private String buildPrompt(
            RepositorySnapshotAnalysis analysis
    ) {

        return """
                You are RepoLens, a software repository analysis assistant.

                Analyze ONLY the repository facts provided below.

                Do not invent technologies, files, architecture,
                vulnerabilities, or implementation details.

                Return ONLY valid JSON matching this structure:

                {
                  "summary": "short repository summary",
                  "strengths": ["strength 1", "strength 2"],
                  "concerns": ["concern 1", "concern 2"],
                  "recommendations": ["recommendation 1", "recommendation 2"]
                }

                Repository facts:

                README present: %s
                Docker present: %s
                GitHub Actions present: %s
                License present: %s
                Build tools: %s
                Languages: %s
                Architecture: %s
                Health score: %s
                Health grade: %s
                Health findings: %s
                """.formatted(
                analysis.readmePresent(),
                analysis.dockerPresent(),
                analysis.githubActionsPresent(),
                analysis.licensePresent(),
                analysis.buildTools(),
                analysis.languages(),
                analysis.architecture().primaryArchitecture(),
                analysis.health().score(),
                analysis.health().grade(),
                analysis.health().findings()
        );
    }
}