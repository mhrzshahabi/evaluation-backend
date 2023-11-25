package com.nicico.evaluation.iservice;

import com.nicico.copper.common.dto.search.SearchDTO;
import com.nicico.evaluation.dto.EvaluationGeneralReportDTO;
import com.nicico.evaluation.dto.FilterDTO;
import com.nicico.evaluation.utility.ExcelGenerator;

import java.util.List;


public interface IEvaluationGeneralReportService {

    SearchDTO.SearchRs<EvaluationGeneralReportDTO.Info> search(SearchDTO.SearchRq request) throws IllegalAccessException, NoSuchFieldException;

    SearchDTO.SearchRs<EvaluationGeneralReportDTO.Info> searchByParent(SearchDTO.SearchRq request) throws IllegalAccessException, NoSuchFieldException;

    SearchDTO.SearchRs<EvaluationGeneralReportDTO.Info> searchEvaluationComprehensive(SearchDTO.SearchRq request, int count, int startIndex);

    SearchDTO.SearchRs<EvaluationGeneralReportDTO.DetailInfo> searchEvaluationGeneralReportDetail(SearchDTO.SearchRq request, int count, int startIndex);

    SearchDTO.SearchRs<EvaluationGeneralReportDTO.Info> searchByPermission(SearchDTO.SearchRq request, int count, int startIndex) throws IllegalAccessException, NoSuchFieldException;

    ExcelGenerator.ExcelDownload downloadExcel(List<FilterDTO> criteria) throws NoSuchFieldException, IllegalAccessException;

}
