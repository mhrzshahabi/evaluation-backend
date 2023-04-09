package com.nicico.evaluation.mapper;

import com.nicico.evaluation.dto.GroupDTO;
import com.nicico.evaluation.model.Group;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

import java.util.List;

@Mapper(componentModel = "spring")
public interface GroupMapper {

    Group dtoCreateToEntity(GroupDTO.Create dto);
    GroupDTO.Info entityToDtoInfo(Group entity);
    List<GroupDTO.Info> entityToDtoInfoList(List<Group> entities);
    void update(@MappingTarget Group entity, GroupDTO.Update dto);
}
