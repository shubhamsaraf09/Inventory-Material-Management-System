package com.jsw.controller;

import com.jsw.entity.IssueTransaction;
import com.jsw.serviceimpl.IssueTransactionServiceImpl;
import jakarta.validation.constraints.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/transactions")
@PreAuthorize("hasAnyRole('MANAGER', 'ADMIN')")
public class IssueTransactionController {

    @Autowired
    private IssueTransactionServiceImpl issueTransactionServiceImpl;

    @GetMapping
    public ResponseEntity<List<IssueTransaction>> getALlTransactions(){
        return ResponseEntity.ok(issueTransactionServiceImpl.getAllTransactions());
    }

    @GetMapping("/department/{id}")
    public ResponseEntity<List<IssueTransaction>> getTransactionsByDepartment(@PathVariable Long id){
        return ResponseEntity.ok(issueTransactionServiceImpl.getTransactionsByDepartment(id));
    }

    @GetMapping("/filters")
    public ResponseEntity<List<IssueTransaction>> getTransactionsByRange(
            @RequestParam @NotNull(message="Start date is required") @DateTimeFormat(iso=DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam @NotNull(message="End date is required") @DateTimeFormat(iso=DateTimeFormat.ISO.DATE) LocalDate endDate
            ) {
        List<IssueTransaction> transactions = issueTransactionServiceImpl.getTransactionsByDateRange(startDate,endDate);
        return ResponseEntity.ok(transactions);
    }
}
