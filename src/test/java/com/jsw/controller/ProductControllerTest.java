package com.jsw.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jsw.dto.ProductRequestDto;
import com.jsw.entity.Product;
import com.jsw.security.JwtFilter;
import com.jsw.security.JwtUtil;
import com.jsw.service.ProductService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ProductController.class)
@AutoConfigureMockMvc(addFilters = false)
class ProductControllerTest {

    @Autowired
    private MockMvc mockMvc;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @MockitoBean
    private ProductService productService;

    @MockitoBean
    private JwtFilter jwtFilter;

    @MockitoBean
    private JwtUtil jwtUtil;

    @Test
    void saveProduct_ShouldReturnCreated_WhenValidRequest() throws Exception {
        ProductRequestDto dto = new ProductRequestDto("P001", "Laptop", 20, 5, "Y");

        Product product = new Product();
        product.setProductMasterid(1L);
        product.setProductCode("P001");
        product.setProductName("Laptop");
        product.setUnit(20);
        product.setMinStock(5);
        product.setActiveFlag("Y");

        when(productService.saveProduct(any(ProductRequestDto.class))).thenReturn(product);

        mockMvc.perform(post("/api/products/save")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.productMasterid").value(1L))
                .andExpect(jsonPath("$.productCode").value("P001"))
                .andExpect(jsonPath("$.productName").value("Laptop"))
                .andExpect(jsonPath("$.unit").value(20))
                .andExpect(jsonPath("$.minStock").value(5))
                .andExpect(jsonPath("$.activeFlag").value("Y"));
    }

    @Test
    void getAllProducts_ShouldReturnList() throws Exception {
        Product product = new Product();
        product.setProductMasterid(1L);
        product.setProductCode("P001");
        product.setProductName("Laptop");
        product.setUnit(20);
        product.setMinStock(5);
        product.setActiveFlag("Y");

        when(productService.getAllProduct()).thenReturn(List.of(product));

        mockMvc.perform(get("/api/products/findAll"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].productMasterid").value(1L))
                .andExpect(jsonPath("$[0].productCode").value("P001"))
                .andExpect(jsonPath("$[0].productName").value("Laptop"));
    }

    @Test
    void getProductById_ShouldReturnProduct_WhenFound() throws Exception{
        Product product = new Product();
        product.setProductMasterid(1L);
        product.setProductCode("P001");
        product.setProductName("Laptop");
        product.setUnit(20);
        product.setMinStock(5);
        product.setActiveFlag("Y");

        when(productService.getProductById(1L)).thenReturn(product);

        mockMvc.perform(get("/api/products/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.productMasterid").value(1L))
                .andExpect(jsonPath("$.productCode").value("P001"))
                .andExpect(jsonPath("$.activeFlag").value("Y"))
                .andExpect(jsonPath("$.productName").value("Laptop"))
                .andExpect(jsonPath("$.minStock").value(5))
                .andExpect(jsonPath("$.unit").value(20));
    }

    @Test
    void updateProduct_ShouldReturnUpdatedProduct_WhenValidRequest() throws Exception{
        ProductRequestDto dto = new ProductRequestDto(
                "P002",
                "Desktop",
                30,
                10,
                "Y"
        );

        Product product = new Product();
        product.setProductCode("P002");
        product.setProductName("Desktop");
        product.setUnit(30);
        product.setActiveFlag("Y");
        product.setMinStock(10);
        product.setProductMasterid(1L);

        when(productService.updateProduct(eq(1L),any(ProductRequestDto.class))).thenReturn(product);

        mockMvc.perform(put("/api/products/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.productName").value("Desktop"))
                .andExpect(jsonPath("$.productCode").value("P002"))
                .andExpect(jsonPath("$.unit").value(30))
                .andExpect(jsonPath("$.activeFlag").value("Y"))
                .andExpect(jsonPath("$.productMasterid").value(1L))
                .andExpect(jsonPath("$.minStock").value(10));

    }

    @Test
    void deleteProductById_ShouldREturnNoContent() throws Exception{
        doNothing().when(productService).deleteById(1L);

        mockMvc.perform(delete("/api/products/1"))
                .andExpect(status().isNoContent());
    }

}