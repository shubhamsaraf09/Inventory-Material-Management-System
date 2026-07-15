package com.jsw.serviceimpl;

import com.jsw.dto.DepartmentRequestDto;
import com.jsw.entity.Department;
import com.jsw.exception.ResourceNotFoundException;
import com.jsw.repository.DepartmentRepository;
import com.jsw.service.DepartmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class DepartmentServiceImpl implements DepartmentService {

    @Autowired
    private DepartmentRepository departmentRepository;

    @Override
    public Department findDepartmentByid(Long id){
       return departmentRepository.findById(id).orElseThrow(()-> new ResourceNotFoundException("no Department by this id exists"));
    }

    @Override
    public List<Department> getAllDepartment(){
        return departmentRepository.findAll();
    }

    @Override
    public Department saveDepartment(DepartmentRequestDto dto){
        Department department=new Department();
        department.setDepartmentCode(dto.getDepartmentCode());
        department.setDepartmentName(dto.getDepartmentName());
        department.setActiveFlag("Y");

        return departmentRepository.save(department);
    }

    @Override
    public Department updateDepartment(Long id,DepartmentRequestDto dto){
        Department department=departmentRepository.findById(id).orElseThrow(()-> new ResourceNotFoundException("Cannot update as it is not present"));
        department.setDepartmentCode(dto.getDepartmentCode());
        department.setDepartmentName(dto.getDepartmentName());
        department.setActiveFlag("Y");

        return departmentRepository.save(department);
    }

    @Override
    public void deleteDepartment(Long id){
        departmentRepository.deleteById(id);
    }

}
