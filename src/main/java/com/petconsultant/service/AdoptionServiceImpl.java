package com.petconsultant.service;

import com.petconsultant.constants.AppConstants;
import com.petconsultant.entity.AdoptionPetEntity;
import com.petconsultant.entity.AdoptionRequestEntity;
import com.petconsultant.entity.UserEntity;
import com.petconsultant.exception.ResourceNotFoundException;
import com.petconsultant.repository.AdoptionPetRepository;
import com.petconsultant.repository.AdoptionRequestRepository;
import com.petconsultant.repository.UserRepository;
import com.petconsultant.request.AdoptionRequest;
import com.petconsultant.response.AdoptionPetResponse;
import com.petconsultant.response.ApiResponse;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;

@Service
public class AdoptionServiceImpl implements AdoptionService {

    private final AdoptionPetRepository     adoptionPetRepository;
    private final AdoptionRequestRepository adoptionRequestRepository;
    private final UserRepository            userRepository;

    public AdoptionServiceImpl(AdoptionPetRepository adoptionPetRepository,
                                AdoptionRequestRepository adoptionRequestRepository,
                                UserRepository userRepository) {
        this.adoptionPetRepository     = adoptionPetRepository;
        this.adoptionRequestRepository = adoptionRequestRepository;
        this.userRepository            = userRepository;
    }

    private UserEntity getCurrentUser() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        if ("anonymousUser".equals(email)) {
            return userRepository.findAll().stream().findFirst()
                    .orElseThrow(() -> new ResourceNotFoundException("No user found in DB. Please register first."));
        }
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException(AppConstants.USER_NOT_FOUND));
    }

    @Override
    public List<AdoptionPetResponse> getAllAdoptionPets() {
        List<AdoptionPetEntity> pets = adoptionPetRepository.findByIsAvailableTrue();
        List<AdoptionPetResponse> result = new ArrayList<>();
        for (AdoptionPetEntity pet : pets) {
            AdoptionPetResponse res = new AdoptionPetResponse();
            res.setId(pet.getId());
            res.setName(pet.getName());
            res.setType(pet.getType());
            res.setBreed(pet.getBreed());
            res.setAge(pet.getAge());
            res.setGender(pet.getGender());
            res.setDescription(pet.getDescription());
            res.setPhoto(pet.getPhoto());
            res.setIsAvailable(pet.getIsAvailable());
            res.setCreatedAt(pet.getCreatedAt());
            result.add(res);
        }
        return result;
    }

    @Override
    public AdoptionPetResponse getAdoptionPetById(Long id) {
        AdoptionPetEntity pet = adoptionPetRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Adoption pet not found"));
        AdoptionPetResponse res = new AdoptionPetResponse();
        res.setId(pet.getId());
        res.setName(pet.getName());
        res.setType(pet.getType());
        res.setBreed(pet.getBreed());
        res.setAge(pet.getAge());
        res.setGender(pet.getGender());
        res.setDescription(pet.getDescription());
        res.setPhoto(pet.getPhoto());
        res.setIsAvailable(pet.getIsAvailable());
        res.setCreatedAt(pet.getCreatedAt());
        return res;
    }

    @Override
    public ApiResponse submitAdoptionRequest(AdoptionRequest request) {
        UserEntity user = getCurrentUser();
        adoptionPetRepository.findById(request.getPetId())
                .orElseThrow(() -> new ResourceNotFoundException("Adoption pet not found"));

        AdoptionRequestEntity entity = new AdoptionRequestEntity();
        entity.setPetId(request.getPetId());
        entity.setUserId(user.getId());
        entity.setMessage(request.getMessage());
        adoptionRequestRepository.save(entity);

        return new ApiResponse(AppConstants.SUCCESS, "Adoption request submitted successfully", null);
    }

    @Override
    public List<ApiResponse> getMyAdoptionRequests() {
        UserEntity user = getCurrentUser();
        List<AdoptionRequestEntity> list = adoptionRequestRepository.findByUserId(user.getId());
        List<ApiResponse> result = new ArrayList<>();
        for (AdoptionRequestEntity e : list) {
            result.add(new ApiResponse(AppConstants.SUCCESS, "Request status: " + e.getStatus(), e));
        }
        return result;
    }
}
