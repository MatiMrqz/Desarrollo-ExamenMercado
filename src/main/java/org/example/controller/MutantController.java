package org.example.controller;

import jakarta.validation.Valid;
import org.example.dto.DnaRequest;
import org.example.service.MutantService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MutantController {
    private final MutantService service;

    //Inyeccion de dependencia a través del constructor
    public MutantController(MutantService service) {
        this.service = service;
    }

    @PostMapping("/mutant")
    public ResponseEntity isMutant(@Valid @RequestBody DnaRequest dnaRequest) {
         if(service.isMutant(dnaRequest.getDna())){
             return ResponseEntity.ok().build();
         } else {
             return ResponseEntity.status(403).build();
         }
    }

}
