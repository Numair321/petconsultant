package com.petconsultant.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.time.LocalDateTime;

@Entity
@Table(name = "adoption_pets")
@Getter
@Setter
@NoArgsConstructor
public class AdoptionPetEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "type")
    private String type; // e.g., Dog, Cat

    @Column(name = "breed")
    private String breed;

    @Column(name = "age")
    private String age;

    @Column(name = "gender")
    private String gender;

    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    @Column(name = "photo")
    private String photo;

    @Column(name = "is_available")
    private Boolean isAvailable;

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
        if (this.isAvailable == null) this.isAvailable = true;
    }
}
