package com.nicico.evaluation.controller;

import com.nicico.copper.common.Loggable;
import com.nicico.evaluation.dto.CatalogTypeDTO;
import com.nicico.evaluation.iservice.ICatalogTypeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;


@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping(value = "/api/catalog-type")
public class CatalogTypeController {

    private final ICatalogTypeService catalogTypeService;

    @GetMapping(value = "/{id}")
    public ResponseEntity<CatalogTypeDTO.Info> get(@PathVariable Long id) {
        return new ResponseEntity<>(catalogTypeService.getById(id), HttpStatus.OK);
    }

    @GetMapping(value = "/list")
    public ResponseEntity<List<CatalogTypeDTO.Info>> list(@RequestParam int count, @RequestParam int startIndex) {
        return new ResponseEntity<>(catalogTypeService.list(), HttpStatus.OK);
    }

    @Loggable
    @PostMapping
    public ResponseEntity<CatalogTypeDTO.Info> create(@RequestBody CatalogTypeDTO.Create create) {
        return new ResponseEntity<>(catalogTypeService.create(create), HttpStatus.OK);
    }

    @Loggable
    @PutMapping
    public ResponseEntity<CatalogTypeDTO.Info> update(@Valid @RequestBody CatalogTypeDTO.Update request) {
        return new ResponseEntity<>(catalogTypeService.update(request), HttpStatus.OK);
    }

    @Loggable
    @DeleteMapping(value = "/{id}")
    public ResponseEntity<String> delete(@PathVariable Long id) {
        catalogTypeService.delete(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
