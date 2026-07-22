package com.repolens.analysis.detector;

import com.repolens.analysis.scanner.ScannedFile;
import com.repolens.analysis.scanner.ScannedRepository;
import org.springframework.stereotype.Component;

import java.util.EnumSet;
import java.util.Set;

@Component
public class DefaultInfrastructureDetector implements InfrastructureDetector {

    @Override
    public Set<Infrastructure> detect(ScannedRepository repository) {

        Set<Infrastructure> infrastructure = EnumSet.noneOf(Infrastructure.class);

        for (ScannedFile file : repository.files()) {

            String relativePathStr = file.relativePath().toString().replace('\\', '/');
            String fileName = file.relativePath().getFileName().toString();

            switch (fileName) {

                case "Dockerfile" ->
                        infrastructure.add(Infrastructure.DOCKER);

                case "docker-compose.yml",
                     "docker-compose.yaml" ->
                        infrastructure.add(Infrastructure.DOCKER_COMPOSE);

                case "Jenkinsfile" ->
                        infrastructure.add(Infrastructure.JENKINS);

                case ".gitlab-ci.yml" ->
                        infrastructure.add(Infrastructure.GITLAB_CI);

                default -> {
                    // Ignore unsupported file names
                }
            }

            if (relativePathStr.startsWith(".github/workflows/")
                    && (relativePathStr.endsWith(".yml")
                    || relativePathStr.endsWith(".yaml"))) {

                infrastructure.add(Infrastructure.GITHUB_ACTIONS);
            }
        }

        return infrastructure;
    }
}