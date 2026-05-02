package com.petconsultant.response;

import lombok.Data;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
public class AppointmentResponse {

    private Long id;
    private Long userId;
    private Long vetId;
    private Long petId;
    private String vetName;
    private String petName;
    private LocalDate appointmentDate;
    private String appointmentTime;
    private String status;
    private String type;
    private String notes;
    private LocalDateTime createdAt;
}
