package com.petconsultant.controller;

import com.petconsultant.request.AppointmentRequest;
import com.petconsultant.response.ApiResponse;
import com.petconsultant.response.AppointmentResponse;
import com.petconsultant.response.VetResponse;
import com.petconsultant.service.AppointmentService;
import com.petconsultant.service.VetService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/v1")
public class VetController {

    private final VetService         vetService;
    private final AppointmentService appointmentService;

    public VetController(VetService vetService, AppointmentService appointmentService) {
        this.vetService         = vetService;
        this.appointmentService = appointmentService;
    }

    // GET /test/v1/api/vet/all
    @GetMapping("/vet/all")
    public ResponseEntity<ApiResponse> getAllVets() {
        List<VetResponse> vets = vetService.getAllVets();
        ApiResponse response = new ApiResponse("SUCCESS", "Vets fetched", vets);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    // GET /test/v1/api/vet/{vetId}
    @GetMapping("/vet/{vetId}")
    public ResponseEntity<ApiResponse> getVetById(@PathVariable Long vetId) {
        VetResponse vet = vetService.getVetById(vetId);
        ApiResponse response = new ApiResponse("SUCCESS", "Vet found", vet);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    // GET /test/v1/api/vet/search?specialization=...
    @GetMapping("/vet/search")
    public ResponseEntity<ApiResponse> searchVets(@RequestParam String specialization) {
        List<VetResponse> vets = vetService.searchVets(specialization);
        ApiResponse response = new ApiResponse("SUCCESS", "Search results", vets);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    // GET /test/v1/api/vet/{vetId}/slots
    @GetMapping("/vet/{vetId}/slots")
    public ResponseEntity<ApiResponse> getVetSlots(@PathVariable Long vetId) {
        List<String> slots = vetService.getVetSlots(vetId);
        ApiResponse response = new ApiResponse("SUCCESS", "Slots fetched", slots);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    // POST /test/v1/api/appointment/book
    @PostMapping("/appointment/book")
    public ResponseEntity<ApiResponse> bookAppointment(@Valid @RequestBody AppointmentRequest request) {
        ApiResponse response = appointmentService.bookAppointment(request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    // GET /test/v1/api/appointment/my
    @GetMapping("/appointment/my")
    public ResponseEntity<ApiResponse> getMyAppointments() {
        List<AppointmentResponse> list = appointmentService.getMyAppointments();
        ApiResponse response = new ApiResponse("SUCCESS", "Appointments fetched", list);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    // PUT /test/v1/api/appointment/cancel/{id}
    @PutMapping("/appointment/cancel/{id}")
    public ResponseEntity<ApiResponse> cancelAppointment(@PathVariable Long id) {
        ApiResponse response = appointmentService.cancelAppointment(id);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    // PUT /test/v1/api/appointment/reschedule/{id}
    @PutMapping("/appointment/reschedule/{id}")
    public ResponseEntity<ApiResponse> rescheduleAppointment(@PathVariable Long id,
                                                              @Valid @RequestBody AppointmentRequest request) {
        ApiResponse response = appointmentService.rescheduleAppointment(id, request);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
