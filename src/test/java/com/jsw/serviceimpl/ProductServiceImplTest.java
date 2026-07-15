package com.jsw.serviceimpl;

import com.jsw.dto.ProductRequestDto;
import com.jsw.entity.Product;
import com.jsw.exception.ResourceNotFoundException;
import com.jsw.repository.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ProductServiceImplTest {

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private ProductServiceImpl productService;

    private Product product;
    private ProductRequestDto dto;

    @BeforeEach
    void setUp() {
        product=new Product();
        product.setProductCode("P001");
        product.setProductName("Laptop");
        product.setActiveFlag("Y");
        product.setMinStock(5);
        product.setUnit(20);

        dto = new ProductRequestDto();
        dto.setMinStock(5);
        dto.setProductCode("P001");
        dto.setProductName("Laptop");
        dto.setUnit(20);

    }

    @Test
    void getProductById_ShouldReturnProduct_IfProductExists(){

        when(productRepository.findById(1L))
                .thenReturn(Optional.of(product));

        Product result = productService.getProductById(1L);

        assertThat(result).isNotNull();
        assertThat(result.getProductName()).isEqualTo("Laptop");
        assertThat(result.getProductCode()).isEqualTo("P001");
        assertThat(result.getMinStock()).isEqualTo(5);
        assertThat(result.getUnit()).isEqualTo(20);
        verify(productRepository,times(1)).findById(1L);
    }

    @Test
    void getProductById_ShouldReturnException_IFProductNotFound() {
        when(productRepository.findById(1L))
                .thenReturn(Optional.empty());
        ResourceNotFoundException exception = assertThrows(
                ResourceNotFoundException.class,
                ()-> productService.getProductById(1L)
        );

        assertThat(exception.getMessage()).isEqualTo("Product not found with this id");
        verify(productRepository,times(1)).findById(1L);

    }

    @Test
    void getAllProduct_ShouldReturnList_IfProductExists() {
        when(productRepository.findAll())
                .thenReturn(List.of(product));

        List<Product> result = productService.getAllProduct();
        assertThat(result.size()).isEqualTo(1);
        assertThat(result.get(0).getProductName()).isEqualTo("Laptop");
        assertThat(result.get(0).getProductCode()).isEqualTo("P001");
        assertThat(result.get(0).getMinStock()).isEqualTo(5);
        assertThat(result.get(0).getUnit()).isEqualTo(20);

        verify(productRepository,times(1)).findAll();
    }

    @Test
    void getAllProduct_shouldThrowException_IfProductDoesntExist(){
        when(productRepository.findAll())
                .thenReturn(Collections.emptyList());

        List<Product> result = productService.getAllProduct();

        assertThat(result).isNotNull();
        assertThat(result).isEmpty();

        verify(productRepository,times(1)).findAll();

    }

    @Test
    void saveProduct_ShouldReturnProduct_WhenDtoIsValid(){
        when(productRepository.save(any(Product.class)))
                .thenReturn(product);

        Product result= productService.saveProduct(dto);

        assertThat(result).isNotNull();
        assertThat(result.getProductName()).isEqualTo("Laptop");
        assertThat(result.getProductCode()).isEqualTo("P001");
        assertThat(result.getMinStock()).isEqualTo(5);
        assertThat(result.getUnit()).isEqualTo(20);
        verify(productRepository,times(1)).save(any(Product.class));

    }

    @Test
    void saveProduct_ActiveFlagShouldBeY_RegardlessOfDto(){
        when(productRepository.save(any(Product.class)))
                .thenReturn(product);

        productService.saveProduct(dto);

        ArgumentCaptor<Product> captor= ArgumentCaptor.forClass(Product.class);

        verify(productRepository).save(captor.capture());

        Product capture = captor.getValue();

        assertThat(capture.getActiveFlag()).isEqualTo("Y");
        assertThat(capture.getProductCode()).isEqualTo("P001");
        assertThat(capture.getProductName()).isEqualTo("Laptop");

        verify(productRepository,times(1)).save(capture);
    }

    @Test
    void updateProduct_ShouldUpdateAndReturn_WhenIdExists() {
        ProductRequestDto dto = new ProductRequestDto();
        dto.setProductCode("P002");
        dto.setProductName("Desktop");
        dto.setMinStock(10);
        dto.setUnit(30);

        when(productRepository.findById(1L)).thenReturn(Optional.of(product));
        when(productRepository.save(any(Product.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Product result = productService.updateProduct(1L, dto);

        assertThat(result.getProductCode()).isEqualTo("P002");
        assertThat(result.getProductName()).isEqualTo("Desktop");
        assertThat(result.getMinStock()).isEqualTo(10);
        assertThat(result.getUnit()).isEqualTo(30);
        assertThat(result.getActiveFlag()).isEqualTo("Y");

        verify(productRepository).findById(1L);
        verify(productRepository).save(any(Product.class));
    }

    @Test
    void updateProduct_ThrowException_ifIdDoesntExist(){
        when(productRepository.findById(99L))
                .thenReturn(Optional.empty());

        ResourceNotFoundException exception = assertThrows(
                ResourceNotFoundException.class,
                () -> productService.updateProduct(99L,dto)
        );

        assertThat(exception.getMessage())
                .isEqualTo("Product not found");

        verify(productRepository,never()).save(any(Product.class));
    }

    @Test
    void deleteProduct_CallDeleteById_WhenIdExists(){
        doNothing().when(productRepository).deleteById(1L);

        productService.deleteById(1L);

        verify(productRepository,times(1)).deleteById(1L);
    }

    @Test
    void deleteProduct_ThowsException_WhenIdDoesntExist() {
        doThrow(new RuntimeException("No DB")).when(productRepository).deleteById(1L);

        RuntimeException exception = assertThrows(
                RuntimeException.class,
                ()-> productService.deleteById(1L)
        );

        assertThat(exception.getMessage()).isEqualTo("No DB");
        verify(productRepository,times(1)).deleteById(1L);
    }
}
