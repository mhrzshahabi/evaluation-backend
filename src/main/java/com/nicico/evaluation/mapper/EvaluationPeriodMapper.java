package com.nicico.evaluation.mapper;

import com.nicico.evaluation.dto.EvaluationPeriodDTO;
import com.nicico.evaluation.model.EvaluationPeriod;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

import java.util.List;

@Mapper(componentModel = "spring")
public interface EvaluationPeriodMapper {
    EvaluationPeriod dtoCreateToEntity(EvaluationPeriodDTO.Create dto);

    EvaluationPeriodDTO.Info entityToDtoInfo(EvaluationPeriod entity);

    List<EvaluationPeriodDTO.Info> entityToDtoInfoList(List<EvaluationPeriod> entities);

    void update(@MappingTarget EvaluationPeriod entity, EvaluationPeriodDTO.Update dto);
}
