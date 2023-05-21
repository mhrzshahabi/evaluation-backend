package com.nicico.evaluation.iservice;

import com.nicico.copper.common.dto.search.SearchDTO;
import com.nicico.evaluation.dto.EvaluationDTO;
import com.nicico.evaluation.dto.FilterDTO;
import com.nicico.evaluation.utility.ExcelGenerator;

import java.util.List;

public interface IEvaluationService {

    ExcelGenerator.ExcelDownload downloadExcel(List<FilterDTO> criteria) throws NoSuchFieldException, IllegalAccessException;

    EvaluationDTO.SpecResponse list(int count, int startIndex);

    EvaluationDTO.Info get(Long id);

    EvaluationDTO.Info create(EvaluationDTO.Create dto);

    List<EvaluationDTO.Info> createList(List<EvaluationDTO.Create> dto);

    EvaluationDTO.Info update(Long id, EvaluationDTO.Update dto);

    EvaluationDTO.Info updateStatus(Long id);

    List<EvaluationDTO.Info> updateStatusAll(List<Long> ids, Long statusCatalogId);

    void delete(Long id);

    SearchDTO.SearchRs<EvaluationDTO.Info> search(SearchDTO.SearchRq request) throws IllegalAccessException, NoSuchFieldException;

}
