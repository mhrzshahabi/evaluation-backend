package com.nicico.evaluation.mapper;

import com.nicico.evaluation.dto.EvaluationDTO;
import com.nicico.evaluation.model.EvaluationView;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring", uses = SpecialCaseMapper.class)
public interface EvaluationViewMapper {

    EvaluationDTO.Info entityToDtoInfo(EvaluationView evaluationView);

    List<EvaluationDTO.Info> entityToDtoInfoList(List<EvaluationView> evaluationViews);
}
