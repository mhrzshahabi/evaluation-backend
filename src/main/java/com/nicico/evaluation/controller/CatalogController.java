package com.nicico.evaluation.controller;

import com.nicico.copper.common.Loggable;
import com.nicico.evaluation.dto.CatalogDTO;
import com.nicico.evaluation.iservice.ICatalogService;
import com.nicico.evaluation.utility.EvaluationConstant;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/anonymous/api/catalog")
@Validated
@AllArgsConstructor
public class CatalogController {

    private final ICatalogService service;

    @GetMapping(value = "/{id}")
    public ResponseEntity<CatalogDTO.Info> get(@PathVariable Long id) {
        return new ResponseEntity<>(service.getById(id), HttpStatus.OK);
    }

    @Loggable
    @PostMapping
    public ResponseEntity create(@RequestBody CatalogDTO.Create create) {
        return new ResponseEntity<>(service.create(create), HttpStatus.OK);
    }

    @PutMapping
    public ResponseEntity<CatalogDTO.Info> update(@Valid @RequestBody CatalogDTO.Update request) {
        return new ResponseEntity<>(service.update(request), HttpStatus.OK);
    }

    @Loggable
    @DeleteMapping(value = "/{id}")
    public ResponseEntity delete(@PathVariable Long id) {
        service.delete(id);
        return new ResponseEntity(HttpStatus.OK);
    }

    @GetMapping(value = "/level-def-list")
    public ResponseEntity<List<CatalogDTO.Info>> list() {
        return new ResponseEntity<>(service.list(EvaluationConstant.LEVEL_DEF), HttpStatus.OK);
    }
}
