package com.nicico.evaluation.group;

import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface GroupMapper {

    List<GroupDTO.retrieve> toGroupDtoRetrieves(List<Group> groups);

    GroupDTO.retrieve toGroupDtoRetrieve(Group group);

    Group toGroup(GroupDTO.create groupCreateDto);

}
