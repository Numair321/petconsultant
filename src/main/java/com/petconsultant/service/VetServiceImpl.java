package com.petconsultant.service;

import com.petconsultant.entity.VetEntity;
import com.petconsultant.exception.ResourceNotFoundException;
import com.petconsultant.repository.VetRepository;
import com.petconsultant.response.VetResponse;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
public class VetServiceImpl implements VetService {

    private final VetRepository vetRepository;

    public VetServiceImpl(VetRepository vetRepository) {
        this.vetRepository = vetRepository;
    }

    private VetResponse mapToResponse(VetEntity vet) {
        VetResponse res = new VetResponse();
        res.setId(vet.getId());
        res.setFullName(vet.getFullName());
        res.setEmail(vet.getEmail());
        res.setPhone(vet.getPhone());
        res.setSpecialization(vet.getSpecialization());
        res.setExperienceYears(vet.getExperienceYears());
        res.setRating(vet.getRating());
        res.setDescription(vet.getDescription());
        res.setPhoto(vet.getPhoto());
        res.setConsultationFee(vet.getConsultationFee());
        res.setAvailableSlots(vet.getAvailableSlots());
        res.setIsAvailable(vet.getIsAvailable());
        return res;
    }

    @Override
    public List<VetResponse> getAllVets() {
        List<VetEntity> vets = vetRepository.findByIsAvailableTrue();
        List<VetResponse> result = new ArrayList<>();
        for (VetEntity vet : vets) {
            result.add(mapToResponse(vet));
        }
        return result;
    }

    @Override
    public VetResponse getVetById(Long vetId) {
        VetEntity vet = vetRepository.findById(vetId)
                .orElseThrow(() -> new ResourceNotFoundException("Vet not found"));
        return mapToResponse(vet);
    }

    @Override
    public List<VetResponse> searchVets(String specialization) {
        List<VetEntity> vets = vetRepository.findBySpecializationContainingIgnoreCase(specialization);
        List<VetResponse> result = new ArrayList<>();
        for (VetEntity vet : vets) {
            result.add(mapToResponse(vet));
        }
        return result;
    }

    @Override
    public List<String> getVetSlots(Long vetId) {
        VetEntity vet = vetRepository.findById(vetId)
                .orElseThrow(() -> new ResourceNotFoundException("Vet not found"));
        String slots = vet.getAvailableSlots();
        if (slots == null || slots.isEmpty()) return new ArrayList<>();
        return Arrays.asList(slots.split(","));
    }

    @Override
    public List<VetResponse> getFeaturedVets() {
        List<VetEntity> vets = vetRepository.findTop5ByIsAvailableTrueOrderByRatingDesc();
        List<VetResponse> result = new ArrayList<>();
        for (VetEntity vet : vets) {
            result.add(mapToResponse(vet));
        }
        return result;
    }
}
