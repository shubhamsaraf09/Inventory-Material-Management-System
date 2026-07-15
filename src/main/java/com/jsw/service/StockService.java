package com.jsw.service;

import com.jsw.dto.StockRequestDto;
import com.jsw.entity.Stock;

import java.util.List;

public interface StockService {
    List<Stock> getAllStock();
    Stock getStockByProductId(Long id);
    Stock addStock(StockRequestDto sto);
    Stock deductStock(Long productId, Long quantity);
    List<Stock> getLowStockReport(Long threshold);
}
