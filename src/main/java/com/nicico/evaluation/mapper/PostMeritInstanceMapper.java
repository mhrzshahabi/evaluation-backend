package com.nicico.evaluation.mapper;

import com.nicico.evaluation.dto.PostMeritComponentDTO;
import com.nicico.evaluation.dto.PostMeritInstanceDTO;
import com.nicico.evaluation.model.PostMeritInstance;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import java.util.List;

@Mapper(componentModel = "spring", uses = MeritComponentMapper.class)
public interface PostMeritInstanceMapper {

    PostMeritInstance dtoCreateToEntity(PostMeritInstanceDTO.Create dto);

    PostMeritComponentDTO.BatchCreate dtoBatchCreateToDtoComponentBatchCreate(PostMeritInstanceDTO.BatchCreate dto);

    @Mapping(target = "postMeritComponent.meritComponent.meritComponentTypes", source = "postMeritComponent.meritComponent.meritComponentTypes", qualifiedByName = "getKpiTypeByMeritComponentId")
    PostMeritInstanceDTO.Info entityToDtoInfo(PostMeritInstance entity);

    List<PostMeritInstanceDTO.Info> entityToDtoInfoList(List<PostMeritInstance> entities);

    void update(@MappingTarget PostMeritInstance entity, PostMeritInstanceDTO.Update dto);

}
