package com.repolens.analysis.scanner;

import java.nio.file.Path;

/**
 * Represents a file discovered during repository scanning.
 *
 * @param absolutePath Absolute path on the local filesystem.
 * @param relativePath Path relative to the repository root.
 */
public record ScannedFile(
        Path absolutePath,
        Path relativePath
) {
}