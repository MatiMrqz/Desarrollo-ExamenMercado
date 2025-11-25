package org.example.service;

import org.example.entity.DnaRecord;
import org.example.repository.DnaRecordRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyBoolean;
import static org.mockito.Mockito.*;

/**
 * Tests unitarios para MutantService
 * Total: 5 tests
 */
@ExtendWith(MockitoExtension.class)
class MutantServiceTest {

    @Mock
    private DnaRecordRepository dnaRecordRepository;

    @Mock
    private MutantDetector mutantDetector;

    @InjectMocks
    private MutantService mutantService;

    private String[] mutantDna;
    private String[] humanDna;

    @BeforeEach
    void setUp() {
        mutantDna = new String[]{
            "ATGCGA",
            "CAGTGC",
            "TTATGT",
            "AGAAGG",
            "CCCCTA",
            "TCACTG"
        };

        humanDna = new String[]{
            "ATGCGA",
            "CAGTGC",
            "TTATCT",
            "AGACGG",
            "GCGTCA",
            "TCACTG"
        };
    }

    @Test
    @DisplayName("Debe retornar true cuando DNA es mutante y guardarlo en BD")
    void testIsMutant_ReturnTrue_WhenDnaIsMutant() {
        // Arrange
        when(mutantDetector.isMutant(mutantDna)).thenReturn(true);
        when(dnaRecordRepository.findByHash(mutantDna)).thenReturn(null);
        doNothing().when(dnaRecordRepository).saveDnaRecord(any(String[].class), anyBoolean());

        // Act
        boolean result = mutantService.isMutant(mutantDna);

        // Assert
        assertTrue(result, "Debe retornar true para DNA mutante");
        verify(mutantDetector, times(1)).isMutant(mutantDna);
        verify(dnaRecordRepository, times(1)).saveDnaRecord(mutantDna, true);
    }

    @Test
    @DisplayName("Debe retornar false cuando DNA es humano y guardarlo en BD")
    void testIsMutant_ReturnFalse_WhenDnaIsHuman() {
        // Arrange
        when(mutantDetector.isMutant(humanDna)).thenReturn(false);
        when(dnaRecordRepository.findByHash(humanDna)).thenReturn(null);
        doNothing().when(dnaRecordRepository).saveDnaRecord(any(String[].class), anyBoolean());

        // Act
        boolean result = mutantService.isMutant(humanDna);

        // Assert
        assertFalse(result, "Debe retornar false para DNA humano");
        verify(mutantDetector, times(1)).isMutant(humanDna);
        verify(dnaRecordRepository, times(1)).saveDnaRecord(humanDna, false);
    }

    @Test
    @DisplayName("Debe usar caché cuando DNA ya fue analizado")
    void testIsMutant_UseCache_WhenDnaExists() {
        // Arrange
        DnaRecord existingRecord = DnaRecord.builder()
            .hash("somehash123")
            .isMutant(true)
            .build();

        when(dnaRecordRepository.findByHash(mutantDna)).thenReturn(existingRecord);

        // Act
        boolean result = mutantService.isMutant(mutantDna);

        // Assert
        assertTrue(result, "Debe retornar resultado del caché");
        verify(mutantDetector, never()).isMutant(any()); // No debe llamar al detector
        verify(dnaRecordRepository, never()).saveDnaRecord(any(), anyBoolean()); // No debe guardar nuevamente
    }

    @Test
    @DisplayName("Debe guardar DNA con isMutant=true cuando es mutante")
    void testIsMutant_SaveWithMutantTrue() {
        // Arrange
        when(mutantDetector.isMutant(mutantDna)).thenReturn(true);
        when(dnaRecordRepository.findByHash(mutantDna)).thenReturn(null);
        doNothing().when(dnaRecordRepository).saveDnaRecord(any(String[].class), anyBoolean());

        // Act
        mutantService.isMutant(mutantDna);

        // Assert
        verify(dnaRecordRepository).saveDnaRecord(mutantDna, true);
    }

    @Test
    @DisplayName("Debe guardar DNA con isMutant=false cuando es humano")
    void testIsMutant_SaveWithMutantFalse() {
        // Arrange
        when(mutantDetector.isMutant(humanDna)).thenReturn(false);
        when(dnaRecordRepository.findByHash(humanDna)).thenReturn(null);
        doNothing().when(dnaRecordRepository).saveDnaRecord(any(String[].class), anyBoolean());

        // Act
        mutantService.isMutant(humanDna);

        // Assert
        verify(dnaRecordRepository).saveDnaRecord(humanDna, false);
    }
}

