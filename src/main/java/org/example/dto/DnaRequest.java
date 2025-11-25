package org.example.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import org.example.validation.ValidDnaSequence;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DnaRequest {
    @NonNull
    @ValidDnaSequence
    @Schema(description = "Array of strings representing the DNA sequence", example = "[\"GCTGCAT\", \"CAGGTCG\", \"TGATCGA\", \"AGTACTG\", \"GTCCAGT\", \"CAGTGCA\", \"TGCATGC\"]")
    private String[] dna;
}
