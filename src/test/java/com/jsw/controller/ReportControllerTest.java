package com.jsw.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.jsw.dto.ReportRequestDto;
import com.jsw.dto.ReportResponseDto;
import com.jsw.security.JwtFilter;
import com.jsw.security.JwtUtil;
import com.jsw.service.ReportService;
import com.jsw.serviceimpl.ReportServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.request;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
@org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc(addFilters = false)
@org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest(ReportController.class)
public class ReportControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private ReportService reportService;

    @MockitoBean
    private JwtUtil jwtUtil;

    @MockitoBean
    private JwtFilter jwtFilter;

    private ObjectMapper objectMapper;
    private ReportRequestDto requestDto;
    private ReportResponseDto responseDto;

    @BeforeEach
    void setUp(){
        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());

        requestDto = new ReportRequestDto();
        requestDto.setStatus("APPROVED");
        requestDto.setDepartmentName("Manufacturing");
        requestDto.setStartDate(LocalDateTime.now().minusDays(10));
        requestDto.setEndDate(LocalDateTime.now());

        responseDto = new ReportResponseDto(
                "Steel Coils",20L,"Manufacturing",30L,1001L,"user_dev1", "APPROVED","Urgent",15L,"manager_it", LocalDateTime.now()
        );
    }

    @Test
    void getFilteredReport_ReturnsData() throws Exception{
        when(reportService.getFilteredReport(any(ReportRequestDto.class)))
                .thenReturn(List.of(responseDto));

        mockMvc.perform(post("/api/reports/filter")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(1))
                .andExpect(jsonPath("$[0].productName").value("Steel Coils"))
                .andExpect(jsonPath("$[0].departmentName").value("Manufacturing"))
                .andExpect(jsonPath("$[0].status").value("APPROVED"));
    }

    @Test
    void getFilteredReport_ReturnEmptyList() throws Exception {
        when(reportService.getFilteredReport(any(ReportRequestDto.class)))
                .thenReturn(Collections.emptyList());

        mockMvc.perform(post("/api/reports/filter")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(0));
    }


}
