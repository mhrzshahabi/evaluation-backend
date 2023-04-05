package com.nicico.evaluation.controller;

import com.nicico.evaluation.dto.GroupDTO;
import com.nicico.evaluation.mapper.GroupMapper;
import com.nicico.evaluation.iservice.IGroupService;
import com.nicico.evaluation.model.Group;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import java.util.List;

@RestController
@RequestMapping("/rest/group")
@Validated
@AllArgsConstructor
public class GroupController {

    private final IGroupService groupService;

    @GetMapping(value = "/list")
    public ResponseEntity<List<GroupDTO.Info>> list() {
        return new ResponseEntity<>(groupService.list(), HttpStatus.OK);
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<GroupDTO.Info> get(@PathVariable Long id) {
        return new ResponseEntity<>(groupService.get(id), HttpStatus.OK);
    }


    @PostMapping
    public ResponseEntity<GroupDTO.Info> create(@Valid @RequestBody GroupDTO.Create request) {
        return new ResponseEntity<>(groupService.create(request), HttpStatus.CREATED);
    }

    @PutMapping
    public ResponseEntity<GroupDTO.Info> update(@Valid @RequestBody GroupDTO.Update request) {
        return new ResponseEntity<>(groupService.update(request), HttpStatus.OK);
    }

    @DeleteMapping(value = {"/{id}"})
    public ResponseEntity<String> remove(@Validated @PathVariable Long id) {
        groupService.delete(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }


}
