package com.jsw.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jsw.dto.IssueRequestDto;
import com.jsw.dto.IssueRequestItemDto;
import com.jsw.entity.Department;
import com.jsw.entity.IssueRequest;
import com.jsw.entity.IssueRequestItem;
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

import java.time.LocalDate;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(IssueRequestController.class)
@AutoConfigureMockMvc(addFilters = false)
public class IssueRequestControllerTest {

    @Autowired
    private MockMvc mockMvc;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @MockitoBean
    private IssueRequestServiceImpl issueRequestServiceImpl;

    @MockitoBean
    private JwtFilter jwtFilter;

    @MockitoBean
    private JwtUtil jwtUtil;

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
    void createIssueRequest_ShouldReturnCreated_WhenValidRequest() throws Exception {
        IssueRequestItemDto itemDto = new IssueRequestItemDto(10L, 2L);
        IssueRequestDto dto = new IssueRequestDto(
                1L,
                "Shubhankshi",
                "Need for lab",
                List.of(itemDto)
        );

        IssueRequest savedRequest = buildIssueRequest();

        when(issueRequestServiceImpl.createIssueRequest(any(IssueRequestDto.class)))
                .thenReturn(savedRequest);

        mockMvc.perform(post("/api/issuerequests")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.issueRequestid").value(1L))
                .andExpect(jsonPath("$.requestNo").value(1001L))
                .andExpect(jsonPath("$.requestedBy").value("Shubhankshi"))
                .andExpect(jsonPath("$.status").value("PENDING"))
                .andExpect(jsonPath("$.remarks").value("Need for lab"))
                .andExpect(jsonPath("$.items[0].issueRequestItemid").value(1L))
                .andExpect(jsonPath("$.items[0].requestedQty").value(2L));
    }

    @Test
    void createIssueRequest_ShouldReturnBadRequest_WhenDepartmentIdIsNull() throws Exception {
        IssueRequestItemDto itemDto = new IssueRequestItemDto(10L, 2L);
        IssueRequestDto dto = new IssueRequestDto(
                null,
                "Shubhankshi",
                "Need for lab",
                List.of(itemDto)
        );

        mockMvc.perform(post("/api/issuerequests")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void createIssueRequest_ShouldReturnBadRequest_WhenRequestedByIsBlank() throws Exception {
        IssueRequestItemDto itemDto = new IssueRequestItemDto(10L, 2L);
        IssueRequestDto dto = new IssueRequestDto(
                1L,
                "",
                "Need for lab",
                List.of(itemDto)
        );

        mockMvc.perform(post("/api/issuerequests")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void getAllIssueRequests_ShouldReturnList() throws Exception {
        IssueRequest issueRequest = buildIssueRequest();

        when(issueRequestServiceImpl.findAllIssueRequest())
                .thenReturn(List.of(issueRequest));

        mockMvc.perform(get("/api/issuerequests"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].issueRequestid").value(1L))
                .andExpect(jsonPath("$[0].requestNo").value(1001L))
                .andExpect(jsonPath("$[0].requestedBy").value("Shubhankshi"))
                .andExpect(jsonPath("$[0].status").value("PENDING"))
                .andExpect(jsonPath("$[0].items[0].requestedQty").value(2L));
    }

    @Test
    void getPendingIssueRequests_ShouldReturnPendingList() throws Exception {
        IssueRequest issueRequest = buildIssueRequest();

        when(issueRequestServiceImpl.getPendingIssueRequests())
                .thenReturn(List.of(issueRequest));

        mockMvc.perform(get("/api/issuerequests/pending"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].issueRequestid").value(1L))
                .andExpect(jsonPath("$[0].status").value("PENDING"));
    }

    @Test
    void getIssueRequestById_ShouldReturnIssueRequest_WhenFound() throws Exception {
        IssueRequest issueRequest = buildIssueRequest();

        when(issueRequestServiceImpl.getIssueRequestById(1L))
                .thenReturn(issueRequest);

        mockMvc.perform(get("/api/issuerequests/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.issueRequestid").value(1L))
                .andExpect(jsonPath("$.requestNo").value(1001L))
                .andExpect(jsonPath("$.requestedBy").value("Shubhankshi"))
                .andExpect(jsonPath("$.status").value("PENDING"))
                .andExpect(jsonPath("$.items[0].requestedQty").value(2L));
    }

    @Test
    void approveIssueRequest_ShouldReturnApprovedIssueRequest() throws Exception {
        IssueRequest issueRequest = buildIssueRequest();
        issueRequest.setStatus("APPROVED");

        when(issueRequestServiceImpl.approveIssueRequest(1L))
                .thenReturn(issueRequest);

        mockMvc.perform(put("/api/issuerequests/1/approve"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.issueRequestid").value(1L))
                .andExpect(jsonPath("$.status").value("APPROVED"));
    }

    @Test
    void rejectIssueRequest_ShouldReturnRejectedIssueRequest() throws Exception {
        IssueRequest issueRequest = buildIssueRequest();
        issueRequest.setStatus("REJECTED");

        when(issueRequestServiceImpl.rejectIssueRequest(1L))
                .thenReturn(issueRequest);

        mockMvc.perform(put("/api/issuerequests/1/reject"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.issueRequestid").value(1L))
                .andExpect(jsonPath("$.status").value("REJECTED"));
    }
}
