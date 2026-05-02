package com.petconsultant.repository;

import com.petconsultant.entity.CallEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface CallRepository extends JpaRepository<CallEntity, Long> {
    List<CallEntity> findByUserIdOrderByCreatedAtDesc(Long userId);
}
