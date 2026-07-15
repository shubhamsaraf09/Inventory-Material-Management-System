package com.jsw.dto;

import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor

public class IssueRequestItemDto {
    private Long productId;

    @Min(value=1,message = "Quantity must be at least 1")
    private Long requestedQty;
}
