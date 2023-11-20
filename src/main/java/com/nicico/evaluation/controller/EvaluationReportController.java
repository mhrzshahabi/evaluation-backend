package com.nicico.evaluation.controller;

import com.nicico.copper.common.dto.search.SearchDTO;
import com.nicico.evaluation.dto.EvaluationGeneralReportDTO;
import com.nicico.evaluation.dto.FilterDTO;
import com.nicico.evaluation.iservice.ICatalogService;
import com.nicico.evaluation.iservice.IEvaluationGeneralReportService;
import com.nicico.evaluation.utility.CriteriaUtil;
import io.swagger.annotations.Api;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/evaluation-report")
@Api("Evaluation Report Api")
@Validated
@Slf4j
@AllArgsConstructor
public class EvaluationReportController {

    private final IEvaluationGeneralReportService evaluationGeneralReportService;
    private final ICatalogService catalogService;
    private final ResourceBundleMessageSource messageSource;

    /**
     * @param count      is the number of entity to every page
     * @param startIndex is the start Index in current page
     * @param criteria   is the key value pair for criteria
     * @return TotalResponse<EvaluationGeneralReportDTO.Info> is the list of EvaluationInfo entity that match the criteria
     */
    @PostMapping(value = "/spec-list/by-parent")
    public ResponseEntity<EvaluationGeneralReportDTO.SpecResponse> searchByParent(@RequestParam(value = "startIndex", required = false, defaultValue = "0") Integer startIndex,
                                                                                  @RequestParam(value = "count", required = false) Integer count,
                                                                                  @RequestBody List<FilterDTO> criteria) throws NoSuchFieldException, IllegalAccessException {

        SearchDTO.SearchRq request = CriteriaUtil.ConvertCriteriaToSearchRequest(criteria, count, startIndex);
        SearchDTO.SearchRs<EvaluationGeneralReportDTO.Info> data = evaluationGeneralReportService.searchByParent(request);
        final EvaluationGeneralReportDTO.Response response = new EvaluationGeneralReportDTO.Response();
        final EvaluationGeneralReportDTO.SpecResponse specRs = new EvaluationGeneralReportDTO.SpecResponse();
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
     * @return TotalResponse<EvaluationGeneralReportDTO.Info> is the list of EvaluationInfo entity that match the criteria
     */
    @PostMapping(value = "/spec-list/evaluation-comprehensive")
    public ResponseEntity<EvaluationGeneralReportDTO.SpecResponse> searchEvaluationComprehensive(@RequestParam(value = "startIndex", required = false, defaultValue = "0") Integer startIndex,
                                                                                                 @RequestParam(value = "count", required = false) Integer count,
                                                                                                 @RequestBody List<FilterDTO> criteria) {
        SearchDTO.SearchRq request = CriteriaUtil.ConvertCriteriaToSearchRequest(criteria, count, startIndex);
        SearchDTO.SearchRs<EvaluationGeneralReportDTO.Info> data = evaluationGeneralReportService.searchEvaluationComprehensive(request, count, startIndex);
        final EvaluationGeneralReportDTO.Response response = new EvaluationGeneralReportDTO.Response();
        final EvaluationGeneralReportDTO.SpecResponse specRs = new EvaluationGeneralReportDTO.SpecResponse();
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
     * @return TotalResponse<EvaluationGeneralReportDTO.Info> is the list of EvaluationInfo entity that match the criteria
     */
    @PostMapping(value = "/spec-list/view/by-permisson")
    public ResponseEntity<EvaluationGeneralReportDTO.SpecResponse> searchViewByPermission(@RequestParam(value = "startIndex", required = false, defaultValue = "0") Integer startIndex,
                                                                                          @RequestParam(value = "count", required = false) Integer count,
                                                                                          @RequestBody List<FilterDTO> criteria) throws NoSuchFieldException, IllegalAccessException {
        SearchDTO.SearchRq request = CriteriaUtil.ConvertCriteriaToSearchRequest(criteria, count, startIndex);
        SearchDTO.SearchRs<EvaluationGeneralReportDTO.Info> data = evaluationGeneralReportService.searchByPermission(request, count, startIndex);
        final EvaluationGeneralReportDTO.Response response = new EvaluationGeneralReportDTO.Response();
        final EvaluationGeneralReportDTO.SpecResponse specRs = new EvaluationGeneralReportDTO.SpecResponse();
        response.setData(data.getList())
                .setStartRow(startIndex)
                .setEndRow(startIndex + data.getList().size())
                .setTotalRows(data.getTotalCount().intValue());
        specRs.setResponse(response);
        return new ResponseEntity<>(specRs, HttpStatus.OK);
    }

}

    