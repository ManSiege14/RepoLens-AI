package com.repolens.analysis.health;

import com.repolens.analysis.snapshot.RepositorySnapshotAnalysis;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class DefaultRepositoryHealthAnalyzer
        implements RepositoryHealthAnalyzer {

    @Override
    public RepositoryHealthAnalysis analyze(
            RepositorySnapshotAnalysis analysis
    ) {

        int score = 0;

        List<HealthFinding> findings =
                new ArrayList<>();

        // Documentation — 20 points
        if (analysis.readmePresent()) {

            score += 20;

            findings.add(
                    new HealthFinding(
                            "Documentation",
                            20,
                            "Repository contains a README."
                    )
            );

        } else {

            findings.add(
                    new HealthFinding(
                            "Documentation",
                            0,
                            "Repository does not contain a README."
                    )
            );
        }

        // License — 15 points
        if (analysis.licensePresent()) {

            score += 15;

            findings.add(
                    new HealthFinding(
                            "License",
                            15,
                            "Repository contains a license."
                    )
            );

        } else {

            findings.add(
                    new HealthFinding(
                            "License",
                            0,
                            "Repository does not contain a recognised license file."
                    )
            );
        }

        // CI/CD — 15 points
        if (analysis.githubActionsPresent()) {

            score += 15;

            findings.add(
                    new HealthFinding(
                            "CI/CD",
                            15,
                            "GitHub Actions workflow detected."
                    )
            );

        } else {

            findings.add(
                    new HealthFinding(
                            "CI/CD",
                            0,
                            "No GitHub Actions workflow detected."
                    )
            );
        }

        // Docker — 10 points
        if (analysis.dockerPresent()) {

            score += 10;

            findings.add(
                    new HealthFinding(
                            "Infrastructure",
                            10,
                            "Docker configuration detected."
                    )
            );

        } else {

            findings.add(
                    new HealthFinding(
                            "Infrastructure",
                            0,
                            "No Docker configuration detected."
                    )
            );
        }

        // Build tool — 10 points
        if (!analysis.buildTools().isEmpty()) {

            score += 10;

            findings.add(
                    new HealthFinding(
                            "Build System",
                            10,
                            "A recognised build tool was detected."
                    )
            );

        } else {

            findings.add(
                    new HealthFinding(
                            "Build System",
                            0,
                            "No recognised build tool was detected."
                    )
            );
        }

        // Programming language — 10 points
        if (!analysis.languages().isEmpty()) {

            score += 10;

            findings.add(
                    new HealthFinding(
                            "Languages",
                            10,
                            "Programming languages were detected."
                    )
            );

        } else {

            findings.add(
                    new HealthFinding(
                            "Languages",
                            0,
                            "No recognised programming language was detected."
                    )
            );
        }

        // Architecture — 10 points
        if (analysis.architecture() != null
                && analysis.architecture()
                .primaryArchitecture()
                != com.repolens.analysis.architecture.ArchitectureType.UNKNOWN) {

            score += 10;

            findings.add(
                    new HealthFinding(
                            "Architecture",
                            10,
                            "A recognised architecture was detected."
                    )
            );

        } else {

            findings.add(
                    new HealthFinding(
                            "Architecture",
                            0,
                            "No recognised architecture was detected."
                    )
            );
        }

        // Repository structure — 10 points
        if (analysis.structure().sourceFiles() > 0) {

            score += 10;

            findings.add(
                    new HealthFinding(
                            "Structure",
                            10,
                            "Repository contains source files."
                    )
            );

        } else {

            findings.add(
                    new HealthFinding(
                            "Structure",
                            0,
                            "No recognised source files were detected."
                    )
            );
        }

        return new RepositoryHealthAnalysis(
                score,
                determineGrade(score),
                List.copyOf(findings)
        );
    }

    private HealthGrade determineGrade(int score) {

        if (score >= 85) {
            return HealthGrade.EXCELLENT;
        }

        if (score >= 70) {
            return HealthGrade.GOOD;
        }

        if (score >= 50) {
            return HealthGrade.FAIR;
        }

        return HealthGrade.POOR;
    }
}
