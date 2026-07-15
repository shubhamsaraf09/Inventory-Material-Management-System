package com.jsw.dto;


import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;
@Data
@AllArgsConstructor
@NoArgsConstructor
@Valid
public class IssueRequestDto {

    @NotNull(message = "Department ID cannot be null")
    private Long departmentId;

    @NotBlank(message = "Requested By name cannot be blank")
    private String requestedBy;

    private String remarks;

    @NotNull(message="Items list cannot be empty")
    private List<IssueRequestItemDto> items;
}
