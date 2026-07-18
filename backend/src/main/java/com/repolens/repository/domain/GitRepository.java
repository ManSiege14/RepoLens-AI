package com.repolens.repository.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "repositories")
@Getter
@Setter
@NoArgsConstructor
public class GitRepository {

    @Id
    private UUID id;

    private Long githubRepositoryId;

    private String owner;

    private String name;

    private String fullName;

    private String htmlUrl;

    private String description;

    private String defaultBranch;

    private String visibility;

    private Integer stars;

    private Integer forks;

    private String primaryLanguage;

    private Instant createdAt;

    private Instant updatedAt;

    @PrePersist
    public void prePersist() {
        if (id == null) {
            id = UUID.randomUUID();
        }

        Instant now = Instant.now();
        createdAt = now;
        updatedAt = now;
    }

    @PreUpdate
    public void preUpdate() {
        updatedAt = Instant.now();
    }
}