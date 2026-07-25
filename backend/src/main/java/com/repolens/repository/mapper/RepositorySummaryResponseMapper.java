package com.repolens.repository.web.mapper;

import com.repolens.repository.domain.GitRepository;
import com.repolens.repository.web.dto.RepositorySummaryResponse;
import org.springframework.stereotype.Component;

@Component
public class RepositorySummaryResponseMapper {

    public RepositorySummaryResponse toResponse(
            GitRepository repository
    ) {

        return new RepositorySummaryResponse(

                repository.getId(),

                repository.getOwner(),

                repository.getName(),

                repository.getPrimaryLanguage(),

                repository.getStars(),

                repository.getForks()
        );
    }
}