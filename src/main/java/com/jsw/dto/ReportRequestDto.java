package com.jsw.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReportRequestDto {
    private LocalDateTime startDate;

    private LocalDateTime endDate;

    private String status;

    private String departmentName;
}
