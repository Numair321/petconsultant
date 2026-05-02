package com.petconsultant.repository;

import com.petconsultant.entity.AppointmentEntity;
import com.petconsultant.enumerator.AppointmentStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.time.LocalDate;
import java.util.List;

@Repository
public interface AppointmentRepository extends JpaRepository<AppointmentEntity, Long> {
    List<AppointmentEntity> findByUserId(Long userId);
    List<AppointmentEntity> findByUserIdAndAppointmentDateGreaterThanEqualAndStatusNot(
        Long userId, LocalDate date, AppointmentStatus status);
}
