package com.jsw.serviceimpl;

import com.jsw.dto.ReportRequestDto;
import com.jsw.dto.ReportResponseDto;
import com.jsw.entity.Report;
import com.jsw.repository.ReportRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ReportServiceImplTest {

    @Mock
    private ReportRepository reportRepository;

    @InjectMocks
    private ReportServiceImpl reportService;


    private ReportRequestDto requestDto;
    private Report mockReport;

    @BeforeEach
    void setUp(){

        requestDto = new ReportRequestDto();
        requestDto.setStatus("APPROVED");
        requestDto.setDepartmentName("Manufacturing");
        requestDto.setStartDate(LocalDateTime.of(2026,1,1,0,0));
        requestDto.setEndDate(LocalDateTime.of(2026,12,31,23,59));

        mockReport = new Report();
        mockReport.setProductName("Steel Coils");
        mockReport.setUnit(20L);
        mockReport.setDepartmentName("Manufacturing");
        mockReport.setAvailableQty(30L);
        mockReport.setRequestNo(1001L);
        mockReport.setRequestedBy("user_dev1");
        mockReport.setStatus("APPROVED");
        mockReport.setRemarks("Urgent");
        mockReport.setRequestedQty(20L);
        mockReport.setIssuedBy("manager_it");
        mockReport.setIssueDate(LocalDateTime.now());

    }

    @Test
    void getFilteredReport_UsingNativeSql() {

        ReflectionTestUtils.setField(reportService, "useNativeSql",true);

        when(reportRepository.findDynamicReportUsingSQL(
                any(),any(),any(), any()
        )).thenReturn(List.of(mockReport));

        List<ReportResponseDto> responseList = reportService.getFilteredReport(requestDto);

        assertNotNull(responseList);
        assertEquals(1,responseList.size());

        ReportResponseDto responseDto = responseList.get(0);
        assertEquals("Steel Coils", responseDto.getProductName());
        assertEquals("Manufacturing", responseDto.getDepartmentName());
        assertEquals("APPROVED",responseDto.getStatus());

        verify(reportRepository, times(1)).findDynamicReportUsingSQL(any(),any(),any(),any());
        verify(reportRepository,never()).findDynamicReportJPQL(any(),any(),any(),any());
    }

    @Test
    void getFilteredReport_UsingJpql() {
        ReflectionTestUtils.setField(reportService, "useNativeSql",false);

        when(reportRepository.findDynamicReportJPQL(
                any(), any(),any(),any()))
                .thenReturn(List.of(mockReport));

        List<ReportResponseDto> responseList = reportService.getFilteredReport(requestDto);

        assertNotNull(responseList);
        assertEquals(1,responseList.size());
        assertEquals("Steel Coils", responseList.get(0).getProductName());

        verify(reportRepository,times(1)).findDynamicReportJPQL(any(),any(),any(),any());
        verify(reportRepository,never()).findDynamicReportUsingSQL(any(),any(),any(),any());
    }

    @Test
    void getFilteredReport_EmptyResultMap(){
        ReflectionTestUtils.setField(reportService,"useNativeSql",false);
        when(reportRepository.findDynamicReportJPQL(any(),any(),any(),any()))
                .thenReturn(Collections.emptyList());

        List<ReportResponseDto> responseList = reportService.getFilteredReport(requestDto);

        assertNotNull(responseList);
        assertTrue(responseList.isEmpty(),"The mapped DTO list should be empty if DB returns no records");
    }

}
