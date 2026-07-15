package com.jsw.serviceimpl;
import com.jsw.dto.StockRequestDto;
import com.jsw.entity.Product;
import com.jsw.entity.Stock;
import com.jsw.exception.BusinessException;
import com.jsw.exception.ResourceNotFoundException;
import com.jsw.repository.ProductRepository;
import com.jsw.repository.StockRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class StockServiceImplTest {
    @Mock
    private StockRepository stockRepository;

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private StockServiceImpl stockService;

    private Product product;
    private Stock stock;
    private StockRequestDto dto;

    @BeforeEach
    void setUp() {
        product = new Product();
        product.setProductMasterid(1L);
        product.setProductCode("P001");
        product.setProductName("Laptop");
        product.setUnit(20);
        product.setMinStock(5);
        product.setActiveFlag("Y");

        stock = new Stock();
        stock.setProduct(product);
        stock.setAvailableQty(50L);

        dto = new StockRequestDto();
        dto.setProductId(1L);
        dto.setQuantity(10L);
    }

    @Test
    void getAllStock_ShouldReturnAllStock() {
        when(stockRepository.findAll()).thenReturn(List.of(stock));

        List<Stock> result = stockService.getAllStock();

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getAvailableQty()).isEqualTo(50L);
        verify(stockRepository,times(1)).findAll();
    }

    @Test
    void getStockByProductId_ShouldReturnStock_WhenFound() {
        when(stockRepository.findStockByProduct_ProductMasterid(1L))
                .thenReturn(Optional.of(stock));

        Stock result = stockService.getStockByProductId(1L);

        assertThat(result).isNotNull();
        assertThat(result.getAvailableQty()).isEqualTo(50L);
        assertThat(result.getProduct().getProductCode()).isEqualTo("P001");
        verify(stockRepository).findStockByProduct_ProductMasterid(1L);
    }

    @Test
    void getStockByProductId_ShouldThrow_WhenNotFound() {
        when(stockRepository.findStockByProduct_ProductMasterid(1L))
                .thenReturn(Optional.empty());

        ResourceNotFoundException ex = assertThrows(
                ResourceNotFoundException.class,
                () -> stockService.getStockByProductId(1L)
        );

        assertThat(ex.getMessage())
                .isEqualTo("Stock for this product having id 1does not exist");
        verify(stockRepository).findStockByProduct_ProductMasterid(1L);
    }

    @Test
    void addStock_ShouldIncreaseQuantity_WhenStockExists() {
        when(stockRepository.findStockByProduct_ProductMasterid(1L))
                .thenReturn(Optional.of(stock));
        when(stockRepository.save(any(Stock.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        Stock result = stockService.addStock(dto);

        assertThat(result.getAvailableQty()).isEqualTo(60L);

        ArgumentCaptor<Stock> captor = ArgumentCaptor.forClass(Stock.class);
        verify(stockRepository).save(captor.capture());
        assertThat(captor.getValue().getAvailableQty()).isEqualTo(60L);
        verify(productRepository, never()).findById(any());
    }

    @Test
    void addStock_ShouldCreateNewStock_WhenNoneExists() {
        when(stockRepository.findStockByProduct_ProductMasterid(1L))
                .thenReturn(Optional.empty());
        when(productRepository.findById(1L))
                .thenReturn(Optional.of(product));
        when(stockRepository.save(any(Stock.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        Stock result = stockService.addStock(dto);

        assertThat(result.getAvailableQty()).isEqualTo(10L);
        assertThat(result.getProduct().getProductCode()).isEqualTo("P001");
        verify(productRepository).findById(1L);
        verify(stockRepository).save(any(Stock.class));
    }

    @Test
    void addStock_ShouldThrow_WhenProductDoesNotExist() {
        when(stockRepository.findStockByProduct_ProductMasterid(1L))
                .thenReturn(Optional.empty());
        when(productRepository.findById(1L))
                .thenReturn(Optional.empty());

        ResourceNotFoundException ex = assertThrows(
                ResourceNotFoundException.class,
                () -> stockService.addStock(dto)
        );

        assertThat(ex.getMessage()).isEqualTo("Product not exist in inventory");
        verify(stockRepository).findStockByProduct_ProductMasterid(1L);
        verify(productRepository).findById(1L);
        verify(stockRepository, never()).save(any(Stock.class));
    }

    @Test
    void deductStock_ShouldReduceQuantity_WhenEnoughStock() {
        when(stockRepository.findStockByProduct_ProductMasterid(1L))
                .thenReturn(Optional.of(stock));
        when(stockRepository.save(any(Stock.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        Stock result = stockService.deductStock(1L, 10L);

        assertThat(result.getAvailableQty()).isEqualTo(40L);
        verify(stockRepository).save(any(Stock.class));
    }

    @Test
    void deductStock_ShouldThrowBusinessException_WhenInsufficientStock() {
        when(stockRepository.findStockByProduct_ProductMasterid(1L))
                .thenReturn(Optional.of(stock));

        BusinessException ex = assertThrows(
                BusinessException.class,
                () -> stockService.deductStock(1L, 100L)
        );

        assertThat(ex.getMessage()).isEqualTo("Insufficient stock available");
        verify(stockRepository, never()).save(any(Stock.class));
    }

    @Test
    void deductStock_ShouldThrow_WhenStockNotFound() {
        when(stockRepository.findStockByProduct_ProductMasterid(1L))
                .thenReturn(Optional.empty());

        ResourceNotFoundException ex = assertThrows(
                ResourceNotFoundException.class,
                () -> stockService.deductStock(1L, 10L)
        );

        assertThat(ex.getMessage())
                .isEqualTo("Stock for this product with productid 1does not exist");
        verify(stockRepository, never()).save(any(Stock.class));
    }

    @Test
    void getLowStockReport_ShouldReturnList_WhenThresholdIsValid() {
        when(stockRepository.findByAvailableQtyLessThanEqual(5L))
                .thenReturn(List.of(stock));

        List<Stock> result = stockService.getLowStockReport(5L);

        assertThat(result).hasSize(1);
        verify(stockRepository).findByAvailableQtyLessThanEqual(5L);
    }

    @Test
    void getLowStockReport_ShouldThrowBusinessException_WhenThresholdNegative() {
        BusinessException ex = assertThrows(
                BusinessException.class,
                () -> stockService.getLowStockReport(-1L)
        );

        assertThat(ex.getMessage()).isEqualTo("Threshold cannot be a negative number.");
        verify(stockRepository, never()).findByAvailableQtyLessThanEqual(any());
    }

}