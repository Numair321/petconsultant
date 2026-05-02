package com.petconsultant.service;

import com.petconsultant.constants.AppConstants;
import com.petconsultant.entity.PetEntity;
import com.petconsultant.entity.UserEntity;
import com.petconsultant.exception.ResourceNotFoundException;
import com.petconsultant.repository.PetRepository;
import com.petconsultant.repository.UserRepository;
import com.petconsultant.request.PetRequest;
import com.petconsultant.response.ApiResponse;
import com.petconsultant.response.PetResponse;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;

@Service
public class PetServiceImpl implements PetService {

    private final PetRepository  petRepository;
    private final UserRepository userRepository;

    public PetServiceImpl(PetRepository petRepository, UserRepository userRepository) {
        this.petRepository  = petRepository;
        this.userRepository = userRepository;
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
    public ApiResponse addPet(PetRequest request) {
        UserEntity user = getCurrentUser();

        PetEntity pet = new PetEntity();
        pet.setUserId(user.getId());
        pet.setName(request.getName());
        pet.setType(request.getType());
        pet.setBreed(request.getBreed());
        pet.setAge(request.getAge());
        pet.setWeight(request.getWeight());
        pet.setGender(request.getGender());
        pet.setDescription(request.getDescription());
        pet.setPhoto(request.getPhoto());

        petRepository.save(pet);

        ApiResponse response = new ApiResponse();
        response.setStatus(AppConstants.SUCCESS);
        response.setMessage("Pet added successfully");
        response.setData(null);
        return response;
    }

    @Override
    public List<PetResponse> getAllMyPets() {
        UserEntity user = getCurrentUser();
        List<PetEntity> pets = petRepository.findByUserId(user.getId());

        List<PetResponse> result = new ArrayList<>();
        for (PetEntity pet : pets) {
            PetResponse res = new PetResponse();
            res.setId(pet.getId());
            res.setUserId(pet.getUserId());
            res.setName(pet.getName());
            res.setType(pet.getType() != null ? pet.getType().name() : null);
            res.setBreed(pet.getBreed());
            res.setAge(pet.getAge());
            res.setWeight(pet.getWeight());
            res.setGender(pet.getGender());
            res.setDescription(pet.getDescription());
            res.setPhoto(pet.getPhoto());
            res.setCreatedAt(pet.getCreatedAt());
            result.add(res);
        }
        return result;
    }

    @Override
    public PetResponse getPetById(Long petId) {
        PetEntity pet = petRepository.findById(petId)
                .orElseThrow(() -> new ResourceNotFoundException("Pet not found"));

        PetResponse res = new PetResponse();
        res.setId(pet.getId());
        res.setUserId(pet.getUserId());
        res.setName(pet.getName());
        res.setType(pet.getType() != null ? pet.getType().name() : null);
        res.setBreed(pet.getBreed());
        res.setAge(pet.getAge());
        res.setWeight(pet.getWeight());
        res.setGender(pet.getGender());
        res.setDescription(pet.getDescription());
        res.setPhoto(pet.getPhoto());
        res.setCreatedAt(pet.getCreatedAt());
        return res;
    }

    @Override
    public ApiResponse updatePet(Long petId, PetRequest request) {
        PetEntity pet = petRepository.findById(petId)
                .orElseThrow(() -> new ResourceNotFoundException("Pet not found"));

        pet.setName(request.getName());
        pet.setType(request.getType());
        pet.setBreed(request.getBreed());
        pet.setAge(request.getAge());
        pet.setWeight(request.getWeight());
        pet.setGender(request.getGender());
        pet.setDescription(request.getDescription());
        pet.setPhoto(request.getPhoto());

        petRepository.save(pet);

        ApiResponse response = new ApiResponse();
        response.setStatus(AppConstants.SUCCESS);
        response.setMessage("Pet updated successfully");
        response.setData(null);
        return response;
    }

    @Override
    public ApiResponse deletePet(Long petId) {
        PetEntity pet = petRepository.findById(petId)
                .orElseThrow(() -> new ResourceNotFoundException("Pet not found"));
        petRepository.delete(pet);

        ApiResponse response = new ApiResponse();
        response.setStatus(AppConstants.SUCCESS);
        response.setMessage("Pet deleted successfully");
        response.setData(null);
        return response;
    }
}
