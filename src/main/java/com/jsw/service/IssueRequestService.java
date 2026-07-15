package com.jsw.service;

import com.jsw.dto.IssueRequestDto;
import com.jsw.entity.IssueRequest;

import java.util.List;

public interface IssueRequestService {
    IssueRequest createIssueRequest(IssueRequestDto dto);
    List<IssueRequest> findAllIssueRequest();
    List<IssueRequest> getPendingIssueRequests();
    IssueRequest getIssueRequestById(Long id);

    IssueRequest approveIssueRequest(Long id);
    IssueRequest rejectIssueRequest(Long id);

}
