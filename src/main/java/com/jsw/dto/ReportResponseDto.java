package com.jsw.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReportResponseDto {

    private String productName;

    private Long unit;

    private String departmentName;

    private Long availableQty;

    private Long requestNo;

    private String requestedBy;

    private String status;

    private String remarks;

    private Long requestedQty;

    private String issuedBy;

    private LocalDateTime issueDate;



}
