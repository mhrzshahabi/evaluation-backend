package com.nicico.evaluation.controller;

import com.nicico.copper.common.domain.criteria.NICICOCriteria;
import com.nicico.copper.common.dto.grid.TotalResponse;
import com.nicico.evaluation.dto.GroupTypeDTO;
import com.nicico.evaluation.iservice.IGroupTypeService;
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
@Api(value = "Group Type")
@RestController
@RequestMapping(value = "/anonymous/api/group-type")
public class GroupTypeController {

    private final IGroupTypeService service;

    @GetMapping(value = "/{id}")
    public ResponseEntity<GroupTypeDTO.Info> get(@PathVariable Long id) throws Exception {
        return new ResponseEntity<>(service.get(id), HttpStatus.OK);
    }

    @GetMapping(value = "/list")
    public ResponseEntity<List<GroupTypeDTO.Info>> list() {
        return new ResponseEntity<>(service.list(), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<GroupTypeDTO.Info> create(@Valid @RequestBody GroupTypeDTO.Create request) {
        return new ResponseEntity<>(service.create(request), HttpStatus.CREATED);
    }

    @PutMapping
    public ResponseEntity<GroupTypeDTO.Info> update(@Valid @RequestBody GroupTypeDTO.Update request) {
        return new ResponseEntity<>(service.update(request), HttpStatus.OK);
    }

    @DeleteMapping(value = {"/{id}"})
    public ResponseEntity<String> remove(@Validated @PathVariable Long id) {
        service.delete(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping(value = "/spec-list")
    public ResponseEntity<TotalResponse<GroupTypeDTO.Info>> search(@RequestParam MultiValueMap<String, String> request) {
        final NICICOCriteria nicicoCriteria = NICICOCriteria.of(request);
        return new ResponseEntity<>(service.search(nicicoCriteria), HttpStatus.OK);
    }
}
