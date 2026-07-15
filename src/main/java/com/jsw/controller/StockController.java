package com.jsw.controller;


import com.jsw.dto.StockRequestDto;
import com.jsw.entity.Stock;
import com.jsw.serviceimpl.StockServiceImpl;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import org.apache.coyote.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.List;

@RestController
@RequestMapping("/api/stocks")
@Validated
public class StockController {

    @Autowired
    private StockServiceImpl stockServiceImpl;

    @PreAuthorize("hasAnyRole('USER', 'MANAGER', 'ADMIN')")
    @GetMapping
    public ResponseEntity<List<Stock>> getAllstock(){
        return ResponseEntity.ok(stockServiceImpl.getAllStock());
    }

    @PreAuthorize("hasAnyRole('MANAGER', 'ADMIN')")
    @PutMapping("/add")
    public ResponseEntity<Stock> addStock(@Valid @RequestBody StockRequestDto dto){
        return ResponseEntity.ok(stockServiceImpl.addStock(dto));
    }

    @PreAuthorize("hasAnyRole('USER', 'MANAGER', 'ADMIN')")
    @GetMapping("/{id:\\d+}")
    public ResponseEntity<Stock> getStockById(@PathVariable Long id){
        return ResponseEntity.ok(stockServiceImpl.getStockByProductId(id));
    }

    @PreAuthorize("hasAnyRole(' MANAGER', 'ADMIN')")
    @PutMapping("/deduct")
    public ResponseEntity<Stock> deductStock(@Valid @RequestBody StockRequestDto dto){
        return ResponseEntity.ok(stockServiceImpl.deductStock(dto.getProductId(),dto.getQuantity()));
    }

    @PreAuthorize("hasAnyRole( 'MANAGER', 'ADMIN')")
    @PutMapping("/low-stock")
    public ResponseEntity<List<Stock>> getLowStrckReport(
            @RequestParam
            @NotNull(message = "Threshold is required")
            @Min(value=0, message = "Threshold cannot be negative")
            Long threshold
    ){
        return ResponseEntity.ok(stockServiceImpl.getLowStockReport(threshold));
    }
}
