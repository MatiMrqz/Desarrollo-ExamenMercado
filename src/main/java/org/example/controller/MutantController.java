package org.example.controller;

import jakarta.validation.Valid;
import org.example.dto.DnaRequest;
import org.example.dto.StatsResponse;
import org.example.service.MutantService;
import org.example.service.StatsService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MutantController {
    private final MutantService mutantService;
    private final StatsService statsService;

    //Inyeccion de dependencia a través del constructor
    public MutantController(MutantService mutantService, StatsService statsService) {
        this.mutantService = mutantService;
        this.statsService = statsService;
    }

    @PostMapping("/mutant")
    public ResponseEntity isMutant(@Valid @RequestBody DnaRequest dnaRequest) {

         if(mutantService.isMutant(dnaRequest.getDna())){
             return ResponseEntity.ok().build();
         } else {
             return ResponseEntity.status(403).build();
         }
    }

    @GetMapping("/stats")
    public ResponseEntity<StatsResponse> getStats() {
        StatsResponse stats = statsService.getStats();
        return ResponseEntity.ok(stats);
    }


}
