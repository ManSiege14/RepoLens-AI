package com.repolens.analysis.persistence;

import com.repolens.analysis.detector.BuildTool;
import com.repolens.analysis.detector.Framework;
import com.repolens.analysis.detector.Infrastructure;
import com.repolens.analysis.detector.ProgrammingLanguage;
import com.repolens.repository.domain.GitRepository;
import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Entity
@Table(name = "repository_analysis")
@Getter
@Setter
@NoArgsConstructor
public class RepositoryAnalysisEntity {

    @Id
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

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(
            name = "repository_analysis_build_tools",
            joinColumns = @JoinColumn(name = "analysis_id")
    )
    @Column(name = "build_tool")
    @Enumerated(EnumType.STRING)
    private Set<BuildTool> buildTools = new HashSet<>();

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(
            name = "repository_analysis_frameworks",
            joinColumns = @JoinColumn(name = "analysis_id")
    )
    @Column(name = "framework")
    @Enumerated(EnumType.STRING)
    private Set<Framework> frameworks = new HashSet<>();

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(
            name = "repository_analysis_infrastructure",
            joinColumns = @JoinColumn(name = "analysis_id")
    )
    @Column(name = "infrastructure")
    @Enumerated(EnumType.STRING)
    private Set<Infrastructure> infrastructure = new HashSet<>();

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(
            name = "repository_analysis_languages",
            joinColumns = @JoinColumn(name = "analysis_id")
    )
    @Column(name = "language")
    @Enumerated(EnumType.STRING)
    private Set<ProgrammingLanguage> languages = new HashSet<>();

    @PrePersist
    public void prePersist() {
        if (id == null) {
            id = UUID.randomUUID();
        }

        if (analyzedAt == null) {
            analyzedAt = Instant.now();
        }
    }

    public void setBuildTools(Set<BuildTool> buildTools) {
        this.buildTools = buildTools == null
                ? new HashSet<>()
                : new HashSet<>(buildTools);
    }

    public void setFrameworks(Set<Framework> frameworks) {
        this.frameworks = frameworks == null
                ? new HashSet<>()
                : new HashSet<>(frameworks);
    }

    public void setInfrastructure(Set<Infrastructure> infrastructure) {
        this.infrastructure = infrastructure == null
                ? new HashSet<>()
                : new HashSet<>(infrastructure);
    }

    public void setLanguages(Set<ProgrammingLanguage> languages) {
        this.languages = languages == null
                ? new HashSet<>()
                : new HashSet<>(languages);
    }
}