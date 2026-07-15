package com.jsw.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor

public class IssueTransactionResponseDto {

    private Long transactionId;

    private Long requestNo;

    private String departmentName;

    private String productName;

    private Long issuedQty;

    private LocalDateTime transactionDate;

}
