package com.jsw.repository;

import com.jsw.entity.IssueRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

public interface IssueRequestRepository extends JpaRepository<IssueRequest,Long> {
    List<IssueRequest> findByStatus(String status);
    List<IssueRequest> findByDepartment_DepartmentId(Long departmentId);

}
