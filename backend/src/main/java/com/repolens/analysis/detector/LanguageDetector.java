package com.repolens.analysis.detector;

import com.repolens.analysis.scanner.ScannedRepository;

import java.util.Set;

public interface LanguageDetector {

    Set<ProgrammingLanguage> detect(ScannedRepository repository);

}