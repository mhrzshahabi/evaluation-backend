package com.nicico.evaluation.mapper;

import com.nicico.evaluation.dto.PostMeritComponentDTO;
import com.nicico.evaluation.model.PostMeritComponent;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

import java.util.List;

@Mapper(componentModel = "spring")
public interface PostMeritComponentMapper {

    PostMeritComponent dtoCreateToEntity(PostMeritComponentDTO.Create dto);

    PostMeritComponentDTO.Info entityToDtoInfo(PostMeritComponent entity);

    List<PostMeritComponentDTO.Info> entityToDtoInfoList(List<PostMeritComponent> entities);

    void update(@MappingTarget PostMeritComponent entity, PostMeritComponentDTO.Update dto);
}
