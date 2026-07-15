package com.jsw.serviceimpl;

import com.jsw.entity.IssueTransaction;
import com.jsw.repository.IssueTransactionRepository;
import com.jsw.service.IssueTransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class IssueTransactionServiceImpl implements IssueTransactionService {

    @Autowired
    private IssueTransactionRepository issueTransactionRepository;

    @Override
    public List<IssueTransaction> getAllTransactions(){
        return issueTransactionRepository.findAll();
    }

    @Override
    public List<IssueTransaction> getTransactionsByDepartment(Long id){
        return issueTransactionRepository.findByIssueRequest_Department_DepartmentId(id);
    }

    @Override
    public List<IssueTransaction> getTransactionsByDateRange(LocalDate start, LocalDate end){

        LocalDateTime startOfDay=start.atStartOfDay();
        LocalDateTime endOfDay=end.atTime(LocalTime.MAX);

        return issueTransactionRepository.findByTransactionDateBetween(startOfDay, endOfDay);
    }
}
