package com.nicico.evaluation.controller;

import com.nicico.copper.common.domain.criteria.NICICOCriteria;
import com.nicico.copper.common.dto.grid.TotalResponse;
import com.nicico.evaluation.dto.GroupDTO;
import com.nicico.evaluation.iservice.IGroupService;
import io.swagger.annotations.Api;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.MultiValueMap;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/group")
@Api("Group Api")
@Validated
@AllArgsConstructor
public class GroupController {

    private final IGroupService service;

    /**
     * @param count is the number of entity to every page
     * @param startIndex is the start Index in current page
     * @return GroupDTO.SpecResponse that contain list of groupInfoDto and the number of total entity
     */
    @GetMapping(value = "/list")
    public ResponseEntity<GroupDTO.SpecResponse> list(@RequestParam int count, @RequestParam int startIndex) {
        return new ResponseEntity<>(service.list(count, startIndex), HttpStatus.OK);
    }

    /**
     *
     * @param request is the key value pair for criteria
     * @return TotalResponse<GroupDTO.Info> is the list of groupInfo entity that match the criteria
     */
    @GetMapping(value = "/spec-list")
    public ResponseEntity<TotalResponse<GroupDTO.Info>> search(@RequestParam MultiValueMap<String, String> request) {
        final NICICOCriteria nicicoCriteria = NICICOCriteria.of(request);
        return new ResponseEntity<>(service.search(nicicoCriteria), HttpStatus.OK);
    }

    /**
     *
     * @param id is the gorup id
     * @return GroupDTO.Info  is the single gorup entity
     */
    @GetMapping(value = "/{id}")
    public ResponseEntity<GroupDTO.Info> get(@PathVariable Long id) {
        return new ResponseEntity<>(service.get(id), HttpStatus.OK);
    }

    /**
     *
     * @param request is the model of input for create group entity
     * @return GroupDTOInfo is the saved group entity
     */
    @PostMapping
    public ResponseEntity<GroupDTO.Info> create(@Valid @RequestBody GroupDTO.Create request) {
        return new ResponseEntity<>(service.create(request), HttpStatus.CREATED);
    }

    /**
     *
     * @param request is  the model of input for update group entity
     * @return GroupDTOInfo is the updated group entity
     */
    @PutMapping
    public ResponseEntity<GroupDTO.Info> update(@Valid @RequestBody GroupDTO.Update request) {
        return new ResponseEntity<>(service.update(request), HttpStatus.OK);
    }

    /**
     *
     * @param id is the group id for delete
     * @return status code only
     */
    @DeleteMapping(value = {"/{id}"})
    public ResponseEntity<String> remove(@Validated @PathVariable Long id) {
        service.delete(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

}
