package com.nicico.evaluation.controller;

import com.nicico.copper.common.dto.search.SearchDTO;
import com.nicico.evaluation.dto.EvaluationDTO;
import com.nicico.evaluation.dto.EvaluationGeneralReportDTO;
import com.nicico.evaluation.dto.FilterDTO;
import com.nicico.evaluation.iservice.IEvaluationCostCenterReportViewService;
import com.nicico.evaluation.iservice.IEvaluationGeneralReportService;
import com.nicico.evaluation.iservice.IEvaluationViewService;
import com.nicico.evaluation.utility.CriteriaUtil;
import com.nicico.evaluation.utility.ExcelGenerator;
import io.swagger.annotations.Api;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
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

    private final IEvaluationViewService evaluationViewService;
    private final IEvaluationGeneralReportService evaluationGeneralReportService;
    private final IEvaluationCostCenterReportViewService evaluationCostCenterReportViewService;

    /**
     * @param count      is the number of entity to every page
     * @param startIndex is the start Index in current page
     * @param criteria   is the key value pair for criteria
     * @return TotalResponse<EvaluationDTO.Info> is the list of EvaluationInfo entity that match the criteria
     */
    @PostMapping(value = "/spec-list/by-parent")
    public ResponseEntity<EvaluationDTO.SpecResponse> searchByParent(@RequestParam(value = "startIndex", required = false, defaultValue = "0") Integer startIndex,
                                                                     @RequestParam(value = "count", required = false) Integer count,
                                                                     @RequestBody List<FilterDTO> criteria) throws NoSuchFieldException, IllegalAccessException {
        SearchDTO.SearchRq request = CriteriaUtil.ConvertCriteriaToSearchRequest(criteria, count, startIndex);
        SearchDTO.SearchRs<EvaluationDTO.Info> data = evaluationViewService.searchByParent(request);
        final EvaluationDTO.Response response = new EvaluationDTO.Response();
        final EvaluationDTO.SpecResponse specRs = new EvaluationDTO.SpecResponse();
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
     * @return TotalResponse<EvaluationDTO.Info> is the list of EvaluationInfo entity that match the criteria
     */
    @PostMapping(value = "/spec-list/comprehensive")
    public ResponseEntity<EvaluationDTO.SpecResponse> searchEvaluationComprehensive(@RequestParam(value = "startIndex", required = false, defaultValue = "0") Integer startIndex,
                                                                                    @RequestParam(value = "count", required = false) Integer count,
                                                                                    @RequestBody List<FilterDTO> criteria) {
        SearchDTO.SearchRq request = CriteriaUtil.ConvertCriteriaToSearchRequest(criteria, count, startIndex);
        SearchDTO.SearchRs<EvaluationDTO.Info> data = evaluationViewService.searchEvaluationComprehensive(request, count, startIndex);
        final EvaluationDTO.Response response = new EvaluationDTO.Response();
        final EvaluationDTO.SpecResponse specRs = new EvaluationDTO.SpecResponse();
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
    @PostMapping(value = "/spec-list/general-report")
    public ResponseEntity<EvaluationGeneralReportDTO.SpecResponse> searchGeneralView(@RequestParam(value = "startIndex", required = false, defaultValue = "0") Integer startIndex,
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

    /**
     * @param count      is the number of entity to every page
     * @param startIndex is the start Index in current page
     * @param criteria   is the key value pair for criteria
     * @return TotalResponse<EvaluationGeneralReportDTO.DetailInfo> is the list of EvaluationInfo entity that match the criteria
     */
    @PostMapping(value = "/spec-list/general-report/detail")
    public ResponseEntity<EvaluationGeneralReportDTO.SpecResponse> searchGeneralViewDetail(@RequestParam(value = "startIndex", required = false, defaultValue = "0") Integer startIndex,
                                                                                           @RequestParam(value = "count", required = false) Integer count,
                                                                                           @RequestBody List<FilterDTO> criteria) {
        SearchDTO.SearchRq request = CriteriaUtil.ConvertCriteriaToSearchRequest(criteria, count, startIndex);
        SearchDTO.SearchRs<EvaluationGeneralReportDTO.DetailInfo> data = evaluationGeneralReportService.searchEvaluationGeneralReportDetail(request, count, startIndex);
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
     * @return TotalResponse<EvaluationDTO.CostCenterInfo> is the list of EvaluationCostCenter entity that match the criteria
     */
    @PostMapping(value = "/spec-list/cost-center")
    public ResponseEntity<EvaluationDTO.SpecResponse> evaluationCostCenterReport(@RequestParam(value = "startIndex", required = false, defaultValue = "0") Integer startIndex,
                                                                                 @RequestParam(value = "count", required = false, defaultValue = "30") Integer count,
                                                                                 @RequestBody List<FilterDTO> criteria) throws NoSuchFieldException, IllegalAccessException {
        SearchDTO.SearchRq request = CriteriaUtil.ConvertCriteriaToSearchRequest(criteria, count, startIndex);
        SearchDTO.SearchRs<EvaluationDTO.CostCenterInfo> data = evaluationGeneralReportService.searchByCostCenter(request, count, startIndex);
        final EvaluationDTO.Response response = new EvaluationDTO.Response();
        final EvaluationDTO.SpecResponse specRs = new EvaluationDTO.SpecResponse();
        response.setData(data.getList())
                .setStartRow(startIndex)
                .setEndRow(startIndex + data.getList().size())
                .setTotalRows(data.getTotalCount().intValue());
        specRs.setResponse(response);
        return new ResponseEntity<>(specRs, HttpStatus.OK);
    }

    /**
     * @param criteria is the key value pair for criteria
     * @return byte[] is the Excel of EvaluationDTO.CostCenterExcel entity that match the criteria
     */
    @PostMapping(value = "/export-excel/cost-center")
    public ResponseEntity<byte[]> exportExcel(@RequestBody List<FilterDTO> criteria) throws NoSuchFieldException, IllegalAccessException {
        ExcelGenerator.ExcelDownload excelDownload = evaluationCostCenterReportViewService.downloadExcel(criteria);
        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(excelDownload.getContentType()))
                .header(HttpHeaders.CONTENT_DISPOSITION, excelDownload.getHeaderValue())
                .body(excelDownload.getContent());
    }

    /**
     * @param criteria is the key value pair for criteria
     * @return byte[] is the Excel of EvaluationDTO.CostCenterExcel entity that match the criteria
     */
    @PostMapping(value = "/export-excel/general-report")
    public ResponseEntity<byte[]> exportExcelGeneralReport(@RequestBody List<FilterDTO> criteria) {
        return evaluationGeneralReportService.downloadExcel(criteria);
    }

    /**
     * @param criteria is the key value pair for criteria
     * @return byte[] is the Excel of EvaluationDTOExcel that match the criteria
     */
    @SneakyThrows
    @PostMapping(value = "/export-excel-by-parent")
    public ResponseEntity<byte[]> exportExcelByParent(@RequestBody List<FilterDTO> criteria) {
        ExcelGenerator.ExcelDownload excelDownload = evaluationViewService.downloadExcelByParent(criteria);
        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(excelDownload.getContentType()))
                .header(HttpHeaders.CONTENT_DISPOSITION, excelDownload.getHeaderValue())
                .body(excelDownload.getContent());
    }

    /**
     * @param criteria is the key value pair for criteria
     * @return byte[] is the Excel of EvaluationDTOExcel that match the criteria
     */
    @PostMapping(value = "/export-excel-evaluation-comprehensive")
    public ResponseEntity<byte[]> exportExcelEvaluationComprehensive(@RequestBody List<FilterDTO> criteria) {
        return evaluationViewService.downloadExcelEvaluationComprehensive(criteria);
    }
}

    