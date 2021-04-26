package com.example.demo.service;

import com.example.demo.model.Report;

public interface ReportService {
    Report updateReport(float textRank);
    int incrementCountOfBlockedRequest();
}
