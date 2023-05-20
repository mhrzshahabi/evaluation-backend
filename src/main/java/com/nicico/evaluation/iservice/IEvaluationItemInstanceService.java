package com.nicico.evaluation.iservice;

import com.nicico.copper.common.dto.search.SearchDTO;
import com.nicico.evaluation.dto.EvaluationItemInstanceDTO;
import com.nicico.evaluation.utility.BaseResponse;

import java.util.List;

public interface IEvaluationItemInstanceService {

    EvaluationItemInstanceDTO.Info get(Long id);

    List<EvaluationItemInstanceDTO.Info> getAllByEvaluationItemId(Long evaluationItemId);

    EvaluationItemInstanceDTO.SpecResponse list(int count, int startIndex);

    SearchDTO.SearchRs<EvaluationItemInstanceDTO.Info> search(SearchDTO.SearchRq request) throws IllegalAccessException, NoSuchFieldException;

    EvaluationItemInstanceDTO.Info create(EvaluationItemInstanceDTO.Create dto);

    BaseResponse batchCreate(EvaluationItemInstanceDTO.Create dto);

    EvaluationItemInstanceDTO.Info update(Long id, EvaluationItemInstanceDTO.Update dto);

    void delete(Long id);

}
