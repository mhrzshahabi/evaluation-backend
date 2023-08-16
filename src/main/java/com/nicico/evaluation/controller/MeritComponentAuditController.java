package com.nicico.evaluation.controller;

import com.nicico.copper.common.dto.search.SearchDTO;
import com.nicico.evaluation.dto.FilterDTO;
import com.nicico.evaluation.dto.MeritComponentAuditDTO;
import com.nicico.evaluation.dto.MeritComponentDTO;
import com.nicico.evaluation.dto.SearchRequestDTO;
import com.nicico.evaluation.iservice.IMeritComponentAuditService;
import com.nicico.evaluation.utility.CriteriaUtil;
import io.swagger.annotations.Api;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Api(value = "MeritComponentAudit")
@RestController
@RequestMapping(value = "/api/merit-Component-audit")
public class MeritComponentAuditController {

    private final IMeritComponentAuditService service;

    /**
     * @param count      is the number of entity to every page
     * @param startIndex is the start Index in current page
     * @param criteria   is the key value pair for criteria
     * @return TotalResponse<MeritComponentDTO.Info> is the list of meritComponent entity that match the criteria
     */
    @PostMapping(value = "/last-active-merit/spec-list")
    public ResponseEntity<MeritComponentDTO.SpecResponse> searchLastActiveMeritComponent(@RequestParam(value = "startIndex", required = false, defaultValue = "0") Integer startIndex,
                                                                                         @RequestParam(value = "count", required = false, defaultValue = "30") Integer count,
                                                                                         @RequestBody List<FilterDTO> criteria) {
        SearchDTO.SearchRq request = CriteriaUtil.ConvertCriteriaToSearchRequest(criteria, count, startIndex);
        List<SearchDTO.CriteriaRq> criteriaRqList = request.getCriteria().getCriteria();

        SearchRequestDTO searchRequestDTO = new SearchRequestDTO();
        List<SearchRequestDTO.SearchDataDTO> searchDataDTOList = new ArrayList<>();
        criteriaRqList.forEach(item -> {
            SearchRequestDTO.SearchDataDTO searchDataDTO = new SearchRequestDTO.SearchDataDTO();
            searchDataDTO.setFieldName(item.getFieldName());
            searchDataDTO.setValue(item.getValue().get(0));
            searchDataDTOList.add(searchDataDTO);
        });
        searchRequestDTO.setSearchDataDTOList(searchDataDTOList);

        SearchDTO.SearchRs<MeritComponentDTO.Info> data = service.searchLastActiveMeritComponent(startIndex, count, searchRequestDTO);
        final MeritComponentDTO.Response response = new MeritComponentDTO.Response();
        final MeritComponentDTO.SpecResponse specRs = new MeritComponentDTO.SpecResponse();
        response.setData(data.getList())
                .setStartRow(startIndex)
                .setEndRow(startIndex + data.getList().size())
                .setTotalRows(data.getTotalCount().intValue());
        specRs.setResponse(response);
        return new ResponseEntity<>(specRs, HttpStatus.OK);
    }

    /**
     * @param count      is the number of entity to every page
     * @param startIndex is the start Index in current page
     * @param criteria   is the key value pair for criteria
     * @return TotalResponse<MeritComponentAuditDTO.Info> is the list of groupInfo entity that match the criteria
     */
    @PostMapping(value = "/change-list/{id}")
    public ResponseEntity<MeritComponentAuditDTO.SpecResponse> changeList(@RequestParam(value = "startIndex", required = false, defaultValue = "0") Integer startIndex,
                                                                     @RequestParam(value = "count", required = false, defaultValue = "30") Integer count,
                                                                     @PathVariable Long id,
                                                                     @RequestBody List<FilterDTO> criteria) throws NoSuchFieldException, IllegalAccessException {
        SearchDTO.SearchRq request = CriteriaUtil.ConvertCriteriaToSearchRequest(criteria, count, startIndex);
        SearchDTO.SearchRs<MeritComponentAuditDTO.Info> data = service.getChangeList(request, id);
        final MeritComponentAuditDTO.Response response = new MeritComponentAuditDTO.Response();
        final MeritComponentAuditDTO.SpecResponse specRs = new MeritComponentAuditDTO.SpecResponse();
        response.setData(data.getList())
                .setStartRow(startIndex)
                .setEndRow(startIndex + data.getList().size())
                .setTotalRows(data.getTotalCount().intValue());
        specRs.setResponse(response);
        return new ResponseEntity<>(specRs, HttpStatus.OK);
    }

}
