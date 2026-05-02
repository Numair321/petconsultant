package com.petconsultant.controller;

import com.petconsultant.request.PetRequest;
import com.petconsultant.response.ApiResponse;
import com.petconsultant.response.PetResponse;
import com.petconsultant.service.PetService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/v1/pet")
public class PetController {

    private final PetService petService;

    public PetController(PetService petService) {
        this.petService = petService;
    }

    // POST /test/v1/api/pet/add
    @PostMapping("/add")
    public ResponseEntity<ApiResponse> addPet(@Valid @RequestBody PetRequest request) {
        ApiResponse response = petService.addPet(request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    // GET /test/v1/api/pet/all
    @GetMapping("/all")
    public ResponseEntity<ApiResponse> getAllMyPets() {
        List<PetResponse> pets = petService.getAllMyPets();
        ApiResponse response = new ApiResponse("SUCCESS", "Pets fetched", pets);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    // GET /test/v1/api/pet/{petId}
    @GetMapping("/{petId}")
    public ResponseEntity<ApiResponse> getPetById(@PathVariable Long petId) {
        PetResponse pet = petService.getPetById(petId);
        ApiResponse response = new ApiResponse("SUCCESS", "Pet found", pet);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    // PUT /test/v1/api/pet/update/{petId}
    @PutMapping("/update/{petId}")
    public ResponseEntity<ApiResponse> updatePet(@PathVariable Long petId,
                                                  @Valid @RequestBody PetRequest request) {
        ApiResponse response = petService.updatePet(petId, request);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    // DELETE /test/v1/api/pet/delete/{petId}
    @DeleteMapping("/delete/{petId}")
    public ResponseEntity<ApiResponse> deletePet(@PathVariable Long petId) {
        ApiResponse response = petService.deletePet(petId);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
