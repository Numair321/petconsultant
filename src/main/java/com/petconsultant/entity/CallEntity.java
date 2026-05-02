package com.petconsultant.entity;

import com.petconsultant.enumerator.CallType;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.time.LocalDateTime;

@Entity
@Table(name = "calls")
@Getter
@Setter
@NoArgsConstructor
public class CallEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Column(name = "vet_id", nullable = false)
    private Long vetId;

    @Enumerated(EnumType.STRING)
    @Column(name = "call_type")
    private CallType callType;

    @Column(name = "token", columnDefinition = "TEXT")
    private String token;

    @Column(name = "status")
    private String status;

    @Column(name = "start_time")
    private LocalDateTime startTime;

    @Column(name = "end_time")
    private LocalDateTime endTime;

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
        if (this.status == null) this.status = "INITIATED";
    }
}
