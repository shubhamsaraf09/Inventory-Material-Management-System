package com.jsw.service;

import com.jsw.dto.ProductRequestDto;
import com.jsw.entity.Product;

import java.util.List;

public interface ProductService {

    Product saveProduct(ProductRequestDto dto);
    List<Product> getAllProduct();
    Product getProductById(Long id);
    Product updateProduct(Long id, ProductRequestDto dto);
    void deleteById(Long id);
}
