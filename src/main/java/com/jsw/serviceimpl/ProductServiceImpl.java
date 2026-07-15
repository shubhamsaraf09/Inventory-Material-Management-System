package com.jsw.serviceimpl;

import com.jsw.dto.ProductRequestDto;
import com.jsw.entity.Product;
import com.jsw.exception.ResourceNotFoundException;
import com.jsw.repository.ProductRepository;
import com.jsw.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductServiceImpl implements ProductService {

    @Autowired
    private ProductRepository productRepository;
    @Override
    public Product saveProduct(ProductRequestDto dto){
        Product product= new Product();
        product.setProductCode(dto.getProductCode());
        product.setProductName(dto.getProductName());
        product.setUnit(dto.getUnit());
        product.setMinStock(dto.getMinStock());
        product.setActiveFlag("Y");


        return productRepository.save(product);
    }

    @Override
    public List<Product> getAllProduct(){
        return productRepository.findAll();

    }

    @Override
    public Product getProductById(Long id){
        return productRepository.findById(id).orElseThrow(()-> new ResourceNotFoundException("Product not found with this id"));
    }

    @Override
    public Product updateProduct(Long id, ProductRequestDto dto){
        Product product=productRepository.findById(id).orElseThrow(()->new ResourceNotFoundException("Product not found"));
        product.setProductCode(dto.getProductCode());
        product.setProductName(dto.getProductName());
        product.setUnit(dto.getUnit());
        product.setMinStock(dto.getMinStock());
        product.setActiveFlag("Y");
        return productRepository.save(product);
    }

    @Override
    public void deleteById(Long id){
        productRepository.deleteById(id);
    }

}
