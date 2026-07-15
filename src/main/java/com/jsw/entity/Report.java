package com.jsw.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;


@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor

@Table(name="Report")
public class Report {

    @Column(name = "product_name")
    private String productName;

    @Column(name = "unit")
    private Long unit;

    @Column(name = "department_name")
    private String departmentName;

    @Column(name = "available_qty")
    private Long availableQty;

    @Id
    @Column(name = "report_id")
    private Long reportId;

    @Column(name = "request_no")
    private Long requestNo;

    @Column(name = "requested_by")
    private String requestedBy;

    @Column(name = "status")
    private String status;

    @Column(name = "remarks")
    private String remarks;

    @Column(name = "requested_qty")
    private Long requestedQty;

    @Column(name = "issued_by")
    private String issuedBy;

    @Column(name = "issue_date")
    private LocalDateTime issueDate;

}
