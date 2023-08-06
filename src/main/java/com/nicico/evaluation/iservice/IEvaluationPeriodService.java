package com.nicico.evaluation.iservice;

import com.nicico.copper.common.dto.search.SearchDTO;
import com.nicico.evaluation.dto.EvaluationDTO;
import com.nicico.evaluation.dto.EvaluationPeriodDTO;
import com.nicico.evaluation.dto.EvaluationPeriodPostDTO;
import com.nicico.evaluation.utility.BaseResponse;

import java.text.ParseException;
import java.util.List;
import java.util.Set;

public interface IEvaluationPeriodService {

    EvaluationPeriodDTO.InfoWithPostInfoEvaluationPeriod get(Long id);

    EvaluationPeriodDTO.SpecResponse list(int count, int startIndex);

    SearchDTO.SearchRs<EvaluationPeriodDTO.Info> search(SearchDTO.SearchRq request) throws IllegalAccessException, NoSuchFieldException;

    EvaluationPeriodDTO.Info create(EvaluationPeriodDTO.Create dto) throws ParseException;

    EvaluationPeriodDTO.Info update(Long id, EvaluationPeriodDTO.Update dto) throws ParseException;

    List<EvaluationPeriodPostDTO.Info> createEvaluationPeriodPost(Long id, Set<String> postCode);

    void deleteEvaluationPeriodPost(Long id, String postCode);

    void delete(Long id);

    BaseResponse changeStatus(EvaluationDTO.ChangeStatusDTO changeStatusDTO);
}
