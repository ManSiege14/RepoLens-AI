package com.repolens.analysis.scanner;

import java.nio.file.Path;

public interface FileScanner {

    ScannedRepository scan(Path repositoryRoot);

}