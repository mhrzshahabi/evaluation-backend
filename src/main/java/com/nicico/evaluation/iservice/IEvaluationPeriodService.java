package com.nicico.evaluation.iservice;

import com.nicico.copper.common.dto.search.SearchDTO;
import com.nicico.evaluation.dto.EvaluationPeriodDTO;

public interface IEvaluationPeriodService {

    EvaluationPeriodDTO.Info get(Long id);

    EvaluationPeriodDTO.SpecResponse list(int count, int startIndex);

    SearchDTO.SearchRs<EvaluationPeriodDTO.Info> search(SearchDTO.SearchRq request) throws IllegalAccessException, NoSuchFieldException;

    EvaluationPeriodDTO.Info create(EvaluationPeriodDTO.Create dto);

    EvaluationPeriodDTO.Info update(Long id, EvaluationPeriodDTO.Update dto);

    void delete(Long id);
}
