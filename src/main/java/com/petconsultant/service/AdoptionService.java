package com.petconsultant.service;

import com.petconsultant.request.AdoptionRequest;
import com.petconsultant.response.AdoptionPetResponse;
import com.petconsultant.response.ApiResponse;
import java.util.List;

public interface AdoptionService {
    List<AdoptionPetResponse> getAllAdoptionPets();
    AdoptionPetResponse getAdoptionPetById(Long id);
    ApiResponse submitAdoptionRequest(AdoptionRequest request);
    List<ApiResponse> getMyAdoptionRequests(); // Simplified for now
}
