package com.repolens.analysis.detector;

import com.repolens.analysis.snapshot.RepositorySnapshot;
import org.springframework.stereotype.Component;

@Component
public class DefaultLicenseDetector
        implements LicenseDetector {

    @Override
    public Boolean detect(RepositorySnapshot snapshot) {

        return snapshot.files()
                .stream()
                .anyMatch(file ->
                        file.name().equalsIgnoreCase("LICENSE")
                                || file.name().equalsIgnoreCase("LICENSE.md")
                                || file.name().equalsIgnoreCase("LICENSE.txt")
                );
    }
}