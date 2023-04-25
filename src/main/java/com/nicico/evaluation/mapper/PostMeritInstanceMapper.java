package com.nicico.evaluation.mapper;

import com.nicico.evaluation.dto.PostMeritInstanceDTO;
import com.nicico.evaluation.model.PostMeritInstance;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

import java.util.List;

@Mapper(componentModel = "spring")
public interface PostMeritInstanceMapper {

    PostMeritInstance dtoCreateToEntity(PostMeritInstanceDTO.Create dto);

    PostMeritInstanceDTO.Info entityToDtoInfo(PostMeritInstance entity);

    List<PostMeritInstanceDTO.Info> entityToDtoInfoList(List<PostMeritInstance> entities);

    void update(@MappingTarget PostMeritInstance entity, PostMeritInstanceDTO.Update dto);
}
