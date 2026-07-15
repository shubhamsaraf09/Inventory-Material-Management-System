package com.jsw.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jsw.dto.DepartmentRequestDto;
import com.jsw.entity.Department;
import com.jsw.security.JwtFilter;
import com.jsw.security.JwtUtil;
import com.jsw.service.DepartmentService;
import com.jsw.serviceimpl.DepartmentServiceImpl;
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

@WebMvcTest(DepartmentController.class)
@AutoConfigureMockMvc(addFilters = false)
public class DepartmentControllerTest {

    @Autowired
    private MockMvc mockMvc;

    private final ObjectMapper objectMapper = new ObjectMapper();

    private Department department;

    @MockitoBean
    private DepartmentServiceImpl departmentServiceImpl;

    @MockitoBean
    private JwtUtil jwtUtil;

    @MockitoBean
    private JwtFilter jwtFilter;

    @Test
    void getDepartmentById_ShouldReturnDepartment_IfIdExists() throws Exception {
        Department department = new Department();
        department.setDepartmentId(1L);
        department.setDepartmentName("IT");
        department.setDepartmentCode("D001");
        department.setActiveFlag("Y");

        when(departmentServiceImpl.findDepartmentByid(1L))
                .thenReturn(department);

        mockMvc.perform(get("/api/departments/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.departmentCode").value("D001"))
                .andExpect(jsonPath("$.departmentName").value("IT"))
                .andExpect(jsonPath("$.departmentId").value(1L));
    }

    @Test
    void getAllDepartment_ShouldReturnListOfDepartment() throws Exception {

        Department department = new Department();
        department.setDepartmentId(1L);
        department.setDepartmentCode("D001");
        department.setDepartmentName("IT");
        department.setActiveFlag("Y");

        when(departmentServiceImpl.getAllDepartment())
                .thenReturn(List.of(department));

        mockMvc.perform(get("/api/departments/findAll"))
                .andExpect(jsonPath("$[0].departmentName").value("IT"))
                .andExpect(jsonPath("$[0].departmentCode").value("D001"))
                .andExpect(jsonPath("$[0].departmentId").value(1L));
    }

    @Test
    void saveDepartment_WhenValidDtoProvided() throws Exception {
        DepartmentRequestDto dto = new DepartmentRequestDto();
        dto.setDepartmentCode("D001");
        dto.setDepartmentName("IT");
        dto.setActiveFlag("Y");

        Department department = new Department();
        department.setDepartmentId(1L);
        department.setDepartmentCode("D001");
        department.setDepartmentName("IT");
        department.setActiveFlag("Y");

        when(departmentServiceImpl.saveDepartment(any(DepartmentRequestDto.class)))
                .thenReturn(department);

        mockMvc.perform(post("/api/departments/save")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.departmentCode").value("D001"))
                .andExpect(jsonPath("$.departmentName").value("IT"))
                .andExpect(jsonPath("$.departmentId").value(1L));
    }

    @Test
    void updateDepartment_ShouldReturnDepartment() throws Exception {

        DepartmentRequestDto dto = new DepartmentRequestDto();
        dto.setDepartmentName("HR");
        dto.setDepartmentCode("D002");
        dto.setActiveFlag("Y");

        Department updtDept = new Department();
        updtDept.setDepartmentId(1L);
        updtDept.setDepartmentName("HR");
        updtDept.setDepartmentCode("D002");
        updtDept.setActiveFlag("Y");

        when(departmentServiceImpl.updateDepartment(eq(1L), any(DepartmentRequestDto.class)))
                .thenReturn(updtDept);

        mockMvc.perform(put("/api/departments/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.departmentCode").value("D002"))
                .andExpect(jsonPath("$.departmentName").value("HR"))
                .andExpect(jsonPath("$.departmentId").value(1L));

    }

    @Test
    void deleteDepartment_ShouldReturnNothing() throws Exception{
        doNothing().when(departmentServiceImpl).deleteDepartment(1L);

        mockMvc.perform(delete("/api/departments/1"))
                .andExpect(status().isNoContent());

    }

}
