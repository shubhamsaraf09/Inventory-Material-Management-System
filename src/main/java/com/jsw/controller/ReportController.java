package com.jsw.controller;

import com.jsw.dto.ReportRequestDto;
import com.jsw.dto.ReportResponseDto;
import com.jsw.entity.Report;
import com.jsw.service.ReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/reports")
@PreAuthorize("hasAnyRole('MANAGER', 'ADMIN')")
public class ReportController {

    @Autowired
    private ReportService reportService;

    @PostMapping("/filter")
    public ResponseEntity<List<ReportResponseDto>> getFilteredReport(@RequestBody ReportRequestDto dto){
        return ResponseEntity.ok(reportService.getFilteredReport(dto));
    }
}
