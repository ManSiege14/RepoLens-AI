package com.repolens.analysis.detector;

import com.repolens.analysis.scanner.ScannedFile;
import com.repolens.analysis.scanner.ScannedRepository;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.file.Files;
import java.util.EnumSet;
import java.util.Locale;
import java.util.Set;

@Component
public class DefaultFrameworkDetector implements FrameworkDetector {

    @Override
    public Set<Framework> detect(ScannedRepository repository) {

        Set<Framework> frameworks = EnumSet.noneOf(Framework.class);

        for (ScannedFile file : repository.files()) {

            String fileName = file.relativePath()
                    .getFileName()
                    .toString();

            switch (fileName) {

                case "pom.xml" -> detectPom(file, frameworks);

                case "package.json" -> detectPackageJson(file, frameworks);

                default -> {
                    // Ignore unsupported files
                }
            }
        }

        return frameworks;
    }

    private void detectPom(ScannedFile file, Set<Framework> frameworks) {

        String content = read(file);

        if (content.contains("spring-boot-starter")) {
            frameworks.add(Framework.SPRING_BOOT);
        }
    }

    private void detectPackageJson(ScannedFile file, Set<Framework> frameworks) {

        String content = read(file).toLowerCase(Locale.ROOT);

        if (content.contains("\"react\"")) {
            frameworks.add(Framework.REACT);
        }

        if (content.contains("\"next\"")) {
            frameworks.add(Framework.NEXT_JS);
        }

        if (content.contains("\"express\"")) {
            frameworks.add(Framework.EXPRESS);
        }
    }

    private String read(ScannedFile file) {

        try {
            return Files.readString(file.absolutePath());
        } catch (IOException e) {
            return "";
        }
    }
}