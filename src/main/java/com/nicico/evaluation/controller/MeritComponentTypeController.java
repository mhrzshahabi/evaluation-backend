package com.nicico.evaluation.controller;

import com.nicico.copper.common.dto.search.SearchDTO;
import com.nicico.evaluation.dto.FilterDTO;
import com.nicico.evaluation.dto.MeritComponentTypeDTO;
import com.nicico.evaluation.iservice.IMeritComponentTypeService;
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
@Api(value = "MeritComponent")
@RestController
@RequestMapping(value = "/api/merit-Component-type")
public class MeritComponentTypeController {

    private final IMeritComponentTypeService service;

    @GetMapping(value = "/{id}")
    public ResponseEntity<MeritComponentTypeDTO.Info> get(@PathVariable Long id) {
        return new ResponseEntity<>(service.get(id), HttpStatus.OK);
    }

    @GetMapping(value = "/list")
    public ResponseEntity<MeritComponentTypeDTO.SpecResponse> list(@RequestParam int count, @RequestParam int startIndex) {
        return new ResponseEntity<>(service.list(count, startIndex), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<MeritComponentTypeDTO.Info> create(@Valid @RequestBody MeritComponentTypeDTO.Create request) {
        return new ResponseEntity<>(service.create(request), HttpStatus.CREATED);
    }

    @PutMapping
    public ResponseEntity<MeritComponentTypeDTO.Info> update(@Valid @RequestBody MeritComponentTypeDTO.Update request) {
        return new ResponseEntity<>(service.update(request), HttpStatus.OK);
    }

    @DeleteMapping(value = {"/{id}"})
    public ResponseEntity<String> remove(@Validated @PathVariable Long id) {
        service.delete(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }


    /**
     * @param count      is the number of entity to every page
     * @param startIndex is the start Index in current page
     * @param criteria is the key value pair for criteria
     * @return TotalResponse<MeritComponentTypeDTO.Info> is the list of groupInfo entity that match the criteria
     */
    @PostMapping(value = "/spec-list")
    public ResponseEntity<MeritComponentTypeDTO.SpecResponse> search(@RequestParam(value = "startIndex", required = false, defaultValue = "0") Integer startIndex,
                                                                     @RequestParam(value = "count", required = false, defaultValue = "30") Integer count,
                                                                     @RequestBody List<FilterDTO> criteria) throws NoSuchFieldException, IllegalAccessException {
        SearchDTO.SearchRq request = CriteriaUtil.ConvertCriteriaToSearchRequest(criteria, count, startIndex);
        SearchDTO.SearchRs<MeritComponentTypeDTO.Info> data = service.search(request);
        final MeritComponentTypeDTO.Response response = new MeritComponentTypeDTO.Response();
        final MeritComponentTypeDTO.SpecResponse specRs = new MeritComponentTypeDTO.SpecResponse();
        response.setData(data.getList())
                .setStartRow(startIndex)
                .setEndRow(startIndex + data.getList().size())
                .setTotalRows(data.getTotalCount().intValue());
        specRs.setResponse(response);
        return new ResponseEntity(response, HttpStatus.OK);
    }
}
