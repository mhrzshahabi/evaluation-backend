package com.nicico.evaluation.mapper;

import com.nicico.evaluation.dto.GroupDTO;
import com.nicico.evaluation.model.Group;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface GroupMapper {

    List<GroupDTO.retrieve> toGroupDtoRetrieves(List<Group> groups);

    GroupDTO.retrieve toGroupDtoRetrieve(Group group);

    Group toGroup(GroupDTO.create groupCreateDto);

}
