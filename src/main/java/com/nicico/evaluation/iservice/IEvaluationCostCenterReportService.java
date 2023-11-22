package com.nicico.evaluation.iservice;

import com.nicico.copper.common.dto.search.SearchDTO;
import com.nicico.evaluation.dto.EvaluationDTO;
import com.nicico.evaluation.dto.FilterDTO;
import com.nicico.evaluation.utility.ExcelGenerator;

import java.util.List;

public interface IEvaluationCostCenterReportService {

    SearchDTO.SearchRs<EvaluationDTO.CostCenterInfo> searchByCostCenter(SearchDTO.SearchRq request, int count, int startIndex) throws IllegalAccessException, NoSuchFieldException;

    SearchDTO.SearchRs<EvaluationDTO.CostCenterInfo> searchAdminByCostCenter(SearchDTO.SearchRq request, Long statusCatalogId, int count, int startIndex);

    SearchDTO.SearchRs<EvaluationDTO.CostCenterInfo> searchFirstLevelByCostCenter(SearchDTO.SearchRq request, Long statusCatalogId, int count, int startIndex);

    SearchDTO.SearchRs<EvaluationDTO.CostCenterInfo> searchLastLevelByCostCenter(SearchDTO.SearchRq request, Long statusCatalogId, int count, int startIndex);

    ExcelGenerator.ExcelDownload downloadExcel(List<FilterDTO> criteria) throws NoSuchFieldException, IllegalAccessException;

}
