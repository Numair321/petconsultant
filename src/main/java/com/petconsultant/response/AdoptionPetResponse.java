package com.petconsultant.response;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class AdoptionPetResponse {
    private Long id;
    private String name;
    private String type;
    private String breed;
    private String age;
    private String gender;
    private String description;
    private String photo;
    private Boolean isAvailable;
    private LocalDateTime createdAt;
}
