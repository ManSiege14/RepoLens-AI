package com.repolens.analysis.detector;

import com.repolens.analysis.snapshot.RepositorySnapshot;
import org.springframework.stereotype.Component;

@Component
public class DefaultDockerDetector
        implements DockerDetector {

    @Override
    public Boolean detect(RepositorySnapshot snapshot) {

        return snapshot.files()
                .stream()
                .anyMatch(file ->
                        file.name().equalsIgnoreCase("Dockerfile")
                                || file.name().equalsIgnoreCase("docker-compose.yml")
                                || file.name().equalsIgnoreCase("docker-compose.yaml")
                );
    }
}