package com.nicico.evaluation.controller;

import com.nicico.copper.common.dto.search.SearchDTO;
import com.nicico.evaluation.dto.FilterDTO;
import com.nicico.evaluation.dto.MeritComponentDTO;
import com.nicico.evaluation.iservice.IMeritComponentService;
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
@RequestMapping(value = "/api/merit-Component")
public class MeritComponentController {

    private final IMeritComponentService service;
    /**
     * @param id is the instance id
     * @return MeritComponentDTO.Info is the single instance entity
     */
    @GetMapping(value = "/{id}")
    public ResponseEntity<MeritComponentDTO.Info> get(@PathVariable Long id) {
        return new ResponseEntity<>(service.get(id), HttpStatus.OK);
    }

    /**
     * @param count      is the number of entity to every page
     * @param startIndex is the start Index in current page
     * @return MeritComponentDTO.SpecResponse that contain list of MeritComponentDTO and the number of total entity
     */
    @GetMapping(value = "/list")
    public ResponseEntity<MeritComponentDTO.SpecResponse> list(@RequestParam int count, @RequestParam int startIndex) {
        return new ResponseEntity<>(service.list(count, startIndex), HttpStatus.OK);
    }

    /**
     * @param request is the model of input for create instance entity
     * @return MeritComponentDTO.Info is the saved instance entity
     */
    @PostMapping
    public ResponseEntity<MeritComponentDTO.Info> create(@Valid @RequestBody MeritComponentDTO.Create request) {
        return new ResponseEntity<>(service.create(request), HttpStatus.CREATED);
    }

    /**
     * @param request is  the model of input for update instance entity
     * @return MeritComponentDTO.Info is the updated instance entity
     */
    @PutMapping
    public ResponseEntity<MeritComponentDTO.Info> update(@Valid @RequestBody MeritComponentDTO.Update request) {
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
     * @return TotalResponse<MeritComponentDTO.Info> is the list of groupInfo entity that match the criteria
     */
    @PostMapping(value = "/spec-list")
    public ResponseEntity<MeritComponentDTO.SpecResponse> search(@RequestParam(value = "startIndex", required = false, defaultValue = "0") Integer startIndex,
                                                                 @RequestParam(value = "count", required = false, defaultValue = "30") Integer count,
                                                                 @RequestBody List<FilterDTO> criteria) throws NoSuchFieldException, IllegalAccessException {
        SearchDTO.SearchRq request = CriteriaUtil.ConvertCriteriaToSearchRequest(criteria, count, startIndex);
        SearchDTO.SearchRs<MeritComponentDTO.Info> data = service.search(request);
        final MeritComponentDTO.Response response = new MeritComponentDTO.Response();
        final MeritComponentDTO.SpecResponse specRs = new MeritComponentDTO.SpecResponse();
        response.setData(data.getList())
                .setStartRow(startIndex)
                .setEndRow(startIndex + data.getList().size())
                .setTotalRows(data.getTotalCount().intValue());
        specRs.setResponse(response);
        return new ResponseEntity<>(specRs, HttpStatus.OK);
    }
}
