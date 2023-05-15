package com.nicico.evaluation.mapper;

import com.nicico.evaluation.dto.EvaluationItemDTO;
import com.nicico.evaluation.dto.PostMeritComponentDTO;
import com.nicico.evaluation.model.PostMeritComponent;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Mappings;

import java.util.List;

@Mapper(componentModel = "spring", uses = {MeritComponentMapper.class, PostMeritInstanceMapper.class})
public interface PostMeritComponentMapper {

    PostMeritComponent dtoCreateToEntity(PostMeritComponentDTO.Create dto);

    PostMeritComponent dtoBatchCreateToEntity(PostMeritComponentDTO.BatchCreate dto);

    @Mappings({
            @Mapping(target = "meritComponent.meritComponentTypes", source = "meritComponent.meritComponentTypes", qualifiedByName = "getKpiTypeByMeritComponentId"),
    })
    PostMeritComponentDTO.Info entityToDtoInfo(PostMeritComponent entity);

    List<PostMeritComponentDTO.Info> entityToDtoInfoList(List<PostMeritComponent> entities);

    void update(@MappingTarget PostMeritComponent entity, PostMeritComponentDTO.Update dto);

    List<EvaluationItemDTO.MeritTupleDTO> postMeritDtoToEvaluationItemInfoList(List<PostMeritComponent> groupTypeMerits);

    @Mapping(target = "postMeritId", source = "id")
    EvaluationItemDTO.MeritTupleDTO postMeritDtoToEvaluationItemInfo(PostMeritComponent groupTypeMerit);
}
