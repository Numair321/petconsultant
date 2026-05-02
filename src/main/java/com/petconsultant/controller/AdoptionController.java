package com.petconsultant.controller;

import com.petconsultant.request.AdoptionRequest;
import com.petconsultant.response.AdoptionPetResponse;
import com.petconsultant.response.ApiResponse;
import com.petconsultant.service.AdoptionService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/v1/adoption")
public class AdoptionController {

    private final AdoptionService adoptionService;

    public AdoptionController(AdoptionService adoptionService) {
        this.adoptionService = adoptionService;
    }

    @GetMapping("/pets")
    public ResponseEntity<ApiResponse> getAllAdoptionPets() {
        List<AdoptionPetResponse> pets = adoptionService.getAllAdoptionPets();
        return ResponseEntity.ok(new ApiResponse("SUCCESS", "Adoption pets fetched", pets));
    }

    @GetMapping("/pet/{id}")
    public ResponseEntity<ApiResponse> getAdoptionPetById(@PathVariable Long id) {
        AdoptionPetResponse pet = adoptionService.getAdoptionPetById(id);
        return ResponseEntity.ok(new ApiResponse("SUCCESS", "Pet found", pet));
    }

    @PostMapping("/request")
    public ResponseEntity<ApiResponse> submitRequest(@RequestBody AdoptionRequest request) {
        ApiResponse response = adoptionService.submitAdoptionRequest(request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping("/my-requests")
    public ResponseEntity<ApiResponse> getMyRequests() {
        return ResponseEntity.ok(new ApiResponse("SUCCESS", "My adoption requests", adoptionService.getMyAdoptionRequests()));
    }
}
