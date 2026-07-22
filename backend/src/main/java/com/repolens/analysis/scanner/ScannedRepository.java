package com.repolens.analysis.scanner;

import java.nio.file.Path;
import java.util.List;

public record ScannedRepository(
        Path root,
        List<ScannedFile> files
) {
}