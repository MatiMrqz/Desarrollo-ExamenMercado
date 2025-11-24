package org.example.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ValidDnaSequenceValidator implements ConstraintValidator<ValidDnaSequence, String[]> {
    @Override
    public boolean isValid(String[] dna, ConstraintValidatorContext context) {
        int n = dna.length;
        if (n < 1) return false;
        log.debug("Validating DNA sequence of length: {}", n);
        for (String strand : dna) {
            if (strand.length() != n || !strand.matches("[ATCG]+")) {
                return false;
            }
        }
        return true;
    }
}
