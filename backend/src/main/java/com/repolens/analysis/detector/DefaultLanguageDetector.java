package com.repolens.analysis.detector;

import com.repolens.analysis.scanner.ScannedFile;
import com.repolens.analysis.scanner.ScannedRepository;
import org.springframework.stereotype.Component;

import java.nio.file.Path;
import java.util.EnumSet;
import java.util.Map;
import java.util.Set;

@Component
public class DefaultLanguageDetector implements LanguageDetector {

    private static final Map<String, ProgrammingLanguage> EXTENSION_MAP = Map.of(
            "java", ProgrammingLanguage.JAVA,
            "kt", ProgrammingLanguage.KOTLIN,
            "js", ProgrammingLanguage.JAVASCRIPT,
            "ts", ProgrammingLanguage.TYPESCRIPT,
            "tsx", ProgrammingLanguage.TYPESCRIPT,
            "py", ProgrammingLanguage.PYTHON,
            "go", ProgrammingLanguage.GO,
            "rs", ProgrammingLanguage.RUST,
            "cs", ProgrammingLanguage.CSHARP
    );

    @Override
    public Set<ProgrammingLanguage> detect(ScannedRepository repository) {

        Set<ProgrammingLanguage> languages =
                EnumSet.noneOf(ProgrammingLanguage.class);

        for (ScannedFile file : repository.files()) {

            Path fileName = file.relativePath().getFileName();

            if (fileName == null) {
                continue;
            }

            String name = fileName.toString();

            int lastDot = name.lastIndexOf('.');

            if (lastDot < 0 || lastDot == name.length() - 1) {
                continue;
            }

            String extension = name.substring(lastDot + 1);

            ProgrammingLanguage language = EXTENSION_MAP.get(extension);

            if (language != null) {
                languages.add(language);
            }
        }

        return languages;
    }
}