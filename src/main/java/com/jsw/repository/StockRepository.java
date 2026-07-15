package com.jsw.repository;

import com.jsw.entity.Stock;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface StockRepository extends JpaRepository<Stock,Long> {
    Optional<Stock> findStockByProduct_ProductMasterid(Long id);
    List<Stock> findByAvailableQtyLessThanEqual(Long threshold);
}
