package com.nicico.evaluation.controller;

import com.nicico.copper.common.domain.criteria.NICICOCriteria;
import com.nicico.copper.common.dto.grid.TotalResponse;
import com.nicico.evaluation.common.PageDTO;
import com.nicico.evaluation.dto.InstanceDTO;
import com.nicico.evaluation.iservice.IInstanceService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.MultiValueMap;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Min;

@RestController
@RequestMapping("/api/instance")
@Api("Instance Api")
@Validated
@AllArgsConstructor
public class InstanceController {

    private final IInstanceService instanceService;

    @ApiOperation("list of instance every page 10")
    @GetMapping(value = "/list")
    public ResponseEntity<PageDTO> list(@RequestParam(defaultValue = "0") Integer page, @RequestParam(defaultValue = "10") Integer pageSize) {
        Pageable pageable = PageRequest.of(page, pageSize, Sort.by(Sort.Direction.DESC, "id"));
        return new ResponseEntity<>(instanceService.list(pageable), HttpStatus.OK);
    }

    @ApiOperation("search instance")
    @GetMapping(value = "/spec-list")
    public ResponseEntity<TotalResponse<InstanceDTO.Info>> search(@RequestParam MultiValueMap<String, String> request) {
        final NICICOCriteria nicicoCriteria = NICICOCriteria.of(request);
        return new ResponseEntity<>(instanceService.search(nicicoCriteria), HttpStatus.OK);
    }
    @ApiOperation("get single instance by id")
    @GetMapping(value = "/{id}")
    public ResponseEntity<InstanceDTO.Info> get(@PathVariable @Min(1) Long id) {
        return new ResponseEntity<>(instanceService.get(id), HttpStatus.OK);
    }

    @ApiOperation("create a instance")
    @PostMapping
    public ResponseEntity<InstanceDTO.Info> create(@Valid @RequestBody InstanceDTO.Create request) {
        return new ResponseEntity<>(instanceService.create(request), HttpStatus.CREATED);
    }

    @ApiOperation("update a instance")
    @PutMapping
    public ResponseEntity<InstanceDTO.Info> update(@Valid @RequestBody InstanceDTO.Update request) {
        return new ResponseEntity<>(instanceService.update(request), HttpStatus.OK);
    }

    @ApiOperation("delete a instance")
    @DeleteMapping(value = {"/{id}"})
    public ResponseEntity<String> remove(@PathVariable @Min(1) Long id) {
        instanceService.delete(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

}
