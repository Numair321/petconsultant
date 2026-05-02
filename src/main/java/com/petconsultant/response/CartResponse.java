package com.petconsultant.response;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class CartResponse {

    private Long id;
    private Long productId;
    private String productName;
    private String productImage;
    private Double productPrice;
    private Integer quantity;
    private Double totalPrice;
    private LocalDateTime updatedAt;
}
