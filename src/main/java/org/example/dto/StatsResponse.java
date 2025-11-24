package org.example.dto;

import lombok.Data;

@Data
public class StatsResponse {
    private int count_mutant_dna;
    private int count_human_dna;
    private double ratio;
}
