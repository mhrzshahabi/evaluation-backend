package com.nicico.evaluation.mapper;

import com.nicico.evaluation.dto.EvaluationDTO;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface EvaluationCostCenterReportMapper {

    EvaluationDTO.CostCenterExcel infoToDtoExcel(EvaluationDTO.CostCenterInfo info);

    List<EvaluationDTO.CostCenterExcel> infoToDtoExcelList(List<EvaluationDTO.CostCenterInfo> infos);
}
