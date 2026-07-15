package com.jsw.serviceimpl;

import org.junit.jupiter.api.extension.ExtendWith;

import com.jsw.dto.DepartmentRequestDto;
import com.jsw.entity.Department;
import com.jsw.exception.ResourceNotFoundException;
import com.jsw.repository.DepartmentRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
public class DepartmentServiceImplTest {

    @Mock
    private DepartmentRepository departmentRepository;

    @InjectMocks
    private DepartmentServiceImpl departmentService;

    private Department department;
    private DepartmentRequestDto dto;

    @BeforeEach
    void setUp() {

        department = new Department();
        department.setDepartmentCode("HR001");
        department.setDepartmentName("Human Resources");
        department.setActiveFlag("Y");

        dto = new DepartmentRequestDto();
        dto.setDepartmentCode("HR001");
        dto.setDepartmentName("Human Resources");

    }

    @Test
    void findDepartmentById_ShouldReturnDepartment_whenIdExists() {

        when(departmentRepository.findById(1L))
                .thenReturn(Optional.of(department));

        Department result=departmentService.findDepartmentByid(1L);

        assertThat(result).isNotNull();
        assertThat(result.getDepartmentCode()).isEqualTo("HR001");
        assertThat(result.getDepartmentName()).isEqualTo("Human Resources");

        verify(departmentRepository, times(1)).findById(1L);
    }

    @Test
    void findDepartmentById_ShouldThrowRessourceNotFoundException_WhenIdNotFound()  {

        when(departmentRepository.findById(99L))
                .thenReturn(Optional.empty());

        ResourceNotFoundException exception=assertThrows(
                ResourceNotFoundException.class,
                () -> departmentService.findDepartmentByid(99L)
        );

        assertThat(exception.getMessage())
                .isEqualTo("no Department by this id exists");

        verify(departmentRepository,times(1)).findById(99L);
    }

    @Test
    void getAllDepartment_ShouldReturnList_WhenDepartmentsExists(){
        when(departmentRepository.findAll())
                .thenReturn(List.of(department));

        List<Department> result = departmentService.getAllDepartment();

        assertThat(result).isNotNull();
        assertThat(result.size()).isEqualTo(1);
        assertThat(result.get(0).getDepartmentCode()).isEqualTo("HR001");

        verify(departmentRepository,times(1)).findAll();

    }

    @Test
    void getAllDepartment_ShouldThrowException_WhenDepartmentsNotExist() {
        when(departmentRepository.findAll())
                .thenReturn(Collections.emptyList());

        List<Department> result=departmentService.getAllDepartment();

        assertThat(result).isNotNull();
        assertThat(result).isEmpty();
        verify(departmentRepository,times(1)).findAll();
    }

    @Test
    void saveDepartment_ShouldSaveAndReturnDepartment_WhenValidDtoGiven() {

        when(departmentRepository.save(any(Department.class)))
                .thenReturn(department);

        Department result=departmentService.saveDepartment(dto);

        assertThat(result).isNotNull();
        assertThat(result.getDepartmentCode()).isEqualTo("HR001");
        assertThat(result.getDepartmentName()).isEqualTo("Human Resources");
        assertThat(result.getActiveFlag()).isEqualTo("Y");
        verify(departmentRepository,times(1)).save(any(Department.class));

    }

    @Test
    void saveDepartment_ShouldAlwaysSetActiveFlagToY() {
        when(departmentRepository.save(any(Department.class)))
                .thenReturn(department);

        departmentService.saveDepartment(dto);

        ArgumentCaptor<Department> captor =
                ArgumentCaptor.forClass(Department.class);

        verify(departmentRepository).save(captor.capture());

        Department captured = captor.getValue();
        assertThat(captured.getActiveFlag()).isEqualTo("Y");
        assertThat(captured.getDepartmentCode()).isEqualTo("HR001");
        assertThat(captured.getDepartmentName()).isEqualTo("Human Resources");

    }

    @Test
    void updateDepartment_ShouldUpdateAndRetrn_WhenIdExists(){
        DepartmentRequestDto updateDto = new DepartmentRequestDto();
        updateDto.setDepartmentCode("HR002");

        Department updatedDept = new Department();
        updatedDept.setDepartmentCode("HR002");
        updatedDept.setDepartmentName("HR Updated");
        updatedDept.setActiveFlag("Y");

        when(departmentRepository.findById(1L))
                .thenReturn(Optional.of(department));
        when(departmentRepository.save(any(Department.class)))
                .thenReturn(updatedDept);

        Department result = departmentService.updateDepartment(1L,updateDto);

        assertThat(result.getDepartmentCode()).isEqualTo("HR002");
        assertThat(result.getDepartmentName()).isEqualTo("HR Updated");
        assertThat(result.getActiveFlag()).isEqualTo("Y");

        verify(departmentRepository, times(1)).findById(1L);
        verify(departmentRepository, times(1)).save(any(Department.class));
    }

    @Test
    void updateDepartment_ShouldThrowResourceNotFoundException_WhenIdNotFound(){
        when(departmentRepository.findById(99L))
                .thenReturn(Optional.empty());

        ResourceNotFoundException exception = assertThrows(
                ResourceNotFoundException.class,
                () -> departmentService.updateDepartment(99L,dto)
        );

        assertThat(exception.getMessage())
                .isEqualTo("Cannot update as it is not present");

        verify(departmentRepository,never()).save(any(Department.class));
    }

    @Test
    void deleteDepartment_ShouldCallDeleteById_WhenIdGiven() {

        doNothing().when(departmentRepository).deleteById(1L);

        departmentService.deleteDepartment(1L);

        verify(departmentRepository,times(1)).deleteById(1L);
    }

    @Test
    void deleteDepartment_ShouldPropagate_WhenRepositoryThrows() {
        doThrow(new RuntimeException("DB error"))
                .when(departmentRepository).deleteById(99L);

        RuntimeException exception=assertThrows(
                RuntimeException.class,
                () -> departmentService.deleteDepartment(99L)
        );
        assertThat(exception.getMessage()).isEqualTo("DB error");
        verify(departmentRepository, times(1)).deleteById(99L);
    }

}
