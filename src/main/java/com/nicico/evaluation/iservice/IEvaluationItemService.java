package com.nicico.evaluation.iservice;

import com.nicico.copper.common.dto.search.SearchDTO;
import com.nicico.evaluation.dto.EvaluationItemDTO;

import java.util.List;

public interface IEvaluationItemService {

    EvaluationItemDTO.SpecResponse list(int count, int startIndex);

    EvaluationItemDTO.Info get(Long id);

    List<EvaluationItemDTO.Info> getByEvalId(Long evaluationId);

    List<EvaluationItemDTO.PostMeritTupleDTO> getAllPostMeritByEvalId(Long evaluationId);

    List<EvaluationItemDTO.MeritTupleDTO> getAllGroupTypeMeritByEvalId(Long evaluationId);

    EvaluationItemDTO.Info create(EvaluationItemDTO.Create dto);

    List<EvaluationItemDTO.Info> createAll(List<EvaluationItemDTO.Create> requests);

    List<EvaluationItemDTO.CreateItemInfo> getInfoByAssessPostCodeForCreate(String assessPostCode);

    List<EvaluationItemDTO.CreateItemInfo> getInfoByEvaluationIdForUpdate(Long id);

    EvaluationItemDTO.Info update(EvaluationItemDTO.Update dto);

    List<EvaluationItemDTO.Info> updateAll(List<EvaluationItemDTO.Update> requests);

    void delete(Long id);

    SearchDTO.SearchRs<EvaluationItemDTO.Info> search(SearchDTO.SearchRq request) throws IllegalAccessException, NoSuchFieldException;

    void deleteAll(List<Long> ids);
}

