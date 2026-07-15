package com.jsw.serviceimpl;

import com.jsw.entity.IssueTransaction;
import com.jsw.repository.IssueTransactionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class IssueTransactionServiceImplTest {

    @Mock
    private IssueTransactionRepository issueTransactionRepository;

    @InjectMocks
    private IssueTransactionServiceImpl issueTransactionService;

    private IssueTransaction transaction;

    @BeforeEach
    void setUp() {
        transaction = new IssueTransaction();
    }

    @Test
    void getAllTransactions_ShouldReturnAllTransactions() {
        when(issueTransactionRepository.findAll()).thenReturn(List.of(transaction));

        List<IssueTransaction> result = issueTransactionService.getAllTransactions();

        assertThat(result).hasSize(1);
        verify(issueTransactionRepository).findAll();
    }

    @Test
    void getTransactionsByDepartment_ShouldReturnTransactionsForDepartment() {
        when(issueTransactionRepository.findByIssueRequest_Department_DepartmentId(1L))
                .thenReturn(List.of(transaction));

        List<IssueTransaction> result = issueTransactionService.getTransactionsByDepartment(1L);

        assertThat(result).hasSize(1);
        verify(issueTransactionRepository).findByIssueRequest_Department_DepartmentId(1L);
    }

    @Test
    void getTransactionsByDateRange_ShouldConvertDatesAndReturnTransactions() {
        LocalDate start = LocalDate.of(2026, 7, 1);
        LocalDate end = LocalDate.of(2026, 7, 3);

        LocalDateTime startOfDay = start.atStartOfDay();
        LocalDateTime endOfDay = end.atTime(java.time.LocalTime.MAX);

        when(issueTransactionRepository.findByTransactionDateBetween(startOfDay, endOfDay))
                .thenReturn(List.of(transaction));

        List<IssueTransaction> result = issueTransactionService.getTransactionsByDateRange(start, end);

        assertThat(result).hasSize(1);
        verify(issueTransactionRepository).findByTransactionDateBetween(startOfDay, endOfDay);
    }

}
