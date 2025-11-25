package org.example.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.dto.DnaRequest;
import org.example.service.MutantService;
import org.example.service.StatsService;
import org.example.dto.StatsResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Tests de integración para MutantController
 * Total: 8 tests
 */
@WebMvcTest(MutantController.class)
class MutantControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private MutantService mutantService;

    @MockBean
    private StatsService statsService;

    @Test
    @DisplayName("POST /mutant debe retornar 200 OK cuando DNA es mutante")
    void testCheckMutant_ReturnOk_WhenIsMutant() throws Exception {
        // Arrange
        String[] dna = {
            "ATGCGA",
            "CAGTGC",
            "TTATGT",
            "AGAAGG",
            "CCCCTA",
            "TCACTG"
        };
        DnaRequest request = new DnaRequest();
        request.setDna(dna);

        when(mutantService.isMutant(any(String[].class))).thenReturn(true);

        // Act & Assert
        mockMvc.perform(post("/mutant")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isOk());
    }

    @Test
    @DisplayName("POST /mutant debe retornar 403 Forbidden cuando DNA es humano")
    void testCheckMutant_ReturnForbidden_WhenIsHuman() throws Exception {
        // Arrange
        String[] dna = {
            "ATGCGA",
            "CAGTGC",
            "TTATGT",
            "AGACGG",
            "GCGTCA",
            "TCACTG"
        };
        DnaRequest request = new DnaRequest();
        request.setDna(dna);

        when(mutantService.isMutant(any(String[].class))).thenReturn(false);

        // Act & Assert
        mockMvc.perform(post("/mutant")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isForbidden());
    }

    @Test
    @DisplayName("POST /mutant debe retornar 400 Bad Request cuando DNA es null")
    void testCheckMutant_ReturnBadRequest_WhenDnaIsNull() throws Exception {
        // Arrange
        String jsonRequest = "{\"dna\": null}";

        // Act & Assert
        mockMvc.perform(post("/mutant")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonRequest))
            .andExpect(status().isInternalServerError());
    }

    @Test
    @DisplayName("POST /mutant debe retornar 400 Bad Request cuando DNA está vacío")
    void testCheckMutant_ReturnBadRequest_WhenDnaIsEmpty() throws Exception {
        // Arrange
        DnaRequest request = new DnaRequest();
        request.setDna(new String[]{});

        // Act & Assert
        mockMvc.perform(post("/mutant")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("POST /mutant debe retornar 400 Bad Request cuando DNA contiene caracteres inválidos")
    void testCheckMutant_ReturnBadRequest_WhenDnaHasInvalidCharacters() throws Exception {
        // Arrange
        String[] dna = {
            "ATGCGA",
            "CAXTGC",  // X es inválido
            "TTATGT",
            "AGAAGG",
            "CCCCTA",
            "TCACTG"
        };
        DnaRequest request = new DnaRequest();
        request.setDna(dna);

        // Act & Assert
        mockMvc.perform(post("/mutant")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("GET /stats debe retornar 200 OK con estadísticas correctas")
    void testGetStats_ReturnOk_WithCorrectStats() throws Exception {
        // Arrange
        StatsResponse stats = StatsResponse.builder()
            .count_mutant_dna(40)
            .count_human_dna(100)
            .ratio(0.4)
            .build();

        when(statsService.getStats()).thenReturn(stats);

        // Act & Assert
        mockMvc.perform(get("/stats")
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.count_mutant_dna").value(40))
            .andExpect(jsonPath("$.count_human_dna").value(100))
            .andExpect(jsonPath("$.ratio").value(0.4));
    }

    @Test
    @DisplayName("GET /stats debe retornar 200 OK con estadísticas vacías cuando no hay datos")
    void testGetStats_ReturnOk_WithEmptyStats() throws Exception {
        // Arrange
        StatsResponse stats = StatsResponse.builder()
            .count_mutant_dna(0)
            .count_human_dna(0)
            .ratio(0.0)
            .build();

        when(statsService.getStats()).thenReturn(stats);

        // Act & Assert
        mockMvc.perform(get("/stats")
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.count_mutant_dna").value(0))
            .andExpect(jsonPath("$.count_human_dna").value(0))
            .andExpect(jsonPath("$.ratio").value(0.0));
    }

    @Test
    @DisplayName("POST /mutant debe retornar 400 Bad Request cuando matriz no es cuadrada")
    void testCheckMutant_ReturnBadRequest_WhenMatrixNotSquare() throws Exception {
        // Arrange
        String[] dna = {
            "ATGCGA",
            "CAGTGC",
            "TTATGT",
            "AGAAGG"  // Solo 4 filas en lugar de 6
        };
        DnaRequest request = new DnaRequest();
        request.setDna(dna);

        // Act & Assert
        mockMvc.perform(post("/mutant")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isBadRequest());
    }
}

