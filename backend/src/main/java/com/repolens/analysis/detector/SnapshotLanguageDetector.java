package com.repolens.analysis.detector;

import com.repolens.analysis.snapshot.RepositorySnapshot;
import org.springframework.stereotype.Component;

import java.util.EnumSet;
import java.util.Map;
import java.util.Set;

@Component
public class SnapshotLanguageDetector
        implements RepositoryDetector<Set<ProgrammingLanguage>> {

    private static final Map<String, ProgrammingLanguage> EXTENSION_MAP =
            Map.of(
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
    public Set<ProgrammingLanguage> detect(
            RepositorySnapshot snapshot
    ) {

        Set<ProgrammingLanguage> languages =
                EnumSet.noneOf(ProgrammingLanguage.class);

        snapshot.files().forEach(file -> {

            String extension = file.extension();

            if (extension == null || extension.isBlank()) {
                return;
            }

            ProgrammingLanguage language =
                    EXTENSION_MAP.get(extension.toLowerCase());

            if (language != null) {
                languages.add(language);
            }
        });

        return languages;
    }
}