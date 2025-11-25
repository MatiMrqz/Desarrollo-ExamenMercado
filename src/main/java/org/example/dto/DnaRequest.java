package org.example.dto;

import lombok.*;
import org.example.validation.ValidDnaSequence;

@Data
@RequiredArgsConstructor
public class DnaRequest {
    @NonNull
    @ValidDnaSequence
    private String[] dna;
}
