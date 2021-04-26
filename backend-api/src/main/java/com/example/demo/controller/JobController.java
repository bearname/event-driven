package com.example.demo.controller;

import com.example.demo.dto.JobDto;
import com.example.demo.model.Job;
import com.example.demo.model.JobStatus;
import com.example.demo.model.RegionCode;
import com.example.demo.service.JobService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;

@RestController
@CrossOrigin(origins = "http://localhost:3000")
@RequestMapping("/api/job")
public class JobController {
    private final JobService jobService;

    @Autowired
    public JobController(JobService jobService) {
        this.jobService = jobService;
    }

    @PostMapping(value = "/create", consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_FORM_URLENCODED_VALUE},
            produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_FORM_URLENCODED_VALUE})
    public ResponseEntity<HashMap<String, String>> createTask(@RequestBody JobDto jobDto) {
        System.out.println("regionCode" + jobDto.getRegion());
        System.out.println(jobDto.getDescription());
        final String jobId = jobService.register(new Job(jobDto.getDescription()), RegionCode.valueOf(jobDto.getRegion()));

        final HashMap<String, String> objectObjectHashMap = new HashMap<>();
        objectObjectHashMap.put("jobId", jobId);
        return new ResponseEntity<>(objectObjectHashMap, HttpStatus.OK);
    }

    @GetMapping("/text-details")
    public ResponseEntity<HashMap<String, String>> getTextDetails(@RequestParam("jobId") String jobId) {
        final Float processingResult = jobService.getProcessingResult(jobId);
        final HashMap<String, String> objectObjectHashMap = new HashMap<>();

        if (processingResult == null) {
            objectObjectHashMap.put("status", String.valueOf(JobStatus.PROCESSING).toLowerCase());
        } else {
            objectObjectHashMap.put("status", String.valueOf(JobStatus.COMPLETED).toLowerCase());
            objectObjectHashMap.put("result", String.valueOf(processingResult));
        }

        return new ResponseEntity<>(objectObjectHashMap, HttpStatus.OK);
    }
}
