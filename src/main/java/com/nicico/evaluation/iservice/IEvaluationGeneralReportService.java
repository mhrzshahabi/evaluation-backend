package com.nicico.evaluation.iservice;

import com.nicico.copper.common.dto.search.SearchDTO;
import com.nicico.evaluation.dto.EvaluationDTO;
import com.nicico.evaluation.dto.EvaluationGeneralReportDTO;


public interface IEvaluationGeneralReportService {

    SearchDTO.SearchRs<EvaluationGeneralReportDTO.Info> search(SearchDTO.SearchRq request) throws IllegalAccessException, NoSuchFieldException;

    SearchDTO.SearchRs<EvaluationGeneralReportDTO.Info> searchByParent(SearchDTO.SearchRq request) throws IllegalAccessException, NoSuchFieldException;

    SearchDTO.SearchRs<EvaluationGeneralReportDTO.Info> searchEvaluationComprehensive(SearchDTO.SearchRq request, int count, int startIndex);

    SearchDTO.SearchRs<EvaluationGeneralReportDTO.DetailInfo> searchEvaluationGeneralReportDetail(SearchDTO.SearchRq request, int count, int startIndex);

    SearchDTO.SearchRs<EvaluationGeneralReportDTO.Info> searchByPermission(SearchDTO.SearchRq request, int count, int startIndex) throws IllegalAccessException, NoSuchFieldException;

    SearchDTO.SearchRs<EvaluationDTO.CostCenterInfo> searchByCostCenter(SearchDTO.SearchRq request, int count, int startIndex) throws IllegalAccessException, NoSuchFieldException;

    SearchDTO.SearchRs<EvaluationDTO.CostCenterInfo> searchAdminByCostCenter(SearchDTO.SearchRq request, Long statusCatalogId, int count, int startIndex);

    SearchDTO.SearchRs<EvaluationDTO.CostCenterInfo> searchFirstLevelByCostCenter(SearchDTO.SearchRq request, Long statusCatalogId, int count, int startIndex);

    SearchDTO.SearchRs<EvaluationDTO.CostCenterInfo> searchLastLevelByCostCenter(SearchDTO.SearchRq request, Long statusCatalogId, int count, int startIndex);

}
