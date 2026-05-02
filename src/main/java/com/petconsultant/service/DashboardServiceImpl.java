package com.petconsultant.service;

import com.petconsultant.entity.BannerEntity;
import com.petconsultant.entity.UserEntity;
import com.petconsultant.exception.ResourceNotFoundException;
import com.petconsultant.repository.BannerRepository;
import com.petconsultant.repository.UserRepository;
import com.petconsultant.response.AppointmentResponse;
import com.petconsultant.response.VetResponse;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class DashboardServiceImpl implements DashboardService {

    private final BannerRepository      bannerRepository;
    private final VetService            vetService;
    private final AppointmentService    appointmentService;
    private final UserRepository        userRepository;

    public DashboardServiceImpl(BannerRepository bannerRepository,
                                 VetService vetService,
                                 AppointmentService appointmentService,
                                 UserRepository userRepository) {
        this.bannerRepository   = bannerRepository;
        this.vetService         = vetService;
        this.appointmentService = appointmentService;
        this.userRepository     = userRepository;
    }

    @Override
    public List<Map<String, Object>> getBanners() {
        List<BannerEntity> banners = bannerRepository.findByIsActiveTrue();
        List<Map<String, Object>> result = new ArrayList<>();
        for (BannerEntity banner : banners) {
            Map<String, Object> map = new HashMap<>();
            map.put("id", banner.getId());
            map.put("title", banner.getTitle());
            map.put("imageUrl", banner.getImageUrl());
            result.add(map);
        }
        return result;
    }

    @Override
    public List<VetResponse> getFeaturedVets() {
        return vetService.getFeaturedVets();
    }

    @Override
    public List<AppointmentResponse> getUpcomingAppointments() {
        return appointmentService.getUpcomingAppointments();
    }
}
