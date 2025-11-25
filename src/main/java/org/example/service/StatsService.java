package org.example.service;

import org.example.dto.StatsResponse;
import org.example.repository.DnaRecordRepository;
import org.springframework.stereotype.Service;

@Service
public class StatsService {
    DnaRecordRepository dnaRecordRepository;
    public StatsService(DnaRecordRepository dnaRecordRepository) {
        this.dnaRecordRepository = dnaRecordRepository;
    }

    public StatsResponse getStats(){
        int mutantCount = dnaRecordRepository.getMutantCount();
        int humanCount = dnaRecordRepository.getHumanCount();
        double ratio = humanCount == 0 ? 1 : (double) mutantCount / humanCount;
        return StatsResponse.builder()
                .count_human_dna(humanCount)
                .count_mutant_dna(mutantCount)
                .ratio(ratio)
                .build();
    }
}
