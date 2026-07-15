package com.jsw.controller;

import com.jsw.dto.IssueRequestDto;
import com.jsw.entity.IssueRequest;
import com.jsw.serviceimpl.IssueRequestServiceImpl;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/issuerequests")
public class IssueRequestController {

    @Autowired
    private IssueRequestServiceImpl issueRequestServiceImpl;

    @PreAuthorize("hasAnyRole('USER', 'MANAGER', 'ADMIN')")
    @PostMapping
    public ResponseEntity<IssueRequest> createIssueRequest(@Valid @RequestBody IssueRequestDto dto){
        return new ResponseEntity<>(issueRequestServiceImpl.createIssueRequest(dto), HttpStatus.CREATED);
    }

    @PreAuthorize("hasAnyRole('USER', 'MANAGER', 'ADMIN')")
    @GetMapping
    public ResponseEntity<List<IssueRequest>> getAllIssueRequests(){
        return ResponseEntity.ok(issueRequestServiceImpl.findAllIssueRequest());
    }

    @PreAuthorize("hasAnyRole('USER', 'MANAGER', 'ADMIN')")
    @GetMapping("/pending")
    public ResponseEntity<List<IssueRequest>> getPendingIssueRequests(){
        return ResponseEntity.ok(issueRequestServiceImpl.getPendingIssueRequests());
    }

    @PreAuthorize("hasAnyRole('USER', 'MANAGER', 'ADMIN')")
    @GetMapping("/{id}")
    public ResponseEntity<IssueRequest> getIssueRequestById(@PathVariable Long id){
        return ResponseEntity.ok(issueRequestServiceImpl.getIssueRequestById(id));
    }

    @PreAuthorize("hasAnyRole('MANAGER', 'ADMIN')")
    @PutMapping("/{id}/approve")
    public ResponseEntity<IssueRequest> approveIssueRequest(@PathVariable Long id){
        return ResponseEntity.ok(issueRequestServiceImpl.approveIssueRequest(id));
    }

    @PreAuthorize("hasAnyRole('MANAGER', 'ADMIN')")
    @PutMapping("/{id}/reject")
    public ResponseEntity<IssueRequest> rejectIssueRequest(@PathVariable Long id){
        return ResponseEntity.ok(issueRequestServiceImpl.rejectIssueRequest(id));
    }

}
