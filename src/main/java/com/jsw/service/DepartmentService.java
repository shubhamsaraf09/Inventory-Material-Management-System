package com.jsw.service;

import com.jsw.dto.DepartmentRequestDto;
import com.jsw.entity.Department;

import java.util.List;
import java.util.Optional;

public interface DepartmentService {

    Department findDepartmentByid(Long id);
    List<Department> getAllDepartment();
    Department saveDepartment(DepartmentRequestDto dto);
    Department updateDepartment(Long id,DepartmentRequestDto dto);
    void deleteDepartment(Long id);

}
