package com.nicico.evaluation.controller;

import com.nicico.copper.common.domain.criteria.NICICOCriteria;
import com.nicico.copper.common.dto.grid.TotalResponse;
import com.nicico.evaluation.common.PageDTO;
import com.nicico.evaluation.dto.GroupDTO;
import com.nicico.evaluation.iservice.IGroupService;
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

@RestController
@RequestMapping("/anonymous/api/group")
@Api("Group Api")
@Validated
@AllArgsConstructor
public class GroupController {

    private final IGroupService groupService;

    @ApiOperation("list of group every page 10")
    @GetMapping(value = "/list")
    public ResponseEntity<PageDTO> list(@RequestParam(defaultValue = "0") Integer page, @RequestParam(defaultValue = "10") Integer pageSize) {
        Pageable pageable = PageRequest.of(page, pageSize, Sort.by(Sort.Direction.DESC, "id"));
        return new ResponseEntity<>(groupService.list(pageable), HttpStatus.OK);
    }

    @ApiOperation("search group")
    @GetMapping(value = "/spec-list")
    public ResponseEntity<TotalResponse<GroupDTO.Info>> search(@RequestParam MultiValueMap<String, String> request) {
        final NICICOCriteria nicicoCriteria = NICICOCriteria.of(request);
        return new ResponseEntity<>(groupService.search(nicicoCriteria), HttpStatus.OK);
    }

    @ApiOperation("get single group by id")
    @GetMapping(value = "/{id}")
    public ResponseEntity<GroupDTO.Info> get(@PathVariable Long id) {
        return new ResponseEntity<>(groupService.get(id), HttpStatus.OK);
    }

    @ApiOperation("create a group")
    @PostMapping
    public ResponseEntity<GroupDTO.Info> create(@Valid @RequestBody GroupDTO.Create request) {
        return new ResponseEntity<>(groupService.create(request), HttpStatus.CREATED);
    }

    @ApiOperation("update a group")
    @PutMapping
    public ResponseEntity<GroupDTO.Info> update(@Valid @RequestBody GroupDTO.Update request) {
        return new ResponseEntity<>(groupService.update(request), HttpStatus.OK);
    }

    @ApiOperation("delete a group by id")
    @DeleteMapping(value = {"/{id}"})
    public ResponseEntity<String> remove(@Validated @PathVariable Long id) {
        groupService.delete(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
