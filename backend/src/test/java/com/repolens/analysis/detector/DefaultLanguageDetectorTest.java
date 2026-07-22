package com.repolens.analysis.detector;

import com.repolens.analysis.scanner.ScannedFile;
import com.repolens.analysis.scanner.ScannedRepository;
import org.junit.jupiter.api.Test;

import java.nio.file.Path;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;

class DefaultLanguageDetectorTest {

    private final LanguageDetector detector =
            new DefaultLanguageDetector();

    @Test
    void shouldDetectJava() {

        assertEquals(
                Set.of(ProgrammingLanguage.JAVA),
                detector.detect(repository("src/Main.java"))
        );
    }

    @Test
    void shouldDetectKotlin() {

        assertEquals(
                Set.of(ProgrammingLanguage.KOTLIN),
                detector.detect(repository("App.kt"))
        );
    }

    @Test
    void shouldDetectTypeScript() {

        assertEquals(
                Set.of(ProgrammingLanguage.TYPESCRIPT),
                detector.detect(repository("src/App.tsx"))
        );
    }

    @Test
    void shouldDetectMultipleLanguages() {

        assertEquals(
                Set.of(
                        ProgrammingLanguage.JAVA,
                        ProgrammingLanguage.TYPESCRIPT,
                        ProgrammingLanguage.PYTHON
                ),
                detector.detect(repository(
                        "Main.java",
                        "frontend/App.tsx",
                        "script.py"
                ))
        );
    }

    @Test
    void shouldReturnEmptySet() {

        assertEquals(
                Set.of(),
                detector.detect(repository(
                        "README.md",
                        "Dockerfile"
                ))
        );
    }

    private ScannedRepository repository(String... paths) {

        List<ScannedFile> files = List.of(paths)
                .stream()
                .map(path -> {
                    Path relative = Path.of(path);

                    return new ScannedFile(
                            relative,
                            relative
                    );
                })
                .toList();

        return new ScannedRepository(
                Path.of("."),
                files
        );
    }
}