package com.nicico.evaluation.iservice;

import com.nicico.evaluation.dto.EvaluationPeriodPostDTO;

import java.util.List;
import java.util.Set;


public interface IEvaluationPeriodPostService {

    void deleteByEvaluationPeriodId(Long evaluationPeriodId);
    List<EvaluationPeriodPostDTO.Info> createAll(Long evaluationPeriodId, Set<String> postCodes);
    List<EvaluationPeriodPostDTO.PostInfoEvaluationPeriod> getAllByEvaluationPeriodId(Long evaluationPeriodId);
}
