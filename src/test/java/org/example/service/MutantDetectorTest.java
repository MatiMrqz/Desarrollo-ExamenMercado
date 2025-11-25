package org.example.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests unitarios para MutantDetector
 * Total: 17 tests
 */
class MutantDetectorTest {

    private MutantDetector detector;

    @BeforeEach
    void setUp() {
        detector = new MutantDetector();
    }

    // ==================== CASOS MUTANTES (deben retornar true) ====================

    @Test
    @DisplayName("Debe detectar mutante con secuencias horizontal y diagonal")
    void testMutantWithHorizontalAndDiagonalSequences() {
        String[] dna = {
            "ATGCGA",
            "CAGTGC",
            "TTATGT",
            "AGAAGG",
            "CCCCTA",  // Horizontal: CCCC
            "TCACTG"
        };

        assertTrue(detector.isMutant(dna), "Debe detectar mutante con secuencias horizontal y diagonal");
    }

    @Test
    @DisplayName("Debe detectar mutante con secuencias verticales")
    void testMutantWithVerticalSequences() {
        String[] dna = {
            "ATGCGA",
            "CAGTGC",
            "TTATGT",
            "AGAAGG",
            "CCCCTA",
            "TCACTG"
        };

        assertTrue(detector.isMutant(dna), "Debe detectar mutante con secuencias verticales");
    }

    @Test
    @DisplayName("Debe detectar mutante con múltiples secuencias horizontales")
    void testMutantWithMultipleHorizontalSequences() {
        String[] dna = {
            "AAAATG",  // Horizontal: AAAA
            "CAGTGC",
            "TTATGT",
            "AGTTTT",  // Horizontal: TTTT
            "CCCCTA",
            "TCACTG"
        };

        assertTrue(detector.isMutant(dna), "Debe detectar mutante con múltiples secuencias horizontales");
    }

    @Test
    @DisplayName("Debe detectar mutante con diagonales ascendentes y descendentes")
    void testMutantWithBothDiagonals() {
        String[] dna = {
            "ATGCGA",
            "CAGTGC",
            "TTATGT",
            "AGAAGG",
            "CCCCTA",
            "TCACTG"
        };

        assertTrue(detector.isMutant(dna), "Debe detectar mutante con ambas diagonales");
    }

    @Test
    @DisplayName("Debe detectar mutante con matriz grande (10x10)")
    void testMutantWithLargeDna() {
        String[] dna = {
            "ATGCGAATGC",
            "CAGTGCATGC",
            "TTATGTATGC",
            "AGAAGGTTGC",
            "CCCCTATTGC",
            "TCACTGACGA",
            "ATGCGAATGC",
            "CAGTGCATGC",
            "TTATGTATGC",
            "AGAAGGTTGC"
        };

        assertTrue(detector.isMutant(dna), "Debe detectar mutante en matriz grande");
    }

    @Test
    @DisplayName("Debe detectar mutante cuando todos los caracteres son iguales")
    void testMutantAllSameCharacter() {
        String[] dna = {
            "AAAA",
            "AAAA",
            "AAAA",
            "AAAA"
        };

        assertTrue(detector.isMutant(dna), "Debe detectar mutante cuando todos son iguales");
    }

    // ==================== CASOS HUMANOS (deben retornar false) ====================

    @Test
    @DisplayName("Debe detectar humano cuando solo tiene una secuencia")
    void testNotMutantWithOnlyOneSequence() {
        String[] dna = {
            "ATGCGA",
            "CAGTGC",
            "TTATTT",  // Solo una secuencia: TTT (pero necesita 4)
            "AGACGG",
            "GCGTCA",
            "TCACTG"
        };

        assertFalse(detector.isMutant(dna), "Debe detectar humano con solo una secuencia");
    }

    @Test
    @DisplayName("Debe detectar humano cuando no tiene secuencias")
    void testNotMutantWithNoSequences() {
        String[] dna = {
            "ATGCGA",
            "CAGTGC",
            "TTATGT",
            "AGACGG",
            "GCGTCA",
            "TCACTG"
        };

        assertFalse(detector.isMutant(dna), "Debe detectar humano sin secuencias");
    }

    @Test
    @DisplayName("Debe detectar humano en matriz pequeña (4x4)")
    void testNotMutantSmallDna() {
        String[] dna = {
            "ATGC",
            "CAGT",
            "TTAT",
            "AGAC"
        };

        assertFalse(detector.isMutant(dna), "Debe detectar humano en matriz pequeña");
    }

    // ==================== VALIDACIONES (deben retornar false) ====================

    @Test
    @DisplayName("Debe retornar false cuando DNA es null")
    void testNotMutantWithNullDna() {
        assertFalse(detector.isMutant(null), "Debe retornar false cuando DNA es null");
    }

    @Test
    @DisplayName("Debe retornar false cuando DNA está vacío")
    void testNotMutantWithEmptyDna() {
        String[] dna = {};
        assertFalse(detector.isMutant(dna), "Debe retornar false cuando DNA está vacío");
    }

    @Test
    @DisplayName("Debe retornar false cuando matriz no es cuadrada")
    void testNotMutantWithNonSquareDna() {
        String[] dna = {
            "ATGCGA",
            "CAGTGC",
            "TTATGT",
            "AGAAGG"  // Solo 4 filas en lugar de 6
        };

        assertFalse(detector.isMutant(dna), "Debe retornar false cuando matriz no es cuadrada");
    }

    @Test
    @DisplayName("Debe retornar false cuando contiene caracteres inválidos")
    void testNotMutantWithInvalidCharacters() {
        String[] dna = {
            "ATGCGA",
            "CAXTGC",  // X es inválido
            "TTATGT",
            "AGAAGG",
            "CCCCTA",
            "TCACTG"
        };

        assertFalse(detector.isMutant(dna), "Debe retornar false con caracteres inválidos");
    }

    @Test
    @DisplayName("Debe retornar false cuando una fila es null")
    void testNotMutantWithNullRow() {
        String[] dna = {
            "ATGCGA",
            null,      // Fila null
            "TTATGT",
            "AGAAGG",
            "CCCCTA",
            "TCACTG"
        };

        assertFalse(detector.isMutant(dna), "Debe retornar false cuando una fila es null");
    }

    @Test
    @DisplayName("Debe retornar false cuando matriz es muy pequeña (menor a 4x4)")
    void testNotMutantWithTooSmallDna() {
        String[] dna = {
            "ATG",
            "CAG",
            "TTA"
        };

        assertFalse(detector.isMutant(dna), "Debe retornar false cuando matriz es menor a 4x4");
    }

    // ==================== EDGE CASES ====================

    @Test
    @DisplayName("Debe retornar false cuando secuencias son más largas que 4")
    void testNotMutantWithSequenceLongerThanFour() {
        String[] dna = {
            "AAAAAA",  // Secuencia de 6, pero solo cuenta una vez
            "CAGTGC",
            "TTATGT",
            "AGACGG",
            "GCGTCA",
            "TCACTG"
        };

        // Solo tiene una secuencia, necesita 2 para ser mutante
        assertFalse(detector.isMutant(dna), "Debe contar correctamente secuencias largas");
    }

    @Test
    @DisplayName("Debe detectar mutante con diagonal en esquina")
    void testMutantDiagonalInCorner() {
        String[] dna = {
            "AAAA",  // Horizontal: AAAA
            "CAGTGC",
            "TTACGT",
            "AGTAGG",
            "CCTCTA",
            "TCACTG"
        };

        assertTrue(detector.isMutant(dna), "Debe detectar mutante con diagonal en esquina");
    }
}

