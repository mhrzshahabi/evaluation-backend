package com.nicico.evaluation.iservice;

import com.nicico.copper.common.dto.search.SearchDTO;
import com.nicico.evaluation.dto.EvaluationDTO;
import com.nicico.evaluation.model.Evaluation;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface IEvaluationService {

    EvaluationDTO.SpecResponse list(int count, int startIndex);

    EvaluationDTO.Info get(Long id);

    EvaluationDTO.Info getAllByPeriodIdAndAssessPostCode(Long periodId, String assessPostCode);

    Evaluation getById(Long id);

    EvaluationDTO.Info create(EvaluationDTO.Create dto);

    List<EvaluationDTO.Info> createList(List<EvaluationDTO.CreateList> dto);

    EvaluationDTO.Info update(Long id, EvaluationDTO.Update dto);

    void delete(Long id);

    SearchDTO.SearchRs<EvaluationDTO.Info> search(SearchDTO.SearchRq request) throws IllegalAccessException, NoSuchFieldException;

    EvaluationDTO.ErrorResponseDTO changeStatus(EvaluationDTO.ChangeStatusDTO changeStatusDTO);

    Boolean validatePosts(List<Long> evaluationIds);

    EvaluationDTO.SpecResponse getAllByAssessNationalCodeAndStatusCatalogId(String assessNationalCode, Long statusCatalogId, int count, int startIndex);

    EvaluationDTO.EvaluationAverageScoreData getEvaluationAverageScoreDataByAssessNationalCodeAndEvaluationPeriodId(String assessNationalCode, Long evaluationPeriodId);

    EvaluationDTO.EvaluationAverageScoreData getEvaluationAverageScoreDataByAssessorNationalCodeAndEvaluationPeriodId(String assessNationalCode, Long evaluationPeriodId);

    List<EvaluationDTO.MostParticipationInFinalizedEvaluation> mostParticipationInFinalizedEvaluationPerOmoor(Long evaluationPeriodId, Long finalizedStatusCatalogId);

    List<String> sendNotification();

    List<EvaluationDTO.AverageWeightDTO> getFinalizedAverageByGradeAndPeriodEvaluation(Long periodId);

    List<EvaluationDTO.BestAssessAverageScoreDTO> getBestAssessesByOmoor(int count, int startIndex, Long periodId);

    ResponseEntity<byte[]> downloadInvalidPostExcel(List<Long> evaluationIds) throws NoSuchFieldException, IllegalAccessException;

    Integer getNumberOfAssessorWorkInWorkSpace();

    List<Long> getAssessorWorkInWorkSpace();

    Integer getNumberOfAssessorWorkInWorkSpaceNotification(String token);
}
