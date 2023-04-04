package com.nicico.evaluation.controller;

import com.nicico.copper.common.domain.criteria.NICICOCriteria;
import com.nicico.copper.common.dto.grid.TotalResponse;
import com.nicico.evaluation.DTO.KPITypeDTO;
import com.nicico.evaluation.serviceImpl.KPITypeServiceImpl;
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
@Api(value = "KPI Type")
@RestController
@RequestMapping(value = "/rest/kpi-type")
public class KPITypeController {
    private final KPITypeServiceImpl service;

    @GetMapping(value = "/{id}")
    public ResponseEntity<KPITypeDTO.Info> get(@PathVariable Long id) throws Exception {
        return new ResponseEntity<>(service.get(id), HttpStatus.OK);
    }

    @GetMapping(value = "/list")
    public ResponseEntity<List<KPITypeDTO.Info>> list() {
        return new ResponseEntity<>(service.list(), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<KPITypeDTO.Info> create(@Valid @RequestBody KPITypeDTO.Create request) {
        return new ResponseEntity<>(service.create(request), HttpStatus.CREATED);

    }

    @PutMapping(value = "")
    public ResponseEntity<KPITypeDTO.Info> update(@Valid @RequestBody KPITypeDTO.Update request) {
        return new ResponseEntity<>(service.update(request), HttpStatus.OK);
    }

    @DeleteMapping(value = "")
    public ResponseEntity<String> remove(@Validated @RequestBody KPITypeDTO.Delete request) {
        service.delete(request);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping(value = "/spec-list")
    public ResponseEntity<TotalResponse<KPITypeDTO.Info>> search(@RequestParam MultiValueMap<String, String> request) {
        final NICICOCriteria nicicoCriteria = NICICOCriteria.of(request);
        return new ResponseEntity<>(service.search(nicicoCriteria), HttpStatus.OK);
    }
}
