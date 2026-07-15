package com.jsw.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "issue_transaction")
public class IssueTransaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "issuetransaction_id")
    private Long issueTransactionid;

    @ManyToOne
    @JoinColumn(name = "request_Id",
            referencedColumnName = "issuerequest_id",
            nullable = false)
    private IssueRequest issueRequest;

    @ManyToOne
    @JoinColumn(name="prod_id",
            referencedColumnName = "product_id",
            nullable=false)
    private Product product;

    @Column(name="issued_qty")
    private Long issuedQty;

    @CreationTimestamp
    @Column(name="issue_date")
    private LocalDateTime transactionDate;

    @Column(name="issued_by")
    private String issuedBy;

}
