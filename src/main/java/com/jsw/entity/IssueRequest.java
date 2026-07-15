package com.jsw.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDate;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor

@Entity
@Table(name="issue_request")
public class IssueRequest {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="issuerequest_id")
    private Long issueRequestid;

    @Column(name="request_no")
    private Long requestNo;

    @Column(name="request_date")
    private LocalDate requestDate;

    @ManyToOne
    @JoinColumn(name="dept_id",
                referencedColumnName = "department_id",
                nullable = false)
    private Department department;

    @Column(name="requested_by")
    private String requestedBy;

    private String status;

    private String remarks;

    @CreationTimestamp
    @Column(name="created_at")
    private LocalDate createdAt;

    @OneToMany(mappedBy = "issueRequest", cascade=CascadeType.ALL)
    @JsonManagedReference
    private List<IssueRequestItem> items;
}

