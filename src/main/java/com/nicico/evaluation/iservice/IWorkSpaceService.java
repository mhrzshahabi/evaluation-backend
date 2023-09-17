package com.nicico.evaluation.iservice;

import com.nicico.evaluation.dto.EvaluationDTO;
import com.nicico.evaluation.dto.WorkSpaceDTO;

import java.util.List;

public interface IWorkSpaceService {

    List<WorkSpaceDTO.Info> workSpaceList();

    List<Long> workSpaceDetail(String code);

    List<WorkSpaceDTO.Info> workSpaceAlarm(List<String> workSpaceCodeList);

    EvaluationDTO.SpecResponse evaluationPeriodListByUser(int count, int startIndex);

    EvaluationDTO.EvaluationAverageScoreData evaluationAverageScoreDataByUser(Long evaluationPeriodId);

    List<EvaluationDTO.MostParticipationInFinalizedEvaluation> mostParticipationPerOmoor(Long evaluationPeriodId);
}
