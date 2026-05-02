package com.petconsultant.repository;

import com.petconsultant.entity.AdoptionRequestEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface AdoptionRequestRepository extends JpaRepository<AdoptionRequestEntity, Long> {
    List<AdoptionRequestEntity> findByUserId(Long userId);
}
