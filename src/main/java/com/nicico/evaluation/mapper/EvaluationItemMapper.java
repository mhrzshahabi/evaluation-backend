package com.nicico.evaluation.mapper;

import com.nicico.evaluation.dto.EvaluationItemDTO;
import com.nicico.evaluation.model.EvaluationItem;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Mappings;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@Mapper(componentModel = "spring")
public abstract class EvaluationItemMapper {

    @Autowired
    MeritComponentMapper meritComponentMapper;

    public abstract EvaluationItem dtoCreateToEntity(EvaluationItemDTO.Create dto);

    @Mappings({
            @Mapping(target = "groupTypeMerit.meritComponent.meritComponentTypes", ignore = true),
            @Mapping(target = "postMeritComponent.meritComponent.meritComponentTypes", ignore = true)
    })
    public abstract EvaluationItemDTO.Info entityToDtoInfo(EvaluationItem entity);

    public abstract List<EvaluationItemDTO.Info> entityToDtoInfoList(List<EvaluationItem> entities);

    public abstract List<EvaluationItemDTO.MeritTupleDTO> entityToUpdateInfoDtoList(List<EvaluationItem> entities);

    @Mappings({
            @Mapping(target = "evaluationItemId", source = "id"),
            @Mapping(target = "groupTypeMeritId", source = "groupTypeMeritId"),
            @Mapping(target = "meritComponent", source = "groupTypeMerit.meritComponent"),
            @Mapping(target = "meritComponent.id", source = "groupTypeMerit.meritComponent.id"),
            @Mapping(target = "meritComponent.title", source = "groupTypeMerit.meritComponent.title"),
            @Mapping(target = "meritComponent.meritComponentTypes", ignore = true)
    })
    public abstract EvaluationItemDTO.MeritTupleDTO entityToUpdateInfoDto(EvaluationItem entity);

    public abstract List<EvaluationItemDTO.PostMeritTupleDTO> entityToPostMeritInfoDtoList(List<EvaluationItem> entities);

    @Mappings({
            @Mapping(target = "evaluationItemId", source = "id"),
            @Mapping(target = "postMeritId", source = "postMeritComponentId"),
            @Mapping(target = "meritComponent", source = "postMeritComponent.meritComponent"),
            @Mapping(target = "meritComponent.id", source = "postMeritComponent.meritComponent.id"),
            @Mapping(target = "meritComponent.title", source = "postMeritComponent.meritComponent.title"),
            @Mapping(target = "meritComponent.meritComponentTypes", ignore = true)
    })
    public abstract EvaluationItemDTO.PostMeritTupleDTO entityToPostMeritInfo(EvaluationItem entity);

    public abstract List<EvaluationItemDTO.MeritTupleDTO> entityToMeritTupleInfoList(List<EvaluationItemDTO.PostMeritTupleDTO> postMeritTuple);

    public abstract EvaluationItemDTO.MeritTupleDTO entityToMeritTupleInfo(EvaluationItemDTO.PostMeritTupleDTO postMeritTuple);

    public abstract void update(@MappingTarget EvaluationItem entity, EvaluationItemDTO.Update dto);
}
