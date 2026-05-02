package com.petconsultant.service;

import com.petconsultant.response.VetResponse;
import java.util.List;

public interface VetService {
    List<VetResponse> getAllVets();
    VetResponse getVetById(Long vetId);
    List<VetResponse> searchVets(String specialization);
    List<String> getVetSlots(Long vetId);
    List<VetResponse> getFeaturedVets();
}
