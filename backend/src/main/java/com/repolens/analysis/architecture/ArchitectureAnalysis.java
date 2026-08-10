package com.repolens.analysis.architecture;

import java.util.List;

public record ArchitectureAnalysis(

        ArchitectureType primaryArchitecture,

        List<ArchitectureType> detectedArchitectures,

        List<ArchitectureEvidence> evidence

) {
}