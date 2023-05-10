package com.nicico.evaluation.mapper;

import com.nicico.evaluation.dto.EvaluationItemDTO;
import com.nicico.evaluation.model.EvaluationItem;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

import java.util.List;

@Mapper(componentModel = "spring")
public interface EvaluationItemMapper {

    EvaluationItem dtoCreateToEntity(EvaluationItemDTO.Create dto);

    EvaluationItemDTO.Info entityToDtoInfo(EvaluationItem entity);

    List<EvaluationItemDTO.Info> entityToDtoInfoList(List<EvaluationItem> entities);

    void update(@MappingTarget EvaluationItem entity, EvaluationItemDTO.Update dto);
}
