package com.nicico.evaluation.controller;

import com.nicico.copper.common.dto.search.SearchDTO;
import com.nicico.evaluation.dto.FilterDTO;
import com.nicico.evaluation.dto.GroupGradeDTO;
import com.nicico.evaluation.iservice.IGroupGradeService;
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
@Api(value = "Group Grade")
@RestController
@RequestMapping(value = "/api/group-grade")
public class GroupGradeController {

    private final IGroupGradeService service;
    /**
     * @param id is the groupGrade id
     * @return GroupGradeDTO.Info is the single groupGrade entity
     */
    @GetMapping(value = "/{id}")
    public ResponseEntity<GroupGradeDTO.Info> get(@PathVariable Long id) {
        return new ResponseEntity<>(service.get(id), HttpStatus.OK);
    }

    /**
     * @param count      is the number of entity to every page
     * @param startIndex is the start Index in current page
     * @return GroupGradeDTO.SpecResponse that contain list of GroupGradeDTO and the number of total entity
     */
    @GetMapping(value = "/list")
    public ResponseEntity<GroupGradeDTO.SpecResponse> list(@RequestParam int count, @RequestParam int startIndex) {
        return new ResponseEntity<>(service.list(count, startIndex), HttpStatus.OK);
    }

    /**
     * @param request is the model of input for create groupGrade entity
     * @return GroupGradeDTO.Info is the saved groupGrade entity
     */
    @PostMapping
    public ResponseEntity<List<GroupGradeDTO.Info>> create(@Valid @RequestBody GroupGradeDTO.CreateAll request) {
        return new ResponseEntity<>(service.createGroupGrade(request), HttpStatus.CREATED);
    }

    /**
     * @param request is  the model of input for update groupGrade entity
     * @return GroupGradeDTO.Info is the updated groupGrade entity
     */
    @PutMapping(value = {"/{id}"})
    public ResponseEntity<List<GroupGradeDTO.Info>> update(@Valid @RequestBody GroupGradeDTO.CreateAll request, @PathVariable Long id) {
        return new ResponseEntity<>(service.update(id, request), HttpStatus.OK);
    }

    /**
     * @param id is the groupGrade id for delete
     * @return status code only
     */
    @DeleteMapping(value = {"/{id}"})
    public ResponseEntity<String> delete(@Validated @PathVariable Long id) {
        service.delete(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    /**
     * @param count      is the number of entity to every page
     * @param startIndex is the start Index in current page
     * @param criteria   is the key value pair for criteria
     * @return TotalResponse<GroupGradeDTO.Info> is the list of groupInfo entity that match the criteria
     */
    @PostMapping(value = "/spec-list")
    public ResponseEntity<GroupGradeDTO.SpecResponse> search(@RequestParam(value = "startIndex", required = false, defaultValue = "0") Integer startIndex,
                                                             @RequestParam(value = "count", required = false, defaultValue = "30") Integer count,
                                                             @RequestBody List<FilterDTO> criteria) throws NoSuchFieldException, IllegalAccessException {
        SearchDTO.SearchRq request = CriteriaUtil.ConvertCriteriaToSearchRequest(criteria, count, startIndex);
        SearchDTO.SearchRs<GroupGradeDTO.Info> data = service.search(request);
        final GroupGradeDTO.Response response = new GroupGradeDTO.Response();
        final GroupGradeDTO.SpecResponse specRs = new GroupGradeDTO.SpecResponse();
        response.setData(data.getList())
                .setStartRow(startIndex)
                .setEndRow(startIndex + data.getList().size())
                .setTotalRows(data.getTotalCount().intValue());
        specRs.setResponse(response);
        return new ResponseEntity<>(specRs, HttpStatus.OK);
    }

}
