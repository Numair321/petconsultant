package com.petconsultant.repository;

import com.petconsultant.entity.AdoptionPetEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface AdoptionPetRepository extends JpaRepository<AdoptionPetEntity, Long> {
    List<AdoptionPetEntity> findByIsAvailableTrue();
}
