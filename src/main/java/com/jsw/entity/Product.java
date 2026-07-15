package com.jsw.entity;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDate;
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name="product_master")

public class Product {
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Column(name="product_id")
    private Long productMasterid;

    @Column(name="product_code", unique=true)
    private String productCode;

    @Column(name="product_name")
    private String productName;

    private Integer unit;

    @Column(name="min_stock")
    private Integer minStock;

    @Column(name="active_flag")
    private String activeFlag;

    @CreationTimestamp
    @Column(name="created_at")
    private LocalDate createdAt;

    @UpdateTimestamp
    @Column(name="updated_at")
    private LocalDate updatedAt;

}
