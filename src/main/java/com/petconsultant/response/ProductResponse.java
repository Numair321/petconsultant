package com.petconsultant.response;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class ProductResponse {

    private Long id;
    private Long categoryId;
    private String name;
    private String description;
    private Double price;
    private Integer stock;
    private String image;
    private Boolean isActive;
    private LocalDateTime createdAt;
}
