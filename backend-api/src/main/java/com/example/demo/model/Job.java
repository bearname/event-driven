package com.example.demo.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Job {
    private String id = "";
    private String description;

    public Job(String description) {
        this.description = description;
    }
}
