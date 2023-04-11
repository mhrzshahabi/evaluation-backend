package com.nicico.evaluation.controller;

import com.nicico.copper.common.dto.search.SearchDTO;
import com.nicico.evaluation.dto.FilterDTO;
import com.nicico.evaluation.dto.GroupTypeDTO;
import com.nicico.evaluation.dto.GroupTypeMeritDTO;
import com.nicico.evaluation.iservice.IGroupTypeMeritService;
import com.nicico.evaluation.utility.CriteriaUtil;
import io.swagger.annotations.Api;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RequiredArgsConstructor
@Api(value = "Group Type Merit")
@RestController
@RequestMapping(value = "/api/group-type-merit")
public class GroupTypeMeritController {

    private final IGroupTypeMeritService service;

    /**
     * @param id is the instance id
     * @return GroupTypeMeritDTO.Info is the single instance entity
     */
    @GetMapping(value = "/{id}")
    public ResponseEntity<GroupTypeMeritDTO.Info> get(@PathVariable Long id) {
        return new ResponseEntity<>(service.get(id), HttpStatus.OK);
    }

    /**
     * @param count      is the number of entity to every page
     * @param startIndex is the start Index in current page
     * @return GroupTypeMeritDTO.SpecResponse that contain list of GroupTypeMeritDTO and the number of total entity
     */
    @GetMapping(value = "/list")
    public ResponseEntity<GroupTypeMeritDTO.SpecResponse> list(@RequestParam int count, @RequestParam int startIndex) {
        return new ResponseEntity<>(service.list(count, startIndex), HttpStatus.OK);
    }

    /**
     * @param request is the model of input for create instance entity
     * @return GroupTypeMeritDTO.Info is the saved instance entity
     */
    @PostMapping
    public ResponseEntity<GroupTypeMeritDTO.Info> create(@Valid @RequestBody GroupTypeMeritDTO.Create request) {
        return new ResponseEntity<>(service.create(request), HttpStatus.CREATED);
    }

    /**
     * @param request is  the model of input for update instance entity
     * @return GroupTypeMeritDTO.Info is the updated instance entity
     */
    @PutMapping
    public ResponseEntity<GroupTypeMeritDTO.Info> update(@Valid @RequestBody GroupTypeMeritDTO.Update request) {
        return new ResponseEntity<>(service.update(request), HttpStatus.OK);
    }

    /**
     * @param id is the instance id for delete
     * @return status code only
     */
    @DeleteMapping(value = {"/{id}"})
    public ResponseEntity<String> remove(@Validated @PathVariable Long id) {
        service.delete(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }


    /**
     * @param count      is the number of entity to every page
     * @param startIndex is the start Index in current page
     * @param criteria   is the key value pair for criteria
     * @return TotalResponse<GroupTypeMeritDTO.Info> is the list of groupInfo entity that match the criteria
     */
    @PostMapping(value = "/spec-list")
    public ResponseEntity<GroupTypeMeritDTO.SpecResponse> search(@RequestParam(value = "startIndex", required = false, defaultValue = "0") Integer startIndex,
                                                            @RequestParam(value = "count", required = false, defaultValue = "30") Integer count,
                                                            @RequestBody List<FilterDTO> criteria) throws NoSuchFieldException, IllegalAccessException {
        SearchDTO.SearchRq request = CriteriaUtil.ConvertCriteriaToSearchRequest(criteria, count, startIndex);
        SearchDTO.SearchRs<GroupTypeMeritDTO.Info> data = service.search(request);
        final GroupTypeMeritDTO.Response response = new GroupTypeMeritDTO.Response();
        final GroupTypeMeritDTO.SpecResponse specRs = new GroupTypeMeritDTO.SpecResponse();
        response.setData(data.getList())
                .setStartRow(startIndex)
                .setEndRow(startIndex + data.getList().size())
                .setTotalRows(data.getTotalCount().intValue());
        specRs.setResponse(response);
        return new ResponseEntity(response, HttpStatus.OK);
    }

}
