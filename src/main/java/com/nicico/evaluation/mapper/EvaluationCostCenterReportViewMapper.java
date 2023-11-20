package com.nicico.evaluation.mapper;

import com.nicico.evaluation.dto.EvaluationDTO;
import com.nicico.evaluation.model.EvaluationCostCenterReportView;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface EvaluationCostCenterReportViewMapper {

    EvaluationDTO.CostCenterInfo entityToDtoInfo(EvaluationCostCenterReportView entity);

    EvaluationDTO.CostCenterExcel entityToDtoExcel(EvaluationCostCenterReportView entity);

}
