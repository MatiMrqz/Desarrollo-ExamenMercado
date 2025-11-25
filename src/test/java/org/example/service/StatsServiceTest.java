package org.example.service;

import org.example.dto.StatsResponse;
import org.example.repository.DnaRecordRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Tests unitarios para StatsService
 * Total: 6 tests
 */
@ExtendWith(MockitoExtension.class)
class StatsServiceTest {

    @Mock
    private DnaRecordRepository dnaRecordRepository;

    @InjectMocks
    private StatsService statsService;

    @Test
    @DisplayName("Debe retornar estadísticas correctas cuando hay mutantes y humanos")
    void testGetStats_WithMutantsAndHumans() {
        // Arrange
        when(dnaRecordRepository.getMutantCount()).thenReturn(40);
        when(dnaRecordRepository.getHumanCount()).thenReturn(100);

        // Act
        StatsResponse stats = statsService.getStats();

        // Assert
        assertNotNull(stats, "Stats no debe ser null");
        assertEquals(40, stats.getCount_mutant_dna(), "Debe contar 40 mutantes");
        assertEquals(100, stats.getCount_human_dna(), "Debe contar 100 humanos");
        assertEquals(0.4, stats.getRatio(), 0.001, "Ratio debe ser 0.4 (40/100)");

        verify(dnaRecordRepository, times(1)).getMutantCount();
        verify(dnaRecordRepository, times(1)).getHumanCount();
    }

    @Test
    @DisplayName("Debe retornar ratio 0.0 cuando no hay mutantes")
    void testGetStats_WithNoMutants() {
        // Arrange
        when(dnaRecordRepository.getMutantCount()).thenReturn(0);
        when(dnaRecordRepository.getHumanCount()).thenReturn(100);

        // Act
        StatsResponse stats = statsService.getStats();

        // Assert
        assertEquals(0, stats.getCount_mutant_dna(), "Debe contar 0 mutantes");
        assertEquals(100, stats.getCount_human_dna(), "Debe contar 100 humanos");
        assertEquals(0.0, stats.getRatio(), "Ratio debe ser 0.0");
    }

    @Test
    @DisplayName("Debe retornar ratio infinito cuando no hay humanos pero sí mutantes")
    void testGetStats_WithNoHumans() {
        // Arrange
        when(dnaRecordRepository.getMutantCount()).thenReturn(40);
        when(dnaRecordRepository.getHumanCount()).thenReturn(0);

        // Act
        StatsResponse stats = statsService.getStats();

        // Assert
        assertEquals(40, stats.getCount_mutant_dna(), "Debe contar 40 mutantes");
        assertEquals(0, stats.getCount_human_dna(), "Debe contar 0 humanos");
        assertTrue(Double.isInfinite(stats.getRatio()), "Ratio debe ser infinito");
    }

    @Test
    @DisplayName("Debe retornar estadísticas vacías cuando no hay datos")
    void testGetStats_WithNoData() {
        // Arrange
        when(dnaRecordRepository.getMutantCount()).thenReturn(0);
        when(dnaRecordRepository.getHumanCount()).thenReturn(0);

        // Act
        StatsResponse stats = statsService.getStats();

        // Assert
        assertEquals(0, stats.getCount_mutant_dna(), "Debe contar 0 mutantes");
        assertEquals(0, stats.getCount_human_dna(), "Debe contar 0 humanos");
        assertEquals(0.0, stats.getRatio(), "Ratio debe ser 0.0");
    }

    @Test
    @DisplayName("Debe calcular ratio correcto con más mutantes que humanos")
    void testGetStats_MoreMutantsThanHumans() {
        // Arrange
        when(dnaRecordRepository.getMutantCount()).thenReturn(100);
        when(dnaRecordRepository.getHumanCount()).thenReturn(50);

        // Act
        StatsResponse stats = statsService.getStats();

        // Assert
        assertEquals(100, stats.getCount_mutant_dna(), "Debe contar 100 mutantes");
        assertEquals(50, stats.getCount_human_dna(), "Debe contar 50 humanos");
        assertEquals(2.0, stats.getRatio(), 0.001, "Ratio debe ser 2.0 (100/50)");
    }

    @Test
    @DisplayName("Debe calcular ratio correcto con valores grandes")
    void testGetStats_WithLargeNumbers() {
        // Arrange
        when(dnaRecordRepository.getMutantCount()).thenReturn(1000000);
        when(dnaRecordRepository.getHumanCount()).thenReturn(3000000);

        // Act
        StatsResponse stats = statsService.getStats();

        // Assert
        assertEquals(1000000, stats.getCount_mutant_dna(), "Debe contar 1,000,000 mutantes");
        assertEquals(3000000, stats.getCount_human_dna(), "Debe contar 3,000,000 humanos");
        assertEquals(0.333, stats.getRatio(), 0.001, "Ratio debe ser ~0.333");
    }
}

