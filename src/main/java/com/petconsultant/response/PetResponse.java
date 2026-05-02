package com.petconsultant.response;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class PetResponse {

    private Long id;
    private Long userId;
    private String name;
    private String type;
    private String breed;
    private Integer age;
    private Double weight;
    private String gender;
    private String description;
    private String photo;
    private LocalDateTime createdAt;
}
