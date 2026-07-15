package com.jsw.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.data.annotation.CreatedDate;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name="department_master")
public class Department {

    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Column(name="department_id")
    private Long departmentId;

    @Column(name="department_code")
    private String departmentCode;

    @Column(name="department_name")
    private String departmentName;

    @Column(name="active_flag")
    private String activeFlag;

    @CreationTimestamp
    @Column(name="created_at")
    private LocalDate createdAt;

    @UpdateTimestamp
    @Column(name="updated_at")
    private LocalDate updatedAt;

}
