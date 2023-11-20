package com.nicico.evaluation.iservice;

import com.nicico.copper.common.dto.search.SearchDTO;
import com.nicico.evaluation.dto.EvaluationDTO;

public interface IEvaluationGeneralReportService {

    SearchDTO.SearchRs<EvaluationDTO.Info> search(SearchDTO.SearchRq request) throws IllegalAccessException, NoSuchFieldException;

    SearchDTO.SearchRs<EvaluationDTO.Info> searchByParent(SearchDTO.SearchRq request) throws IllegalAccessException, NoSuchFieldException;

    SearchDTO.SearchRs<EvaluationDTO.Info> searchEvaluationComprehensive(SearchDTO.SearchRq request, int count, int startIndex);

    SearchDTO.SearchRs<EvaluationDTO.Info> searchByPermission(SearchDTO.SearchRq request, int count, int startIndex) throws IllegalAccessException, NoSuchFieldException;

}
