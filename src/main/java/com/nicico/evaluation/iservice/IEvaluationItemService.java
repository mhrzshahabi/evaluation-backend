package com.nicico.evaluation.iservice;

import com.nicico.copper.common.dto.search.SearchDTO;
import com.nicico.evaluation.dto.EvaluationItemDTO;

import java.util.List;

public interface IEvaluationItemService {

    EvaluationItemDTO.SpecResponse list(int count, int startIndex);

    EvaluationItemDTO.Info get(Long id);

    EvaluationItemDTO.Info create(EvaluationItemDTO.Create dto);

    List<EvaluationItemDTO.CreateItemInfo> getItemInfoByAssessPostCode(String assessPostCode);

    EvaluationItemDTO.Info update(Long id, EvaluationItemDTO.Update dto);

    void delete(Long id);

    SearchDTO.SearchRs<EvaluationItemDTO.Info> search(SearchDTO.SearchRq request) throws IllegalAccessException, NoSuchFieldException;

}

