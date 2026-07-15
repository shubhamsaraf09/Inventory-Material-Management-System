package com.jsw.serviceimpl;


import com.jsw.dto.IssueRequestDto;
import com.jsw.dto.IssueRequestItemDto;
import com.jsw.entity.Department;
import com.jsw.entity.IssueRequest;
import com.jsw.entity.IssueRequestItem;
import com.jsw.entity.Product;
import com.jsw.exception.BusinessException;
import com.jsw.exception.ResourceNotFoundException;
import com.jsw.repository.DepartmentRepository;
import com.jsw.repository.IssueRequestRepository;
import com.jsw.repository.ProductRepository;
import com.jsw.service.StockService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class IssueRequestServiceImplTest {


    @Mock
    private IssueRequestRepository issueRequestRepository;

    @Mock
    private DepartmentRepository departmentRepository;

    @Mock
    private ProductRepository productRepository;

    @Mock
    private StockService stockService;

    @InjectMocks
    private IssueRequestServiceImpl issueRequestService;

    private Department department;
    private Product product;
    private IssueRequestDto issueRequestDto;
    private IssueRequestItemDto itemDto;
    private IssueRequest request;
    private IssueRequestItem item;

    @BeforeEach
    void setUp() {
        department = new Department();
        department.setDepartmentId(1L);
        department.setDepartmentCode("D001");
        department.setDepartmentName("IT");

        product = new Product();
        product.setProductMasterid(10L);
        product.setProductCode("P001");
        product.setProductName("Laptop");
        product.setUnit(20);
        product.setMinStock(5);
        product.setActiveFlag("Y");

        itemDto = new IssueRequestItemDto();
        itemDto.setProductId(10L);
        itemDto.setRequestedQty(2L);

        issueRequestDto = new IssueRequestDto();
        issueRequestDto.setDepartmentId(1L);
        issueRequestDto.setRequestedBy("Shubh");
        issueRequestDto.setRemarks("Need items");
        issueRequestDto.setItems(List.of(itemDto));

        item = new IssueRequestItem();
        item.setProduct(product);
        item.setRequestedQty(2L);

        request = new IssueRequest();
        request.setIssueRequestid(100L);
        request.setRequestNo(12345L);
        request.setRequestDate(LocalDate.now());
        request.setDepartment(department);
        request.setRequestedBy("Shubh");
        request.setStatus("PENDING");
        request.setRemarks("Need items");
        request.setItems(List.of(item));
    }

    @Test
    void createIssueRequest_ShouldSaveRequest_WhenDepartmentAndProductsExist() {
        when(departmentRepository.findById(1L)).thenReturn(Optional.of(department));
        when(productRepository.findById(10L)).thenReturn(Optional.of(product));
        when(issueRequestRepository.save(any(IssueRequest.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        IssueRequest result = issueRequestService.createIssueRequest(issueRequestDto);

        assertThat(result).isNotNull();
        assertThat(result.getDepartment()).isEqualTo(department);
        assertThat(result.getRequestedBy()).isEqualTo("Shubh");
        assertThat(result.getRemarks()).isEqualTo("Need items");
        assertThat(result.getStatus()).isEqualTo("PENDING");
        assertThat(result.getItems()).hasSize(1);
        assertThat(result.getItems().get(0).getProduct()).isEqualTo(product);
        assertThat(result.getItems().get(0).getRequestedQty()).isEqualTo(2L);

        verify(departmentRepository).findById(1L);
        verify(productRepository).findById(10L);
        verify(issueRequestRepository).save(any(IssueRequest.class));
    }

    @Test
    void createIssueRequest_ShouldThrow_WhenDepartmentNotFound() {
        when(departmentRepository.findById(1L)).thenReturn(Optional.empty());

        ResourceNotFoundException ex = assertThrows(
                ResourceNotFoundException.class,
                () -> issueRequestService.createIssueRequest(issueRequestDto)
        );

        assertThat(ex.getMessage()).isEqualTo("Department not Found");
        verify(departmentRepository).findById(1L);
        verifyNoInteractions(productRepository, issueRequestRepository);
    }

    @Test
    void createIssueRequest_ShouldThrow_WhenProductNotFound() {
        when(departmentRepository.findById(1L)).thenReturn(Optional.of(department));
        when(productRepository.findById(10L)).thenReturn(Optional.empty());

        ResourceNotFoundException ex = assertThrows(
                ResourceNotFoundException.class,
                () -> issueRequestService.createIssueRequest(issueRequestDto)
        );

        assertThat(ex.getMessage()).isEqualTo("Product not present in inventory");
        verify(departmentRepository).findById(1L);
        verify(productRepository).findById(10L);
        verify(issueRequestRepository, never()).save(any(IssueRequest.class));
    }

    @Test
    void findAllIssueRequest_ShouldReturnAllRequests() {
        when(issueRequestRepository.findAll()).thenReturn(List.of(request));

        List<IssueRequest> result = issueRequestService.findAllIssueRequest();

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getIssueRequestid()).isEqualTo(100L);
        verify(issueRequestRepository).findAll();
    }

    @Test
    void getPendingIssueRequests_ShouldReturnPendingList() {
        when(issueRequestRepository.findByStatus("PENDING")).thenReturn(List.of(request));

        List<IssueRequest> result = issueRequestService.getPendingIssueRequests();

        assertThat(result).hasSize(1);
        verify(issueRequestRepository).findByStatus("PENDING");
    }

    @Test
    void getIssueRequestById_ShouldReturnRequest_WhenFound() {
        when(issueRequestRepository.findById(100L)).thenReturn(Optional.of(request));

        IssueRequest result = issueRequestService.getIssueRequestById(100L);

        assertThat(result).isNotNull();
        assertThat(result.getIssueRequestid()).isEqualTo(100L);
        verify(issueRequestRepository).findById(100L);
    }

    @Test
    void getIssueRequestById_ShouldThrow_WhenNotFound() {
        when(issueRequestRepository.findById(100L)).thenReturn(Optional.empty());

        ResourceNotFoundException ex = assertThrows(
                ResourceNotFoundException.class,
                () -> issueRequestService.getIssueRequestById(100L)
        );

        assertThat(ex.getMessage()).isEqualTo("IssueRequest with id 100does not exist");
        verify(issueRequestRepository).findById(100L);
    }

    @Test
    void approveIssueRequest_ShouldApproveAndDeductStock() {
        request.setStatus("PENDING");
        when(issueRequestRepository.findById(100L)).thenReturn(Optional.of(request));
        when(issueRequestRepository.save(any(IssueRequest.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));
        when(stockService.deductStock(10L, 2L)).thenReturn(null);

        IssueRequest result = issueRequestService.approveIssueRequest(100L);

        assertThat(result.getStatus()).isEqualTo("APPROVED");
        verify(stockService).deductStock(10L, 2L);
        verify(issueRequestRepository).save(any(IssueRequest.class));
    }

    @Test
    void approveIssueRequest_ShouldThrow_WhenStatusIsNotPending() {
        request.setStatus("APPROVED");
        when(issueRequestRepository.findById(100L)).thenReturn(Optional.of(request));

        BusinessException ex = assertThrows(
                BusinessException.class,
                () -> issueRequestService.approveIssueRequest(100L)
        );

        assertThat(ex.getMessage())
                .isEqualTo("Only PENDING requests can be approved.Current Status : APPROVED");
        verify(stockService, never()).deductStock(any(), any());
        verify(issueRequestRepository, never()).save(any(IssueRequest.class));
    }

    @Test
    void rejectIssueRequest_ShouldReject_WhenStatusIsPending() {
        request.setStatus("PENDING");
        when(issueRequestRepository.findById(100L)).thenReturn(Optional.of(request));
        when(issueRequestRepository.save(any(IssueRequest.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        IssueRequest result = issueRequestService.rejectIssueRequest(100L);

        assertThat(result.getStatus()).isEqualTo("REJECTED");
        verify(issueRequestRepository).save(any(IssueRequest.class));
    }

    @Test
    void rejectIssueRequest_ShouldThrow_WhenStatusIsNotPending() {
        request.setStatus("APPROVED");
        when(issueRequestRepository.findById(100L)).thenReturn(Optional.of(request));

        BusinessException ex = assertThrows(
                BusinessException.class,
                () -> issueRequestService.rejectIssueRequest(100L)
        );

        assertThat(ex.getMessage()).isEqualTo("Only PENDING requests can be rejected.");
        verify(issueRequestRepository, never()).save(any(IssueRequest.class));
    }

}