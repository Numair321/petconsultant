package com.petconsultant.repository;

import com.petconsultant.entity.VetEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface VetRepository extends JpaRepository<VetEntity, Long> {
    List<VetEntity> findByIsAvailableTrue();
    List<VetEntity> findBySpecializationContainingIgnoreCase(String specialization);
    List<VetEntity> findTop5ByIsAvailableTrueOrderByRatingDesc();
}
