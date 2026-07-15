package com.jsw.serviceimpl;

import com.jsw.dto.ReportRequestDto;
import com.jsw.dto.ReportResponseDto;
import com.jsw.entity.Report;
import com.jsw.repository.ReportRepository;
import com.jsw.service.ReportService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class ReportServiceImpl implements ReportService {

    @Autowired
    public ReportRepository reportRepository;

    @Value("${feature.toggles.use-native-sql-for-reports:false}")
    private boolean useNativeSql;

    @Override
    public List<ReportResponseDto> getFilteredReport(ReportRequestDto dto) {

        List<Report> rawReports;

        log.info("Fetching filtered reports. Status: {}, Department: {}, Start: {}, End: {}", dto.getStatus(), dto.getDepartmentName(), dto.getStartDate(), dto.getEndDate());

        if (useNativeSql) {

            log.debug("Executing database query using NATIVE SQL strategy");

            rawReports = reportRepository.findDynamicReportUsingSQL(
                    dto.getStatus(),
                    dto.getDepartmentName(),
                    dto.getStartDate(),
                    dto.getEndDate()
            );
        } else {
            log.debug("Executing database query using JPQL strategy");
            rawReports = reportRepository.findDynamicReportJPQL(
                    dto.getStatus(),
                    dto.getDepartmentName(),
                    dto.getStartDate(),
                    dto.getEndDate()
            );
        }

        log.info("Successfully fetched {} raw reports fromo the database",rawReports.size());

        return rawReports.stream().map(report -> new ReportResponseDto(
                report.getProductName(),
                report.getUnit(),
                report.getDepartmentName(),
                report.getAvailableQty(),
                report.getRequestNo(),
                report.getRequestedBy(),
                report.getStatus(),
                report.getRemarks(),
                report.getRequestedQty(),
                report.getIssuedBy(),
                report.getIssueDate()
        )).collect(Collectors.toList());
    }
}
