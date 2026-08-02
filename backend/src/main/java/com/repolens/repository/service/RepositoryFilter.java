package com.repolens.repository.service;

public record RepositoryFilter(

        String query,

        String owner,

        String language,

        String visibility

) {
}