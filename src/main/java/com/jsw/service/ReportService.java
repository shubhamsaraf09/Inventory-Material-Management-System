package com.jsw.service;

import com.jsw.dto.ReportRequestDto;
import com.jsw.dto.ReportResponseDto;
import com.jsw.entity.Report;

import java.time.LocalDateTime;
import java.util.List;

public interface ReportService {

    List<ReportResponseDto> getFilteredReport(ReportRequestDto dto);

}
