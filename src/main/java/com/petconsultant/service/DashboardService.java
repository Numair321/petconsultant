package com.petconsultant.service;

import com.petconsultant.response.AppointmentResponse;
import com.petconsultant.response.VetResponse;
import java.util.List;
import java.util.Map;

public interface DashboardService {
    List<Map<String, Object>> getBanners();
    List<VetResponse> getFeaturedVets();
    List<AppointmentResponse> getUpcomingAppointments();
}
