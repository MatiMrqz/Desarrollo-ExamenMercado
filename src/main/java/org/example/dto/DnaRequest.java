package org.example.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import org.example.validation.ValidDnaSequence;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DnaRequest {
    @NonNull
    @ValidDnaSequence
    private String[] dna;
}
