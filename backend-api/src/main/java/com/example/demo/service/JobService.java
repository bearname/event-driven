package com.example.demo.service;

import com.example.demo.model.Job;
import com.example.demo.model.RegionCode;

public interface JobService {
    String register(Job job);

    String register(Job job, RegionCode regionCode);

    Float getProcessingResult(String jobId);
}
