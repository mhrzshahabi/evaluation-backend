package com.nicico.evaluation.iservice;

import com.nicico.evaluation.dto.EvaluationPeriodPostDTO;
import com.nicico.evaluation.dto.PostDTO;

import java.util.List;
import java.util.Set;


public interface IEvaluationPeriodPostService {

    EvaluationPeriodPostDTO.Info get(Long id);
    void deleteByEvaluationPeriodId(Long evaluationPeriodId);
    List<EvaluationPeriodPostDTO.Info> createAll(Long evaluationPeriodId, Set<String> postCodes);
    List<EvaluationPeriodPostDTO.Info> getAllByPostCode(String postCode);
    List<PostDTO.InfoEvaluationPeriod> getAllByEvaluationPeriodId(Long evaluationPeriodId);
}
