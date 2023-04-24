package com.nicico.evaluation.mapper;

import com.nicico.evaluation.dto.GroupPostDTO;
import com.nicico.evaluation.model.GroupPost;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public abstract class GroupPostMapper {

    public abstract GroupPostDTO.Info entityToDtoInfo(GroupPost entity);

    public abstract List<GroupPostDTO.Info> entityToDtoInfoList(List<GroupPost> entities);

}
