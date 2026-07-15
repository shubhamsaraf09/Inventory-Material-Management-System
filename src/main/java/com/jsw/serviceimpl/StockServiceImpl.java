package com.jsw.serviceimpl;

import com.jsw.dto.StockRequestDto;
import com.jsw.entity.Product;
import com.jsw.entity.Stock;
import com.jsw.exception.BusinessException;
import com.jsw.exception.ResourceNotFoundException;
import com.jsw.repository.ProductRepository;
import com.jsw.repository.StockRepository;
import com.jsw.service.StockService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class StockServiceImpl implements StockService {

    @Autowired
    private StockRepository stockRepository;

    @Autowired
    private ProductRepository productrepository;

    @Override
    public List<Stock> getAllStock(){
        return stockRepository.findAll();
    }

    @Override
    public Stock getStockByProductId(Long id){
        return stockRepository.findStockByProduct_ProductMasterid(id).orElseThrow(()-> new ResourceNotFoundException("Stock for this product having id "+id+ "does not exist"));
    }

    @Override
    public Stock addStock(StockRequestDto dto){
        Optional<Stock> existingstock=stockRepository.findStockByProduct_ProductMasterid(dto.getProductId());
        if(existingstock.isPresent()) {
            Stock stock=existingstock.get();
            stock.setAvailableQty(stock.getAvailableQty() + dto.getQuantity());
            return stockRepository.save(stock);
        }
        else {
            Product product= productrepository.findById(dto.getProductId()).orElseThrow(()->new ResourceNotFoundException("Product not exist in inventory"));
            Stock stock=new Stock();
            stock.setProduct(product);
            stock.setAvailableQty(dto.getQuantity());
            return stockRepository.save(stock);
        }
    }

    @Override
    public Stock deductStock(Long productId, Long quantity){
        Stock stock=stockRepository.findStockByProduct_ProductMasterid(productId).orElseThrow(()->new ResourceNotFoundException("Stock for this product with productid "+productId+"does not exist"));
        if (stock.getAvailableQty()>=quantity) {
            stock.setAvailableQty(stock.getAvailableQty() - quantity);
        }
        else throw new BusinessException("Insufficient stock available");
        return stockRepository.save(stock);
    }

    @Override
    public List<Stock> getLowStockReport(Long threshold){

        log.info("Generating low stock report for threshold level: {}", threshold);

        if(threshold<0){
            log.error("Failed to generate report: Provided threshold {} is negative",threshold);
            throw new BusinessException("Threshold cannot be a negative number.");
        }
        List<Stock> lowStockList = stockRepository.findByAvailableQtyLessThanEqual(threshold);
        log.info("Low stock report generated successfully. Found {} items",lowStockList.size());
        return lowStockList;
    }
}
