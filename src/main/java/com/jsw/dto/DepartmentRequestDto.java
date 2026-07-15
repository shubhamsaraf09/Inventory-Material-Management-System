package com.jsw.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor

public class DepartmentRequestDto {

    @NotBlank(message = "Department code cannot be blank")
    @Size(min=2,max=10,message = "Department code must be between 2 to 10 characters")
    private String departmentCode;

    @NotBlank(message = "Department name cannot be blank")
    @Size(max=100, message = "Department name cannot exceed 100 characters")
    private String departmentName;

    @NotBlank(message = "Active flag cannot be blank")
    @Pattern(regexp = "^[YN]$",message = "Active flag must be either 'Y' or 'N'")
    private String activeFlag;
}
