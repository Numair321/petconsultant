package com.petconsultant.service;

import com.petconsultant.request.AppointmentRequest;
import com.petconsultant.response.ApiResponse;
import com.petconsultant.response.AppointmentResponse;
import java.util.List;

public interface AppointmentService {
    ApiResponse bookAppointment(AppointmentRequest request);
    List<AppointmentResponse> getMyAppointments();
    ApiResponse cancelAppointment(Long id);
    ApiResponse rescheduleAppointment(Long id, AppointmentRequest request);
    List<AppointmentResponse> getUpcomingAppointments();
}
