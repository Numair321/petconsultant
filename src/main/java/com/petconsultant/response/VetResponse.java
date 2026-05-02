package com.petconsultant.response;

import lombok.Data;

@Data
public class VetResponse {

    private Long id;
    private String fullName;
    private String email;
    private String phone;
    private String specialization;
    private Integer experienceYears;
    private Double rating;
    private String description;
    private String photo;
    private Double consultationFee;
    private String availableSlots;
    private Boolean isAvailable;
}
