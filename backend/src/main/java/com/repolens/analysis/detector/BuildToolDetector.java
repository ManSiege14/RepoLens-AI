package com.repolens.analysis.detector;

import com.repolens.analysis.scanner.ScannedRepository;

import java.util.Set;

public interface BuildToolDetector {

    Set<BuildTool> detect(ScannedRepository repository);
}