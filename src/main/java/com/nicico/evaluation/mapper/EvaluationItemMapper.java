package com.nicico.evaluation.mapper;

import com.nicico.evaluation.dto.EvaluationItemDTO;
import com.nicico.evaluation.model.EvaluationItem;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Mappings;

import java.util.List;

@Mapper(componentModel = "spring")
public interface EvaluationItemMapper {

    EvaluationItem dtoCreateToEntity(EvaluationItemDTO.Create dto);

    @Mappings({
            @Mapping(target = "groupTypeMerit.meritComponent.meritComponentTypes", ignore = true),
            @Mapping(target = "postMeritComponent.meritComponent.meritComponentTypes", ignore = true)
    })
    EvaluationItemDTO.Info entityToDtoInfo(EvaluationItem entity);

    List<EvaluationItemDTO.Info> entityToDtoInfoList(List<EvaluationItem> entities);

    List<EvaluationItemDTO.MeritTupleDTO> entityToUpdateInfoDtoList(List<EvaluationItem> entities);

    @Mappings({
            @Mapping(target = "evaluationItemId", source = "id"),
            @Mapping(target = "meritComponent", source = "meritComponentAudit"),
            @Mapping(target = "meritComponent.id", source = "meritComponentAudit.auditId.id"),
            @Mapping(target = "meritComponent.title", source = "meritComponentAudit.title"),
            @Mapping(target = "meritComponent.meritComponentTypes", ignore = true),
            @Mapping(target = "groupTypeMerit.instanceGroupTypeMerits", ignore = true),
            @Mapping(target = "weight", source = "groupTypeMerit.weight"),
    })
    EvaluationItemDTO.MeritTupleDTO entityToUpdateInfoDto(EvaluationItem entity);

    List<EvaluationItemDTO.PostMeritTupleDTO> entityToPostMeritInfoDtoList(List<EvaluationItem> entities);

    @Mappings({
            @Mapping(target = "evaluationItemId", source = "id"),
            @Mapping(target = "postMeritId", source = "postMeritComponentId"),
            @Mapping(target = "meritComponent", source = "postMeritComponent.meritComponent"),
            @Mapping(target = "meritComponent.id", source = "postMeritComponent.meritComponent.id"),
            @Mapping(target = "meritComponent.title", source = "postMeritComponent.meritComponent.title"),
            @Mapping(target = "meritComponent.meritComponentTypes", ignore = true),
            @Mapping(target = "weight", source = "postMeritComponent.weight"),
    })
    EvaluationItemDTO.PostMeritTupleDTO entityToPostMeritInfo(EvaluationItem entity);

    List<EvaluationItemDTO.MeritTupleDTO> entityToMeritTupleInfoList(List<EvaluationItemDTO.PostMeritTupleDTO> postMeritTuple);

    EvaluationItemDTO.MeritTupleDTO entityToMeritTupleInfo(EvaluationItemDTO.PostMeritTupleDTO postMeritTuple);

    void update(@MappingTarget EvaluationItem entity, EvaluationItemDTO.Update dto);

}
