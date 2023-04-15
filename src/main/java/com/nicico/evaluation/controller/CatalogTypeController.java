package com.nicico.evaluation.controller;

import com.nicico.copper.common.dto.search.SearchDTO;
import com.nicico.evaluation.dto.CatalogTypeDTO;
import com.nicico.evaluation.dto.FilterDTO;
import com.nicico.evaluation.iservice.ICatalogTypeService;
import com.nicico.evaluation.utility.CriteriaUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;


@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping(value = "/api/catalog-type")
public class CatalogTypeController {

    private final ICatalogTypeService service;

    /**
     * @param id is the CatalogType id
     * @return CatalogTypeDTO.Info is the single kPIType entity
     */
    @GetMapping(value = "/{id}")
    public ResponseEntity<CatalogTypeDTO.Info> get(@PathVariable Long id) {
        return new ResponseEntity<>(service.get(id), HttpStatus.OK);
    }

    /**
     * @param count      is the number of entity to every page
     * @param startIndex is the start Index in current page
     * @return CatalogTypeDTO.SpecResponse that contain list of CatalogTypeDTO and the number of total entity
     */
    @GetMapping(value = "/list")
    public ResponseEntity<CatalogTypeDTO.SpecResponse> list(@RequestParam int count, @RequestParam int startIndex) {
        return new ResponseEntity<>(service.list(count, startIndex), HttpStatus.OK);
    }

    /**
     * @param create is the model of input for create catalogType entity
     * @return CatalogTypeDTO.Info is the saved catalogType entity
     */
    @PostMapping
    public ResponseEntity<CatalogTypeDTO.Info> create(@RequestBody CatalogTypeDTO.Create create) {
        return new ResponseEntity<>(service.create(create), HttpStatus.OK);
    }

    /**
     * @param request is the model of input for update catalogType entity
     * @return CatalogTypeDTO.Info is the updated catalogType entity
     */
    @PutMapping
    public ResponseEntity<CatalogTypeDTO.Info> update(@Valid @RequestBody CatalogTypeDTO.Update request) {
        return new ResponseEntity<>(service.update(request), HttpStatus.OK);
    }

    /**
     * @param id is the catalogType id for delete
     * @return status code only
     */
    @DeleteMapping(value = "/{id}")
    public ResponseEntity<String> delete(@PathVariable Long id) {
        service.delete(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    /**
     * @param count      is the number of entity to every page
     * @param startIndex is the start Index in current page
     * @param criteria is the key value pair for criteria
     * @return TotalResponse<CatalogTypeDTO.Info> is the list of catalogTypeInfo entity that match the criteria
     */
    @PostMapping(value = "/spec-list")
    public ResponseEntity<CatalogTypeDTO.SpecResponse> search(@RequestParam(value = "startIndex", required = false, defaultValue = "0") Integer startIndex,
                                                              @RequestParam(value = "count", required = false, defaultValue = "30") Integer count,
                                                              @RequestBody List<FilterDTO> criteria) throws NoSuchFieldException, IllegalAccessException {
        SearchDTO.SearchRq request = CriteriaUtil.ConvertCriteriaToSearchRequest(criteria, count, startIndex);
        SearchDTO.SearchRs<CatalogTypeDTO.Info> data = service.search(request);
        final CatalogTypeDTO.Response response = new CatalogTypeDTO.Response();
        final CatalogTypeDTO.SpecResponse specRs = new CatalogTypeDTO.SpecResponse();
        response.setData(data.getList())
                .setStartRow(startIndex)
                .setEndRow(startIndex + data.getList().size())
                .setTotalRows(data.getTotalCount().intValue());
        specRs.setResponse(response);
        return new ResponseEntity<>(specRs, HttpStatus.OK);
    }
}
