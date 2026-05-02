package com.petconsultant.request;

import com.petconsultant.enumerator.PetType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class PetRequest {

    @NotBlank(message = "Pet name is required")
    private String name;

    @NotNull(message = "Pet type is required")
    private PetType type;

    private String breed;
    private Integer age;
    private Double weight;
    private String gender;
    private String description;
    private String photo;
}
