package com.repolens.analysis.detector;

import com.repolens.analysis.snapshot.RepositorySnapshot;
import org.springframework.stereotype.Component;

import java.util.EnumSet;
import java.util.Set;

@Component
public class SnapshotBuildToolDetector
        implements RepositoryDetector<Set<BuildTool>> {

    @Override
    public Set<BuildTool> detect(RepositorySnapshot snapshot) {

        Set<BuildTool> detected =
                EnumSet.noneOf(BuildTool.class);

        snapshot.files().forEach(file -> {

            switch (file.name()) {

                case "pom.xml" ->
                        detected.add(BuildTool.MAVEN);

                case "build.gradle", "build.gradle.kts" ->
                        detected.add(BuildTool.GRADLE);

                case "package.json" ->
                        detected.add(BuildTool.NPM);

                case "yarn.lock" ->
                        detected.add(BuildTool.YARN);

                case "pnpm-lock.yaml" ->
                        detected.add(BuildTool.PNPM);

                case "requirements.txt" ->
                        detected.add(BuildTool.PIP);

                case "pyproject.toml" ->
                        detected.add(BuildTool.POETRY);

                case "Cargo.toml" ->
                        detected.add(BuildTool.CARGO);

                case "go.mod" ->
                        detected.add(BuildTool.GO_MODULES);

                default -> {
                    // Not a recognised build tool.
                }
            }
        });

        return detected;
    }
}