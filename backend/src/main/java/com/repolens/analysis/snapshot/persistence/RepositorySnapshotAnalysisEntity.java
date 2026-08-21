package com.repolens.analysis.snapshot.persistence;

import com.repolens.analysis.architecture.ArchitectureType;
import com.repolens.analysis.detector.BuildTool;
import com.repolens.analysis.detector.ProgrammingLanguage;
import com.repolens.analysis.health.HealthGrade;
import com.repolens.repository.domain.GitRepository;
import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.time.Instant;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Entity
@Table(name = "repository_snapshot_analysis")
@Getter
@Setter
@NoArgsConstructor
public class RepositorySnapshotAnalysisEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "repository_id",
            nullable = false,
            unique = true
    )
    private GitRepository repository;

    @Column(nullable = false)
    private Instant analyzedAt;

    @Column(nullable = false)
    private boolean readmePresent;

    @Column(nullable = false)
    private boolean dockerPresent;

    @Column(nullable = false)
    private boolean githubActionsPresent;

    @Column(nullable = false)
    private boolean licensePresent;

    @Column(nullable = false)
    private int totalFiles;

    @Column(nullable = false)
    private int totalDirectories;

    @Column(nullable = false)
    private int sourceFiles;

    @Column(nullable = false)
    private int documentationFiles;

    @Column(nullable = false)
    private int configurationFiles;

    @Enumerated(EnumType.STRING)
    private ArchitectureType primaryArchitecture;

    @Column(nullable = false)
    private int healthScore;

    @Enumerated(EnumType.STRING)
    private HealthGrade healthGrade;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(columnDefinition = "jsonb")
    private String architectureData;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(columnDefinition = "jsonb")
    private String healthData;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(
            name = "repository_snapshot_analysis_build_tools",
            joinColumns = @JoinColumn(name = "analysis_id")
    )
    @Column(name = "build_tool")
    @Enumerated(EnumType.STRING)
    private Set<BuildTool> buildTools = new HashSet<>();

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(
            name = "repository_snapshot_analysis_languages",
            joinColumns = @JoinColumn(name = "analysis_id")
    )
    @Column(name = "language")
    @Enumerated(EnumType.STRING)
    private Set<ProgrammingLanguage> languages = new HashSet<>();

    public void setBuildTools(Set<BuildTool> buildTools) {
        this.buildTools = buildTools == null
                ? new HashSet<>()
                : new HashSet<>(buildTools);
    }

    public void setLanguages(Set<ProgrammingLanguage> languages) {
        this.languages = languages == null
                ? new HashSet<>()
                : new HashSet<>(languages);
    }
}