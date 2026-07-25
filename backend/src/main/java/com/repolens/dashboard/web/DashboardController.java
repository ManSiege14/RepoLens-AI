package com.repolens.dashboard.web;

import com.repolens.dashboard.service.DashboardStatsService;
import com.repolens.dashboard.web.dto.DashboardStatsResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/dashboard")
@Tag(
        name = "Dashboard",
        description = "Dashboard statistics endpoints"
)
public class DashboardController {

    private final DashboardStatsService dashboardStatsService;

    public DashboardController(
            DashboardStatsService dashboardStatsService
    ) {
        this.dashboardStatsService = dashboardStatsService;
    }

    @Operation(
            summary = "Get dashboard statistics",
            description = "Returns overall statistics for the RepoLens dashboard"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Dashboard statistics retrieved successfully"
            )
    })
    @GetMapping("/stats")
    public DashboardStatsResponse getDashboardStats() {
        return dashboardStatsService.getDashboardStats();
    }
}