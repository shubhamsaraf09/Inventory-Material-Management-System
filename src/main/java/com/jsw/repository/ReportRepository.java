package com.jsw.repository;

import com.jsw.entity.Report;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface ReportRepository extends JpaRepository<Report,Integer> {

    @Query(value = "SELECT * from Report where " +
            "(status = coalesce(:status,status)) and "+
            "(department_name = coalesce(:dept,department_name)) and "+
            "(issue_date >= coalesce(:startDate,issue_date)) and" +
            "(issue_date <= coalesce(:endDate, issue_date))",
            nativeQuery = true)
            List<Report> findDynamicReportUsingSQL(
                    @Param("status") String status,
                    @Param("dept") String dept,
                    @Param("startDate") LocalDateTime startDate,
                    @Param("endDate") LocalDateTime endDate);


    @Query(value="select r from Report r where" +
            "(:status is null or r.status = :status) and " +
            "(:dept is null or r.departmentName = :dept) and " +
            "(cast(:startDate as date) is null or r.issueDate>=:startDate) and" +
            "(cast(:endDate as date ) is null or r.issueDate <= :endDate)")
    List<Report> findDynamicReportJPQL(
            @Param("status") String status,
            @Param("dept") String dept,
            @Param("startDate") LocalDateTime startDate,
            @Param("endDate") LocalDateTime endDate
    );
}
