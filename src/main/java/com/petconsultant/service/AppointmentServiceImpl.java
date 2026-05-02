package com.petconsultant.service;

import com.petconsultant.constants.AppConstants;
import com.petconsultant.entity.AppointmentEntity;
import com.petconsultant.entity.PetEntity;
import com.petconsultant.entity.UserEntity;
import com.petconsultant.entity.VetEntity;
import com.petconsultant.enumerator.AppointmentStatus;
import com.petconsultant.exception.ResourceNotFoundException;
import com.petconsultant.repository.AppointmentRepository;
import com.petconsultant.repository.PetRepository;
import com.petconsultant.repository.UserRepository;
import com.petconsultant.repository.VetRepository;
import com.petconsultant.request.AppointmentRequest;
import com.petconsultant.response.ApiResponse;
import com.petconsultant.response.AppointmentResponse;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
public class AppointmentServiceImpl implements AppointmentService {

    private final AppointmentRepository appointmentRepository;
    private final UserRepository        userRepository;
    private final VetRepository         vetRepository;
    private final PetRepository         petRepository;

    public AppointmentServiceImpl(AppointmentRepository appointmentRepository,
                                   UserRepository userRepository,
                                   VetRepository vetRepository,
                                   PetRepository petRepository) {
        this.appointmentRepository = appointmentRepository;
        this.userRepository        = userRepository;
        this.vetRepository         = vetRepository;
        this.petRepository         = petRepository;
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

    private AppointmentResponse mapToResponse(AppointmentEntity appointment) {
        AppointmentResponse res = new AppointmentResponse();
        res.setId(appointment.getId());
        res.setUserId(appointment.getUserId());
        res.setVetId(appointment.getVetId());
        res.setPetId(appointment.getPetId());
        res.setAppointmentDate(appointment.getAppointmentDate());
        res.setAppointmentTime(appointment.getAppointmentTime());
        res.setStatus(appointment.getStatus() != null ? appointment.getStatus().name() : null);
        res.setType(appointment.getType() != null ? appointment.getType().name() : null);
        res.setNotes(appointment.getNotes());
        res.setCreatedAt(appointment.getCreatedAt());

        vetRepository.findById(appointment.getVetId())
                .ifPresent(v -> res.setVetName(v.getFullName()));

        if (appointment.getPetId() != null) {
            petRepository.findById(appointment.getPetId())
                    .ifPresent(p -> res.setPetName(p.getName()));
        }
        return res;
    }

    @Override
    public ApiResponse bookAppointment(AppointmentRequest request) {
        UserEntity user = getCurrentUser();

        AppointmentEntity appointment = new AppointmentEntity();
        appointment.setUserId(user.getId());
        appointment.setVetId(request.getVetId());
        appointment.setPetId(request.getPetId());
        appointment.setAppointmentDate(request.getAppointmentDate());
        appointment.setAppointmentTime(request.getAppointmentTime());
        appointment.setType(request.getType());
        appointment.setNotes(request.getNotes());

        appointmentRepository.save(appointment);

        ApiResponse response = new ApiResponse();
        response.setStatus(AppConstants.SUCCESS);
        response.setMessage("Appointment booked successfully");
        response.setData(null);
        return response;
    }

    @Override
    public List<AppointmentResponse> getMyAppointments() {
        UserEntity user = getCurrentUser();
        List<AppointmentEntity> list = appointmentRepository.findByUserId(user.getId());
        List<AppointmentResponse> result = new ArrayList<>();
        for (AppointmentEntity a : list) {
            result.add(mapToResponse(a));
        }
        return result;
    }

    @Override
    public ApiResponse cancelAppointment(Long id) {
        AppointmentEntity appointment = appointmentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Appointment not found"));
        appointment.setStatus(AppointmentStatus.CANCELLED);
        appointmentRepository.save(appointment);

        ApiResponse response = new ApiResponse();
        response.setStatus(AppConstants.SUCCESS);
        response.setMessage("Appointment cancelled");
        response.setData(null);
        return response;
    }

    @Override
    public ApiResponse rescheduleAppointment(Long id, AppointmentRequest request) {
        AppointmentEntity appointment = appointmentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Appointment not found"));
        appointment.setAppointmentDate(request.getAppointmentDate());
        appointment.setAppointmentTime(request.getAppointmentTime());
        appointment.setStatus(AppointmentStatus.RESCHEDULED);
        appointmentRepository.save(appointment);

        ApiResponse response = new ApiResponse();
        response.setStatus(AppConstants.SUCCESS);
        response.setMessage("Appointment rescheduled");
        response.setData(null);
        return response;
    }

    @Override
    public List<AppointmentResponse> getUpcomingAppointments() {
        UserEntity user = getCurrentUser();
        List<AppointmentEntity> list = appointmentRepository
                .findByUserIdAndAppointmentDateGreaterThanEqualAndStatusNot(
                        user.getId(), LocalDate.now(), AppointmentStatus.CANCELLED);
        List<AppointmentResponse> result = new ArrayList<>();
        for (AppointmentEntity a : list) {
            result.add(mapToResponse(a));
        }
        return result;
    }
}
