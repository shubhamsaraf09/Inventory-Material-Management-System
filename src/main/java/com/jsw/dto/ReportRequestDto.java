package com.jsw.dto;

import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReportRequestDto {
    @PastOrPresent(message = "Start date cannot be in the future")
    private LocalDateTime startDate;

    private LocalDateTime endDate;

    private String status;

    @Size(max = 100, message = "Department name cannot exceed 100 characters")
    private String departmentName;

    @AssertTrue(message = "End date must be after or equal to the start date")
    public boolean isDateRangeValid() {
        if (startDate == null || endDate == null) {
            return true;
        }
        return !startDate.isAfter(endDate);
    }

}
