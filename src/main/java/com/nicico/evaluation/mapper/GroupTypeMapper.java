package com.nicico.evaluation.mapper;

import com.nicico.evaluation.dto.GroupTypeDTO;
import com.nicico.evaluation.model.GroupType;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

import java.util.List;

@Mapper(componentModel = "spring")
public interface GroupTypeMapper {

    GroupType dtoCreateToEntity(GroupTypeDTO.Create dto);

    GroupTypeDTO.Info entityToDtoInfo(GroupType entity);

    List<GroupTypeDTO.Info> entityToDtoInfoList(List<GroupType> entities);

    void update(@MappingTarget GroupType entity, GroupTypeDTO.Update dto);
}
