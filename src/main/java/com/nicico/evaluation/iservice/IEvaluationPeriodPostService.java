package com.nicico.evaluation.iservice;

import com.nicico.copper.common.dto.search.SearchDTO;
import com.nicico.evaluation.dto.EvaluationPeriodPostDTO;
import com.nicico.evaluation.model.EvaluationPeriod;

import java.util.List;
import java.util.Set;


public interface IEvaluationPeriodPostService {

    void delete(Long id);

    void deleteByEvaluationPeriodId(Long evaluationPeriodId);

    List<EvaluationPeriodPostDTO.Info> getByEvaluationPeriodId(Long evaluationPeriodId);

    void deleteByEvaluationPeriodIdAndPostCode(Long evaluationPeriodId, String postCode);

    List<EvaluationPeriodPostDTO.Info> createAll(EvaluationPeriod evaluationPeriod, Set<String> postCode);

    List<EvaluationPeriodPostDTO.PostInfoEvaluationPeriod> getAllByEvaluationPeriodId(Long evaluationPeriodId);

    List<String> getUnUsedPostCodeByEvaluationPeriodId(Long evaluationPeriodId);

    SearchDTO.SearchRs<EvaluationPeriodPostDTO.Info> search(SearchDTO.SearchRq request) throws IllegalAccessException, NoSuchFieldException;
}
