package com.jsw.controller;

import com.jsw.entity.*;
import com.jsw.repository.IssueTransactionRepository;
import com.jsw.serviceimpl.IssueTransactionServiceImpl;
import org.junit.jupiter.api.Test;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import com.jsw.entity.Product;
import com.jsw.security.JwtFilter;
import com.jsw.security.JwtUtil;
import com.jsw.serviceimpl.IssueRequestServiceImpl;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(IssueTransactionController.class)
@AutoConfigureMockMvc(addFilters = false)
public class IssueTransactionControllerTest {

    @MockitoBean
    private JwtFilter jwtFilter;

    @MockitoBean
    private JwtUtil jwtUtil;

    @MockitoBean
    private IssueTransactionServiceImpl issueTransactionServiceImpl;

    @MockitoBean
    private IssueTransactionRepository issueTransactionRepository;

    @Autowired
    private MockMvc mockMvc;

    private IssueTransaction issueTransaction;

    Product buildProd() {
        Product product = new Product();
        product.setProductMasterid(1L);
        product.setProductCode("P001");
        product.setProductName("Laptop");
        product.setUnit(20);
        product.setMinStock(5);
        product.setActiveFlag("Y");
        return product;
    }

    private IssueRequest buildIssueRequest() {
        Department department = new Department();
        department.setDepartmentId(1L);
        department.setDepartmentName("IT");
        department.setDepartmentCode("D001");
        department.setActiveFlag("Y");

        Product product = new Product();
        product.setProductMasterid(10L);
        product.setProductCode("P001");
        product.setProductName("Laptop");
        product.setUnit(20);
        product.setMinStock(5);
        product.setActiveFlag("Y");

        IssueRequest issueRequest = new IssueRequest();
        issueRequest.setIssueRequestid(1L);
        issueRequest.setRequestNo(1001L);
        issueRequest.setRequestDate(LocalDate.of(2026, 7, 4));
        issueRequest.setDepartment(department);
        issueRequest.setRequestedBy("Shubhankshi");
        issueRequest.setStatus("PENDING");
        issueRequest.setRemarks("Need for lab");
        issueRequest.setCreatedAt(LocalDate.of(2026, 7, 4));

        IssueRequestItem item = new IssueRequestItem();
        item.setIssueRequestItemid(1L);
        item.setIssueRequest(issueRequest);
        item.setProduct(product);
        item.setRequestedQty(2L);

        issueRequest.setItems(List.of(item));

        return issueRequest;
    }

    @Test
    void getAllIssueTransaction_ShouldReturnList() throws Exception{

        Product product=buildProd();
        IssueRequest issueRequest = buildIssueRequest();

        IssueTransaction issueTransaction = new IssueTransaction();
        issueTransaction.setIssuedBy("store_Manager");
        issueTransaction.setIssuedQty(20L);
        issueTransaction.setProduct(product);
        issueTransaction.setIssueTransactionid(1L);
        issueTransaction.setIssueRequest(issueRequest);

        when(issueTransactionServiceImpl.getAllTransactions())
                .thenReturn(List.of(issueTransaction));

        mockMvc.perform(get("/api/transactions"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].issuedBy").value("store_Manager"))
                .andExpect(jsonPath("$[0].issuedQty").value(20L))
                .andExpect(jsonPath("$[0].issueTransactionid").value(1))
                .andExpect(jsonPath("$[0].product.productName").value("Laptop"))
                .andExpect(jsonPath("$[0].product.productCode").value("P001"))
                .andExpect(jsonPath("$[0].issueRequest.requestNo").value(1001))
                .andExpect(jsonPath("$[0].issueRequest.requestedBy").value("Shubhankshi"));

    }

    @Test
    void getTransactionByDepartment_ShouldReturnList() throws Exception{
        IssueTransaction issueTransaction = new IssueTransaction();
        IssueRequest issueRequest = buildIssueRequest();
        Product product = buildProd();
        issueTransaction.setIssuedQty(20L);
        issueTransaction.setIssueRequest(issueRequest);
        issueTransaction.setIssueTransactionid(1L);
        issueTransaction.setIssuedBy("store_Manager");
        issueTransaction.setProduct(product);

        when(issueTransactionServiceImpl.getTransactionsByDepartment(1L))
                .thenReturn(List.of(issueTransaction));

        mockMvc.perform(get("/api/transactions/department/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].issuedBy").value("store_Manager"))
                .andExpect(jsonPath("$[0].issuedQty").value(20L))
                .andExpect(jsonPath("$[0].issueTransactionid").value(1))
                .andExpect(jsonPath("$[0].product.productName").value("Laptop"))
                .andExpect(jsonPath("$[0].product.productCode").value("P001"))
                .andExpect(jsonPath("$[0].issueRequest.requestNo").value(1001))
                .andExpect(jsonPath("$[0].issueRequest.requestedBy").value("Shubhankshi"));

    }

    @Test
    void getTransactionByRange_ShouldReturnListOfIssueTransactions() throws Exception  {

        IssueRequest issueRequest = buildIssueRequest();
        Product product = buildProd();
        IssueTransaction issueTransaction = new IssueTransaction();

        issueTransaction.setTransactionDate(LocalDateTime.of(2026,07,07,0,0));
        issueTransaction.setIssuedQty(20L);
        issueTransaction.setIssueRequest(issueRequest);
        issueTransaction.setIssueTransactionid(1L);
        issueTransaction.setIssuedBy("store_Manager");
        issueTransaction.setProduct(product);

        when(issueTransactionServiceImpl.getTransactionsByDateRange(LocalDate.of(2026,7,7),LocalDate.of(2026,7,8)))
        .thenReturn(List.of(issueTransaction));

        mockMvc.perform(get("/api/transactions/filters")
                .param("startDate", "2026-07-07")
                .param("endDate", "2026-07-08"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].issuedBy").value("store_Manager"))
                .andExpect(jsonPath("$[0].issuedQty").value(20L))
                .andExpect(jsonPath("$[0].issueTransactionid").value(1))
                .andExpect(jsonPath("$[0].product.productName").value("Laptop"))
                .andExpect(jsonPath("$[0].product.productCode").value("P001"))
                .andExpect(jsonPath("$[0].issueRequest.requestNo").value(1001))
                .andExpect(jsonPath("$[0].issueRequest.requestedBy").value("Shubhankshi"));


    }

}
