package com.nicico.evaluation.controller;

import com.nicico.evaluation.dto.GroupDTO;
import com.nicico.evaluation.dto.InstanceDTO;
import com.nicico.evaluation.iservice.IGroupService;
import com.nicico.evaluation.iservice.IInstanceService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import java.util.List;

@RestController
@RequestMapping("/api/instance")
@Validated
@AllArgsConstructor
public class InstatnceController {

    private final IInstanceService instanceService;

    @GetMapping(value = "/list")
    public ResponseEntity<List<InstanceDTO.Info>> list() {
        return new ResponseEntity<>(instanceService.list(), HttpStatus.OK);
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<InstanceDTO.Info> get(@PathVariable @Min(1) Long id) {
        return new ResponseEntity<>(instanceService.get(id), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<InstanceDTO.Info> create(@Valid @RequestBody InstanceDTO.Create request) {
        return new ResponseEntity<>(instanceService.create(request), HttpStatus.CREATED);
    }

    @PutMapping
    public ResponseEntity<InstanceDTO.Info> update(@Valid @RequestBody InstanceDTO.Update request) {
        return new ResponseEntity<>(instanceService.update(request), HttpStatus.OK);
    }

    @DeleteMapping(value = {"/{id}"})
    public ResponseEntity<String> remove(@PathVariable @Min(1) Long id) {
        instanceService.delete(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

}
