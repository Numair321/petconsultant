package com.petconsultant.request;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import java.time.LocalDate;

@Data
public class AppointmentRequest {

    @NotNull(message = "Vet ID is required")
    private Long vetId;

    private Long petId;

    @NotNull(message = "Appointment date is required")
    private LocalDate appointmentDate;

    private String appointmentTime;
    private com.petconsultant.enumerator.AppointmentType type;
    private String notes;
}
