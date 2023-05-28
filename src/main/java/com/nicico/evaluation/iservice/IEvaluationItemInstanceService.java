package com.nicico.evaluation.iservice;

import com.nicico.copper.common.dto.search.SearchDTO;
import com.nicico.evaluation.dto.EvaluationItemInstanceDTO;
import com.nicico.evaluation.utility.BaseResponse;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface IEvaluationItemInstanceService {

    EvaluationItemInstanceDTO.Info get(Long id);

    List<EvaluationItemInstanceDTO.Info> getAllByEvaluationItemId(List<Long> evaluationItemId);

    EvaluationItemInstanceDTO.SpecResponse list(int count, int startIndex);

    SearchDTO.SearchRs<EvaluationItemInstanceDTO.Info> search(SearchDTO.SearchRq request) throws IllegalAccessException, NoSuchFieldException;

    EvaluationItemInstanceDTO.Info create(EvaluationItemInstanceDTO.Create dto);

    List<EvaluationItemInstanceDTO.Info> createAll(List<EvaluationItemInstanceDTO.Create> dto);

    BaseResponse batchCreate(EvaluationItemInstanceDTO.Create dto);

    EvaluationItemInstanceDTO.Info update(Long id, EvaluationItemInstanceDTO.Update dto);

    void delete(Long id);

    void deleteAll(List<Long> ids);
}
