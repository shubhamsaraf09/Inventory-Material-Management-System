package com.jsw.service;

import com.jsw.entity.IssueTransaction;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public interface IssueTransactionService {

    List<IssueTransaction> getAllTransactions();

    List<IssueTransaction> getTransactionsByDepartment(Long id);

    List<IssueTransaction> getTransactionsByDateRange(LocalDate start, LocalDate end);
}
