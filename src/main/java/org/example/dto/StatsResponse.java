package org.example.dto;

import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class StatsResponse {
    private int count_mutant_dna;
    private int count_human_dna;
    private double ratio;
}
