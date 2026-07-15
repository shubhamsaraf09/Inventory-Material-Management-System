package com.jsw.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "issue_request_item")
public class IssueRequestItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="issuerequestitem_id")
    private Long issueRequestItemid;

    @ManyToOne
    @JoinColumn(name="request_Id",
                referencedColumnName = "issuerequest_id",
                nullable=false)
    @JsonBackReference
    private IssueRequest issueRequest;

    @ManyToOne
    @JoinColumn(name="prod_id",
            referencedColumnName = "product_id",
            nullable=false)
    private Product product;

    @Column(name="requested_qty")
    private Long requestedQty;
}
