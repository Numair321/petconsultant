package com.petconsultant.controller;

import com.petconsultant.response.ApiResponse;
import com.petconsultant.service.DashboardService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/dashboard")
public class DashboardController {

    private final DashboardService dashboardService;

    public DashboardController(DashboardService dashboardService) {
        this.dashboardService = dashboardService;
    }

    @GetMapping("/banners")
    public ResponseEntity<ApiResponse> getBanners() {
        ApiResponse response = new ApiResponse("SUCCESS", "Banners fetched", dashboardService.getBanners());
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/featured-vets")
    public ResponseEntity<ApiResponse> getFeaturedVets() {
        ApiResponse response = new ApiResponse("SUCCESS", "Featured vets fetched", dashboardService.getFeaturedVets());
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/upcoming-appointments")
    public ResponseEntity<ApiResponse> getUpcomingAppointments() {
        ApiResponse response = new ApiResponse("SUCCESS", "Upcoming appointments", dashboardService.getUpcomingAppointments());
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
