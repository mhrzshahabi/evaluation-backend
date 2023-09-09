package com.nicico.evaluation.iservice;

import com.nicico.evaluation.dto.EvaluationDTO;
import com.nicico.evaluation.dto.WorkSpaceDTO;

import java.util.List;

public interface IWorkSpaceService {

    List<WorkSpaceDTO.Info> workSpaceList();

    List<Long> workSpaceDetail(String code);

    List<WorkSpaceDTO.Info> workSpaceAlarm(List<String> workSpaceCodeList);

    List<EvaluationDTO.EvaluationPeriodDashboard> evaluationPeriodListByUser();
}
