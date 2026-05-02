package com.petconsultant.repository;

import com.petconsultant.entity.ChatEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface ChatRepository extends JpaRepository<ChatEntity, Long> {
    List<ChatEntity> findByUserId(Long userId);
    Optional<ChatEntity> findByUserIdAndVetId(Long userId, Long vetId);
}
