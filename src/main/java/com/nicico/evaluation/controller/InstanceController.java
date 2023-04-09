package com.nicico.evaluation.controller;

import com.nicico.copper.common.domain.criteria.NICICOCriteria;
import com.nicico.copper.common.dto.grid.TotalResponse;
import com.nicico.evaluation.common.PageDTO;
import com.nicico.evaluation.dto.InstanceDTO;
import com.nicico.evaluation.iservice.IInstanceService;
import io.swagger.annotations.Api;
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

    /**
     * @param  page is the page number
     * @param pageSize is the number of entity to every page
     * @return PageDTO that contain list of instanceInfoDto and the number of total entity
     */
    @GetMapping(value = "/list")
    public ResponseEntity<PageDTO> list(@RequestParam(defaultValue = "0") Integer page, @RequestParam(defaultValue = "10") Integer pageSize) {
        Pageable pageable = PageRequest.of(page, pageSize, Sort.by(Sort.Direction.DESC, "id"));
        return new ResponseEntity<>(instanceService.list(pageable), HttpStatus.OK);
    }

    /**
     *
     * @param request is the key value pair for criteria
     * @return TotalResponse<InstanceDTO.Info> is the list of instanceInfo entity that match the criteria
     */
    @GetMapping(value = "/spec-list")
    public ResponseEntity<TotalResponse<InstanceDTO.Info>> search(@RequestParam MultiValueMap<String, String> request) {
        final NICICOCriteria nicicoCriteria = NICICOCriteria.of(request);
        return new ResponseEntity<>(instanceService.search(nicicoCriteria), HttpStatus.OK);
    }

    /**
     *
     * @param id is the instance id
     * @return InstanceDTO.Info is the single instance entity
     */
    @GetMapping(value = "/{id}")
    public ResponseEntity<InstanceDTO.Info> get(@PathVariable @Min(1) Long id) {
        return new ResponseEntity<>(instanceService.get(id), HttpStatus.OK);
    }

    /**
     *
     * @param request is the model of input for create instance entity
     * @return GroupDTOInfo is the saved instance entity
     */
    @PostMapping
    public ResponseEntity<InstanceDTO.Info> create(@Valid @RequestBody InstanceDTO.Create request) {
        return new ResponseEntity<>(instanceService.create(request), HttpStatus.CREATED);
    }

    /**
     *
     * @param request is  the model of input for update instance entity
     * @return GroupDTOInfo is the updated instance entity
     */
    @PutMapping
    public ResponseEntity<InstanceDTO.Info> update(@Valid @RequestBody InstanceDTO.Update request) {
        return new ResponseEntity<>(instanceService.update(request), HttpStatus.OK);
    }

    /**
     *
     * @param id is the instance id for delete
     * @return status code only
     */
    @DeleteMapping(value = {"/{id}"})
    public ResponseEntity<String> remove(@PathVariable @Min(1) Long id) {
        instanceService.delete(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

}
