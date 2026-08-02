package com.repolens.shared.web.mapper;

import com.repolens.shared.web.dto.PagedResponse;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

@Component
public class PagedResponseMapper {

    public <T> PagedResponse<T> toResponse(
            Page<T> page
    ) {

        return new PagedResponse<>(

                page.getContent(),

                page.getNumber(),

                page.getSize(),

                page.getTotalElements(),

                page.getTotalPages(),

                page.isFirst(),

                page.isLast()
        );
    }
}