package com.repolens.dashboard.web.dto;

public record DashboardStatsResponse(

        long repositoryCount,

        long analysisCount,

        int languagesDetected,

        int frameworksDetected,

        int buildToolsDetected,

        int infrastructureDetected,

        String mostUsedLanguage

) {
}