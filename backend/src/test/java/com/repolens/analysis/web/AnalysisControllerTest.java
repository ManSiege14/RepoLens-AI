package com.repolens.analysis.web;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.repolens.analysis.detector.BuildTool;
import com.repolens.analysis.detector.Framework;
import com.repolens.analysis.detector.Infrastructure;
import com.repolens.analysis.detector.ProgrammingLanguage;
import com.repolens.analysis.model.RepositoryAnalysis;
import com.repolens.analysis.service.RepositoryAnalysisService;
import com.repolens.analysis.web.dto.AnalyzeRepositoryRequest;
import com.repolens.analysis.web.mapper.RepositoryAnalysisResponseMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Set;

import static org.hamcrest.Matchers.hasItem;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(AnalysisController.class)
@Import(RepositoryAnalysisResponseMapper.class)
class AnalysisControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private RepositoryAnalysisService repositoryAnalysisService;

    @Test
    void shouldAnalyzeRepository() throws Exception {
        String repositoryUrl = "https://github.com/example/demo";

        RepositoryAnalysis analysis = new RepositoryAnalysis(
                Set.of(BuildTool.MAVEN),
                Set.of(Framework.SPRING_BOOT),
                Set.of(Infrastructure.DOCKER),
                Set.of(ProgrammingLanguage.JAVA)
        );

        when(repositoryAnalysisService.analyze(repositoryUrl))
                .thenReturn(analysis);

        AnalyzeRepositoryRequest request =
                new AnalyzeRepositoryRequest(repositoryUrl);

        String json = objectMapper.writeValueAsString(request);

        mockMvc.perform(post("/api/analysis")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.buildTools").isArray())
                .andExpect(jsonPath("$.frameworks").isArray())
                .andExpect(jsonPath("$.infrastructure").isArray())
                .andExpect(jsonPath("$.languages").isArray())
                .andExpect(jsonPath("$.buildTools", hasItem("MAVEN")))
                .andExpect(jsonPath("$.frameworks", hasItem("SPRING_BOOT")))
                .andExpect(jsonPath("$.infrastructure", hasItem("DOCKER")))
                .andExpect(jsonPath("$.languages", hasItem("JAVA")));

        verify(repositoryAnalysisService).analyze(repositoryUrl);
    }

    @Test
    void shouldReturnBadRequestWhenRepositoryUrlIsBlank() throws Exception {
        String json = """
                {
                  "repositoryUrl": ""
                }
                """;

        mockMvc.perform(post("/api/analysis")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isBadRequest());

        verify(repositoryAnalysisService, never())
                .analyze(anyString());
    }
}