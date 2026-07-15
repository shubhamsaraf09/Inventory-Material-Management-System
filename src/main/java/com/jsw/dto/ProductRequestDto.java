package com.jsw.dto;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductRequestDto {

    @NotBlank(message = "Product Code  cannot be blank")
    @Size(min=2, max=20, message="Product code must be between 2 and 20 characters")
    private String productCode;

    @NotBlank(message = "Product name cannot be blank")
    @Size(max=100, message = "Product name cannot exceed 100 characters")
    private String productName;

    @NotNull(message = "Unit must be provided")
    @Min(value=1, message = "Unit must be at least 1")
    private Integer unit;

    @NotNull(message = "Minimun stock level must be provided")
    @Min(value=0,message = "Minimum stock cannot be negative")
    private Integer minStock;

    @NotBlank(message="Active flag cannot be blank")
    @Pattern(regexp = "^[YN]$",message = "Active flag must be exactly 'Y' or 'N'")
    private String activeFlag;


}
