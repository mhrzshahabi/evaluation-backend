package com.nicico.evaluation.controller;

import com.nicico.copper.common.dto.search.SearchDTO;
import com.nicico.evaluation.dto.FilterDTO;
import com.nicico.evaluation.dto.OrganizationTreeDTO;
import com.nicico.evaluation.iservice.IOrganizationTreeService;
import com.nicico.evaluation.utility.CriteriaUtil;
import io.swagger.annotations.Api;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@Api(value = "OrganizationTree")
@RestController
@RequestMapping(value = "/api/organization-tree")
public class OrganizationTreeController {
    private final IOrganizationTreeService service;

    @GetMapping(value = "/{id}")
    public ResponseEntity<List<OrganizationTreeDTO.InfoTree>> get(@PathVariable Long id) {
        return new ResponseEntity<>(service.get(id), HttpStatus.OK);
    }

    @GetMapping(value = "/get-detail/{id}")
    public ResponseEntity<OrganizationTreeDTO.InfoDetail> getDetail(@PathVariable Long id) {
        return new ResponseEntity<>(service.getDetail(id), HttpStatus.OK);
    }

    @GetMapping(value = "/list-tree")
    public ResponseEntity<List<OrganizationTreeDTO.InfoTree>> listTree(@RequestParam int count, @RequestParam int startIndex,
                                                                       @RequestParam Long orgStructureId,
                                                                       @RequestParam(defaultValue = "0") Long parentId) {
        return new ResponseEntity<>(service.listTree(count, startIndex, orgStructureId, parentId), HttpStatus.OK);
    }

    @PostMapping(value = "/spec-list/{orgStructureId}")
    public ResponseEntity<OrganizationTreeDTO.SpecResponse> search(@RequestParam(value = "startIndex", required = false, defaultValue = "0") Integer startIndex,
                                                                   @RequestParam(value = "count", required = false, defaultValue = "30") Integer count,
                                                                   @RequestParam Long orgStructureId,
                                                                   @RequestBody List<FilterDTO> criteria) throws NoSuchFieldException, IllegalAccessException {

        SearchDTO.SearchRq request = CriteriaUtil.ConvertCriteriaToSearchRequest(criteria, count, startIndex);

//        final SearchDTO.CriteriaRq orgStructureIdCriteriaRq = new SearchDTO.CriteriaRq()
//                .setOperator(EOperator.equals)
//                .setFieldName("orgStructureId")
//                .setValue(orgStructureId);

//        request.getCriteria().setCriteria(orgStructureIdCriteriaRq.getCriteria());
        SearchDTO.SearchRs<OrganizationTreeDTO.InfoTree> data = service.search(request);
        final OrganizationTreeDTO.Response response = new OrganizationTreeDTO.Response();
        final OrganizationTreeDTO.SpecResponse specRs = new OrganizationTreeDTO.SpecResponse();
        response.setData(data.getList())
                .setStartRow(startIndex)
                .setEndRow(startIndex + data.getList().size())
                .setTotalRows(data.getTotalCount().intValue());
        specRs.setResponse(response);
        return new ResponseEntity<>(specRs, HttpStatus.OK);
    }
}
