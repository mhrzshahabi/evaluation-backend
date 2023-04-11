package com.nicico.evaluation.controller;

import com.nicico.copper.common.Loggable;
import com.nicico.copper.common.dto.search.SearchDTO;
import com.nicico.evaluation.dto.CatalogDTO;
import com.nicico.evaluation.dto.FilterDTO;
import com.nicico.evaluation.iservice.ICatalogService;
import com.nicico.evaluation.utility.CriteriaUtil;
import com.nicico.evaluation.utility.EvaluationConstant;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/catalog")
@Validated
@AllArgsConstructor
public class CatalogController {

    private final ICatalogService service;

    @GetMapping(value = "/{id}")
    public ResponseEntity<CatalogDTO.Info> get(@PathVariable Long id) {
        return new ResponseEntity<>(service.getById(id), HttpStatus.OK);
    }

    @Loggable
    @GetMapping(value = "/list")
    public ResponseEntity<CatalogDTO.SpecResponse> list(@RequestParam int count, @RequestParam int startIndex) {
        return new ResponseEntity<>(service.list(count, startIndex), HttpStatus.OK);
    }

    @Loggable
    @PostMapping
    public ResponseEntity<CatalogDTO.Info> create(@RequestBody CatalogDTO.Create create) {
        return new ResponseEntity<>(service.create(create), HttpStatus.OK);
    }

    @PutMapping
    public ResponseEntity<CatalogDTO.Info> update(@Valid @RequestBody CatalogDTO.Update request) {
        return new ResponseEntity<>(service.update(request), HttpStatus.OK);
    }

    @Loggable
    @DeleteMapping(value = "/{id}")
    public ResponseEntity<String> delete(@PathVariable Long id) {
        service.delete(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping(value = "/level-def-list")
    public ResponseEntity<List<CatalogDTO.Info>> levelDefList() {
        return new ResponseEntity<>(service.levelDefList(EvaluationConstant.LEVEL_DEF), HttpStatus.OK);
    }



    /**
     * @param count      is the number of entity to every page
     * @param startIndex is the start Index in current page
     * @param criteria is the key value pair for criteria
     * @return TotalResponse<CatalogDTO.Info> is the list of groupInfo entity that match the criteria
     */
    @PostMapping(value = "/spec-list")
    public ResponseEntity<CatalogDTO.SpecResponse> search(@RequestParam(value = "startIndex", required = false, defaultValue = "0") Integer startIndex,
                                                        @RequestParam(value = "count", required = false, defaultValue = "30") Integer count,
                                                        @RequestBody List<FilterDTO> criteria) throws NoSuchFieldException, IllegalAccessException {
        SearchDTO.SearchRq request = CriteriaUtil.ConvertCriteriaToSearchRequest(criteria, count, startIndex);
        SearchDTO.SearchRs<CatalogDTO.Info> data = service.search(request);
        final CatalogDTO.Response response = new CatalogDTO.Response();
        final CatalogDTO.SpecResponse specRs = new CatalogDTO.SpecResponse();
        response.setData(data.getList())
                .setStartRow(startIndex)
                .setEndRow(startIndex + data.getList().size())
                .setTotalRows(data.getTotalCount().intValue());
        specRs.setResponse(response);
        return new ResponseEntity<>(specRs, HttpStatus.OK);
    }
}
