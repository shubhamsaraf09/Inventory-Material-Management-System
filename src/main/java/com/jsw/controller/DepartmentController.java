package com.jsw.controller;

import com.jsw.dto.DepartmentRequestDto;
import com.jsw.entity.Department;
import com.jsw.service.DepartmentService;
import com.jsw.serviceimpl.DepartmentServiceImpl;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/departments")
public class DepartmentController {

    @Autowired
    private DepartmentServiceImpl departmentServiceImpl;

    @PreAuthorize("hasAnyRole('USER', 'MANAGER', 'ADMIN')")
    @GetMapping("/findAll")
    public ResponseEntity<List<Department>> getAllDepartment(){
        return ResponseEntity.ok(departmentServiceImpl.getAllDepartment());
    }

    @PreAuthorize("hasAnyRole('USER', 'MANAGER', 'ADMIN')")
    @GetMapping("/{id}")
    public ResponseEntity<Department> getDepartmentById(@PathVariable Long id){
        return ResponseEntity.
                ok(departmentServiceImpl.findDepartmentByid(id));
    }

    @PreAuthorize("hasAnyRole('MANAGER', 'ADMIN')")
    @PostMapping("/save")
    public ResponseEntity<Department> saveDepartment(@Valid @RequestBody DepartmentRequestDto dto){
        return new ResponseEntity<>(departmentServiceImpl.saveDepartment(dto), HttpStatus.CREATED);
    }

    @PreAuthorize("hasAnyRole('MANAGER', 'ADMIN')")
    @PutMapping("/{id}")
    public ResponseEntity<Department> updateDepartment(@PathVariable Long id,@Valid @RequestBody DepartmentRequestDto dto){
        return ResponseEntity.ok(departmentServiceImpl.updateDepartment(id,dto));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteDepartment(@PathVariable Long id){
        departmentServiceImpl.deleteDepartment(id);
        return ResponseEntity.noContent().build();
    }
}
