package com.jsw.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "stock_master")
public class Stock {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="stock_id")
    private Long stockId;

    @OneToOne
    @JsonIgnore
    @ToString.Exclude
    @JoinColumn(name="prod_id",
                referencedColumnName = "product_id",
            nullable=false)
    private Product product;

    @Column(name="available_qty")
    private Long availableQty;

    @UpdateTimestamp
    @Column(name="last_updated")
    private LocalDateTime lastUpdated;
}
