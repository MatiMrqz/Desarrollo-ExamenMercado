package org.example.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class MutantDetector {
    private final int DNA_COMPONENTS_REQUIRED = 4;
    private final int SEQUENCES_REQUIRED = 2;

    public boolean isMutant(String[] dna){
        if (dna == null || dna.length == 0) {
            log.warn("DNA sequence is null or empty");
            return false;
        }
        int n = dna.length;
        char[][] matrix = new char[n][n];
        // Convert the DNA array into a 2D matrix
        for (int i = 0; i < n; i++) {
            if(dna[i] == null){
                log.warn("DNA sequence is null or empty");
                return false;
            }
            if(!dna[i].matches("[ATCG]+")){
                log.warn("DNA sequence contains invalid characters: {}", dna[i]);
                return false;
            }
            matrix[i] = dna[i].toCharArray();
        }
        int sequencesFound = 0;
        // Check rows and columns
        for (int i = 0; i < n; i++) {
            sequencesFound += checkLineSequence(matrix[i]);
            if (sequencesFound >= SEQUENCES_REQUIRED) {
                return true;
            }
            sequencesFound += checkColumnSequence(matrix, i);
            if (sequencesFound >= SEQUENCES_REQUIRED) {
                return true;
            }
            sequencesFound += checkDownDiagonalSequence(matrix, i);
            if (sequencesFound >= SEQUENCES_REQUIRED) {
                return true;
            }
            sequencesFound += checkUpDiagonalSequence(matrix, i);
            if (sequencesFound >= SEQUENCES_REQUIRED) {
                return true;
            }
        }
        return false;
    }
    private int checkLineSequence(char[] line) {
        for (int i = 0; i <= line.length - DNA_COMPONENTS_REQUIRED; i++) {
            if (line[i] == line[i + 1] && line[i + 1] == line[i + 2] && line[i + 2] == line[i + 3]) {
                log.info("Found sequence: {}{}{}{} at index {}", line[i], line[i + 1], line[i + 2], line[i + 3], i);
                return 1;
            }
        }
        return 0;
    }

    private char[] columnExtractor(char[][] matrix, int col) {
        int n = matrix.length;
        char[] column = new char[n];
        for (int j = 0; j < n; j++) {
            column[j] = matrix[j][col];
        }
        return column;
    }

    private int checkColumnSequence(char[][] matrix, int col) {
        char[] column = columnExtractor(matrix, col);
        return checkLineSequence(column);
    }

    private char[] downDiagonalExtractor(char[][] matrix, int i) {
        int n = matrix.length;
        int startIndex = n - DNA_COMPONENTS_REQUIRED - i;
        int endCol = (n - Math.abs(startIndex) -1);
        if (endCol+1 < DNA_COMPONENTS_REQUIRED) return null;
        char[] line = new char[endCol+1];
        if (startIndex > 0) {
            for (int j = 0; j <= endCol; j++) {
                line[j] = matrix[startIndex + j][j];
            }
        } else if (startIndex==0) {
            for (int j = 0; j < n; j++) {
                line[j] = matrix[j][j];
            }
        } else {
            for (int j = 0; j <= endCol; j++) {
                line[j] = matrix[j][(-startIndex) + j];
            }
        }
        return line;
    }

    private int checkDownDiagonalSequence(char[][] matrix, int i) {
        char[] line = downDiagonalExtractor(matrix, i);
        return line != null ? checkLineSequence(line) : 0;
    }

    private char[] upDiagonalExtractor(char[][] matrix, int i) {
        int n = matrix.length;
        int startIndex = n - DNA_COMPONENTS_REQUIRED - i;
        int endCol = (n - Math.abs(startIndex) - 1);
        if (endCol + 1 < DNA_COMPONENTS_REQUIRED) return null;
        char[] line = new char[endCol + 1];
        if (startIndex > 0) {
            for (int j = 0; j <= endCol; j++) {
                line[j] = matrix[startIndex + j][n - 1 - j];
            }
        } else if (startIndex == 0) {
            for (int j = 0; j < n; j++) {
                line[j] = matrix[j][n - 1 - j];
            }
        } else {
            for (int j = 0; j <= endCol; j++) {
                line[j] = matrix[j][n - 1 - (-startIndex) - j];
            }
        }
        return line;
    }

    private int checkUpDiagonalSequence(char[][] matrix, int i) {
        char[] line = upDiagonalExtractor(matrix, i);
        return line != null ? checkLineSequence(line) : 0;
    }
}
