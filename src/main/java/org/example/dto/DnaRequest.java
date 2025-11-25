package org.example.dto;

import lombok.*;
import org.example.validation.ValidDnaSequence;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DnaRequest {
    @NonNull
    @ValidDnaSequence
    private String[] dna;
}
