package com.nicico.evaluation.iservice;

import com.nicico.evaluation.dto.EvaluationPeriodPostDTO;
import com.nicico.evaluation.model.EvaluationPeriod;

import java.util.List;
import java.util.Set;


public interface IEvaluationPeriodPostService {

    void delete(Long id);

    void deleteByEvaluationPeriodId(Long evaluationPeriodId);

    void deleteByEvaluationPeriodIdAndPostCode(Long evaluationPeriodId, String postCode);

    List<EvaluationPeriodPostDTO.Info> createAll(EvaluationPeriod evaluationPeriod, Set<String> postCode);

    List<EvaluationPeriodPostDTO.PostInfoEvaluationPeriod> getAllByEvaluationPeriodId(Long evaluationPeriodId);
}
