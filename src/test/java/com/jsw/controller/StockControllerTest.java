package com.jsw.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jsw.dto.StockRequestDto;
import com.jsw.entity.Stock;
import com.jsw.security.JwtFilter;
import com.jsw.security.JwtUtil;
import com.jsw.serviceimpl.StockServiceImpl;
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
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(StockController.class)
@AutoConfigureMockMvc(addFilters = false)
class StockControllerTest {

    @Autowired
    private MockMvc mockMvc;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @MockitoBean
    private StockServiceImpl stockServiceImpl;

    @MockitoBean
    private JwtFilter jwtFilter;

    @MockitoBean
    private JwtUtil jwtUtil;

    @Test
    void getAllStock_ShouldReturnList() throws Exception {
        Stock stock = new Stock();
        stock.setStockId(1L);
        stock.setAvailableQty(50L);

        when(stockServiceImpl.getAllStock()).thenReturn(List.of(stock));

        mockMvc.perform(get("/api/stocks"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].stockId").value(1L))
                .andExpect(jsonPath("$[0].availableQty").value(50L))
                .andExpect(jsonPath("$[0].product").doesNotExist());
    }

    @Test
    void addStock_ShouldReturnUpdatedStock_WhenValidRequest() throws Exception {
        StockRequestDto dto = new StockRequestDto(10L, 20L);

        Stock stock = new Stock();
        stock.setStockId(1L);
        stock.setAvailableQty(70L);

        when(stockServiceImpl.addStock(any(StockRequestDto.class))).thenReturn(stock);

        mockMvc.perform(put("/api/stocks/add")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.stockId").value(1L))
                .andExpect(jsonPath("$.availableQty").value(70L))
                .andExpect(jsonPath("$.product").doesNotExist());
    }

    @Test
    void addStock_ShouldReturnBadRequest_WhenProductIdMissing() throws Exception {
        StockRequestDto dto = new StockRequestDto(null, 20L);

        mockMvc.perform(put("/api/stocks/add")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void addStock_ShouldReturnBadRequest_WhenQuantityIsZero() throws Exception {
        StockRequestDto dto = new StockRequestDto(10L, 0L);

        mockMvc.perform(put("/api/stocks/add")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void getStockById_ShouldReturnStock_WhenFound() throws Exception {
        Stock stock = new Stock();
        stock.setStockId(1L);
        stock.setAvailableQty(50L);

        when(stockServiceImpl.getStockByProductId(10L)).thenReturn(stock);

        mockMvc.perform(get("/api/stocks/10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.stockId").value(1L))
                .andExpect(jsonPath("$.availableQty").value(50L));
    }

    @Test
    void deductStock_ShouldReturnUpdatedStock_WhenValidRequest() throws Exception {
        StockRequestDto dto = new StockRequestDto(10L, 5L);

        Stock stock = new Stock();
        stock.setStockId(1L);
        stock.setAvailableQty(45L);

        when(stockServiceImpl.deductStock(eq(10L), eq(5L))).thenReturn(stock);

        mockMvc.perform(put("/api/stocks/deduct")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.stockId").value(1L))
                .andExpect(jsonPath("$.availableQty").value(45L));
    }

    @Test
    void deductStock_ShouldReturnBadRequest_WhenQuantityNegative() throws Exception {
        StockRequestDto dto = new StockRequestDto(10L, -5L);

        mockMvc.perform(put("/api/stocks/deduct")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void getLowStockReport_ShouldReturnList_WhenThresholdValid() throws Exception {
        Stock stock = new Stock();
        stock.setStockId(1L);
        stock.setAvailableQty(3L);

        when(stockServiceImpl.getLowStockReport(5L)).thenReturn(List.of(stock));

        mockMvc.perform(put("/api/stocks/low-stock")
                        .param("threshold", "5"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].stockId").value(1L))
                .andExpect(jsonPath("$[0].availableQty").value(3L));
    }

    @Test
    void getLowStockReport_ShouldReturnBadRequest_WhenThresholdMissing() throws Exception {
        mockMvc.perform(put("/api/stocks/low-stock"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void getLowStockReport_ShouldReturnBadRequest_WhenThresholdNegative() throws Exception {
        mockMvc.perform(put("/api/stocks/low-stock")
                        .param("threshold", "-1"))
                .andExpect(status().isBadRequest());
    }
}