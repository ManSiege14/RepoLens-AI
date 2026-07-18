package com.repolens.repository.persistence;

import com.repolens.repository.domain.GitRepository;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

@org.springframework.stereotype.Repository
public interface GitRepositoryRepository
        extends JpaRepository<GitRepository, UUID> {

}