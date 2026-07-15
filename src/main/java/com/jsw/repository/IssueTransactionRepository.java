package com.jsw.repository;

import com.jsw.entity.IssueTransaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface IssueTransactionRepository extends JpaRepository<IssueTransaction, Long> {

    List<IssueTransaction> findByIssueRequest_Department_DepartmentId(Long id);

    List<IssueTransaction> findByTransactionDateBetween(LocalDateTime startDate, LocalDateTime endDate);
}