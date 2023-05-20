package com.nicico.evaluation.mapper;

import com.nicico.evaluation.dto.EvaluationItemInstanceDTO;
import com.nicico.evaluation.model.EvaluationItemInstance;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Mappings;

import java.util.List;

@Mapper(componentModel = "spring")
public interface EvaluationItemInstanceMapper {

    EvaluationItemInstance dtoCreateToEntity(EvaluationItemInstanceDTO.Create dto);

    @Mappings({
            @Mapping(target = "evaluationItem.groupTypeMerit.meritComponent.meritComponentTypes", ignore = true),
            @Mapping(target = "postMeritInstance.postMeritComponent.meritComponent.meritComponentTypes", ignore = true),
            @Mapping(target = "evaluationItem.postMeritComponent.meritComponent.meritComponentTypes", ignore = true),
            @Mapping(target = "evaluationItem.evaluation", ignore = true)
    })
    EvaluationItemInstanceDTO.Info entityToDtoInfo(EvaluationItemInstance entity);

    List<EvaluationItemInstanceDTO.Info> entityToDtoInfoList(List<EvaluationItemInstance> entities);

    void update(@MappingTarget EvaluationItemInstance entity, EvaluationItemInstanceDTO.Update dto);

    @Mappings({
            @Mapping(target = "id", source = "instance.id"),
            @Mapping(target = "title", source = "instance.title"),
    })
    EvaluationItemInstanceDTO.InstanceTupleDTO entityToTupleDTOInfo(EvaluationItemInstance entity);
}
