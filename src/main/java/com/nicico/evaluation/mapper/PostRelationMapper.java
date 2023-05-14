package com.nicico.evaluation.mapper;

import com.nicico.evaluation.dto.PostRelationDTO;
import com.nicico.evaluation.model.PostRelation;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface PostRelationMapper {

    PostRelationDTO.Info entityToDtoInfo(PostRelation entity);

    List<PostRelationDTO.Info> entityToDtoInfoList(List<PostRelation> entities);
}
