package com.nicico.evaluation.mapper;

import com.nicico.evaluation.dto.GroupDTO;
import com.nicico.evaluation.dto.GroupTypeDTO;
import com.nicico.evaluation.model.GroupType;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;

import java.util.List;

@Mapper(componentModel = "spring")
public abstract class GroupTypeMapper {

    @Lazy
    @Autowired
    private GroupMapper groupMapper;

    public abstract GroupType dtoCreateToEntity(GroupTypeDTO.Create dto);

    @Mapping(target = "group", source = "entity", qualifiedByName = "setGroupGrade")
    public abstract GroupTypeDTO.Info entityToDtoInfo(GroupType entity);

    public abstract List<GroupTypeDTO.Info> entityToDtoInfoList(List<GroupType> entities);

    public abstract void update(@MappingTarget GroupType entity, GroupTypeDTO.Update dto);

    @Named("setGroupGrade")
    GroupDTO.Info setGroupGrade(GroupType entity) {
        return groupMapper.entityToDtoInfo(entity.getGroup());
    }
}
