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
import com.jsw.service.IssueRequestService;

import com.jsw.service.StockService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
public class IssueRequestServiceImpl implements IssueRequestService {

    @Autowired
    private IssueRequestRepository issueRequestRepository;

    @Autowired
    private DepartmentRepository departmentRepository;

    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private StockService stockService;

    @Override
    public IssueRequest createIssueRequest(IssueRequestDto dto){

        Department department=departmentRepository.findById(dto.getDepartmentId()).orElseThrow(()-> new ResourceNotFoundException("Department not Found"));

        IssueRequest issueRequest=new IssueRequest();
        issueRequest.setRemarks(dto.getRemarks());
        issueRequest.setRequestedBy(dto.getRequestedBy());
        issueRequest.setDepartment(department);
        issueRequest.setStatus("PENDING");
        issueRequest.setRequestDate(LocalDate.now());

        issueRequest.setRequestNo(System.currentTimeMillis() % 100000);

        List<IssueRequestItem> items = new ArrayList<>();
        for(IssueRequestItemDto itemDto : dto.getItems()){
            Product product= productRepository.findById(itemDto.getProductId())
                    .orElseThrow(()-> new ResourceNotFoundException("Product not present in inventory"));

            IssueRequestItem item=new IssueRequestItem();
            item.setProduct(product);
            item.setRequestedQty(itemDto.getRequestedQty());
            item.setIssueRequest(issueRequest);

            items.add(item);
        }

        issueRequest.setItems(items);


        return issueRequestRepository.save(issueRequest);
    }

    @Override
    public List<IssueRequest> findAllIssueRequest(){
        return issueRequestRepository.findAll();
    }

    @Override
    public List<IssueRequest> getPendingIssueRequests(){
        return issueRequestRepository.findByStatus("PENDING");
    }

    @Override
    public IssueRequest getIssueRequestById(Long id){
        return issueRequestRepository.findById(id)
                .orElseThrow(()-> new ResourceNotFoundException("IssueRequest with id "+ id+ "does not exist"));
    }

    @Override
    @Transactional
    public IssueRequest approveIssueRequest(Long id){
        IssueRequest request = getIssueRequestById(id);

        if(!request.getStatus().equals("PENDING")){
            throw new BusinessException("Only PENDING requests can be approved.Current Status : " + request.getStatus());
        }

        for(IssueRequestItem item : request.getItems()){
            stockService.deductStock(item.getProduct().getProductMasterid(),item.getRequestedQty());
        }
        request.setStatus("APPROVED");

         return issueRequestRepository.save(request);
    }

    @Override
    public IssueRequest rejectIssueRequest(Long id){
        IssueRequest request=getIssueRequestById(id);

        if(!request.getStatus().equals("PENDING")){
            throw new BusinessException("Only PENDING requests can be rejected.");
        }

        request.setStatus("REJECTED");
        return issueRequestRepository.save(request);
    }
}
