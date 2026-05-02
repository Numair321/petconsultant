package com.petconsultant.service;

import com.petconsultant.request.PetRequest;
import com.petconsultant.response.ApiResponse;
import com.petconsultant.response.PetResponse;
import java.util.List;

public interface PetService {
    ApiResponse addPet(PetRequest request);
    List<PetResponse> getAllMyPets();
    PetResponse getPetById(Long petId);
    ApiResponse updatePet(Long petId, PetRequest request);
    ApiResponse deletePet(Long petId);
}
