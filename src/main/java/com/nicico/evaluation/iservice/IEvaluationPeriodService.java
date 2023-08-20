package com.nicico.evaluation.iservice;

import com.nicico.copper.common.dto.search.SearchDTO;
import com.nicico.evaluation.dto.EvaluationDTO;
import com.nicico.evaluation.dto.EvaluationPeriodDTO;
import com.nicico.evaluation.dto.EvaluationPeriodPostDTO;
import com.nicico.evaluation.utility.BaseResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;

import java.text.ParseException;
import java.util.List;
import java.util.Set;

public interface IEvaluationPeriodService {

    EvaluationPeriodDTO.InfoWithPostInfoEvaluationPeriod getWithPostInfo(Long id);

    EvaluationPeriodDTO.Info get(Long id);

    List<EvaluationPeriodDTO.Info> getAllByDateAssessment();

    EvaluationPeriodDTO.SpecResponse list(int count, int startIndex);

    SearchDTO.SearchRs<EvaluationPeriodDTO.Info> search(SearchDTO.SearchRq request) throws IllegalAccessException, NoSuchFieldException;

    EvaluationPeriodDTO.Info create(EvaluationPeriodDTO.Create dto) throws ParseException;

    EvaluationPeriodDTO.Info update(Long id, EvaluationPeriodDTO.Update dto) throws ParseException;

    List<EvaluationPeriodPostDTO.Info> createEvaluationPeriodPost(Long id, Set<String> postCode);

    void deleteEvaluationPeriodPost(Long id, String postCode);

    void delete(Long id);

    BaseResponse changeStatus(EvaluationDTO.ChangeStatusDTO changeStatusDTO);

    Boolean validatePosts(Long evaluationPeriodId);

    ResponseEntity<byte[]> downloadExcel(Long evaluationPeriodId) throws NoSuchFieldException, IllegalAccessException;
}
