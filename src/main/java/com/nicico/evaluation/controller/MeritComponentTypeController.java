package com.nicico.evaluation.controller;

import com.nicico.copper.common.domain.criteria.NICICOCriteria;
import com.nicico.copper.common.dto.grid.TotalResponse;
import com.nicico.evaluation.dto.MeritComponentTypeDTO;
import com.nicico.evaluation.iservice.IMeritComponentTypeService;
import io.swagger.annotations.Api;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.MultiValueMap;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RequiredArgsConstructor
@Api(value = "MeritComponent")
@RestController
@RequestMapping(value = "/api/merit-Component-type")
public class MeritComponentTypeController {

    private final IMeritComponentTypeService service;

    @GetMapping(value = "/{id}")
    public ResponseEntity<MeritComponentTypeDTO.Info> get(@PathVariable Long id)  {
        return new ResponseEntity<>(service.get(id), HttpStatus.OK);
    }

    @GetMapping(value = "/list")
    public ResponseEntity<List<MeritComponentTypeDTO.Info>> list() {
        return new ResponseEntity<>(service.list(), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<MeritComponentTypeDTO.Info> create(@Valid @RequestBody MeritComponentTypeDTO.Create request) {
        return new ResponseEntity<>(service.create(request), HttpStatus.CREATED);
    }

    @PutMapping
    public ResponseEntity<MeritComponentTypeDTO.Info> update(@Valid @RequestBody MeritComponentTypeDTO.Update request) {
        return new ResponseEntity<>(service.update(request), HttpStatus.OK);
    }

    @DeleteMapping(value = {"/{id}"})
    public ResponseEntity<String> remove(@Validated @PathVariable Long id) {
        service.delete(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping(value = "/spec-list")
    public ResponseEntity<TotalResponse<MeritComponentTypeDTO.Info>> search(@RequestParam MultiValueMap<String, String> request) {
        final NICICOCriteria nicicoCriteria = NICICOCriteria.of(request);
        return new ResponseEntity<>(service.search(nicicoCriteria), HttpStatus.OK);
    }
}
