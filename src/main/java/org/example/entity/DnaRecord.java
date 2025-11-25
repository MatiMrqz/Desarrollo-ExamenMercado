package org.example.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.time.LocalDate;

@Setter
@Getter
@NoArgsConstructor
@SuperBuilder
@Entity
@Table(name = "dna_records", indexes = {
        @Index(name = "idx_dna_hash", columnList = "dna_hash"),
        @Index(name = "idx_is_mutant", columnList = "is_mutant")
})
public class DnaRecord {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Long id;
    @Column(name = "dna_hash", unique = true, nullable = false)
    private String hash;
    @Column(name= "is_mutant", nullable = false)
    private boolean isMutant;
    @Builder.Default
    @Column(name = "created_at", nullable = false)
    private LocalDate createdAt = LocalDate.now();
}
