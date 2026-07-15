package com.jsw.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class StockRequestDto {

    @NotNull(message = "Product Id cannot be null")
    @Min(value=1,message="Product ID must be at least 1")
    private Long productId;

    @NotNull(message="Quantity must be provided")
    @Min(value=1, message = "Quantity must be at least 1")
    private Long quantity;

}
