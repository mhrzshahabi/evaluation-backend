package com.nicico.evaluation.iservice;

import com.nicico.copper.common.dto.search.SearchDTO;
import com.nicico.evaluation.dto.EvaluationDTO;
import com.nicico.evaluation.dto.EvaluationViewDTO;
import com.nicico.evaluation.dto.FilterDTO;
import com.nicico.evaluation.utility.ExcelGenerator;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface IEvaluationViewService {

    SearchDTO.SearchRs<EvaluationDTO.Info> search(SearchDTO.SearchRq request) throws IllegalAccessException, NoSuchFieldException;

    SearchDTO.SearchRs<EvaluationDTO.Info> searchByParent(SearchDTO.SearchRq request) throws IllegalAccessException, NoSuchFieldException;

    SearchDTO.SearchRs<EvaluationViewDTO.Info> searchEvaluationComprehensive(SearchDTO.SearchRq request, int count, int startIndex, String orderBy);

    ResponseEntity<byte[]> downloadExcelEvaluationComprehensive(List<FilterDTO> criteria);

    ExcelGenerator.ExcelDownload downloadExcelByParent(List<FilterDTO> criteria) throws NoSuchFieldException, IllegalAccessException;

    ExcelGenerator.ExcelDownload downloadExcel(List<FilterDTO> criteria) throws NoSuchFieldException, IllegalAccessException;
}
