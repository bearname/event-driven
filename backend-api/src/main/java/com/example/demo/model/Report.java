package com.example.demo.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Report {
    private int textNumber = 0;
    private int highRankPart = 0;
    private float averageRank = 0;
}
