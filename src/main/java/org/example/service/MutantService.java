package org.example.service;

import lombok.extern.slf4j.Slf4j;
import org.example.entity.DnaRecord;
import org.example.repository.DnaRecordRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class MutantService {
    @Autowired
    private DnaRecordRepository dnaRecordRepository;
    @Autowired
    private MutantDetector mutantDetector = new MutantDetector();

    public boolean isMutant(String[] dna) {
        DnaRecord dnaRecord = this.dnaRecordRepository.findByHash(dna);
        if (dnaRecord != null) {
            return dnaRecord.isMutant();
        }
        boolean result = mutantDetector.isMutant(dna);
        log.info("isMutant: {}", result);
        dnaRecordRepository.saveDnaRecord(dna, result);
        log.info("DNA record saved successfully");
        return result;
    }

}
