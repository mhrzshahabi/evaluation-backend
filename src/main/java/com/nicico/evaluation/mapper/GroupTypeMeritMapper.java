package com.nicico.evaluation.mapper;

import com.nicico.evaluation.dto.GroupTypeMeritDTO;
import com.nicico.evaluation.model.GroupTypeMerit;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

import java.util.List;

@Mapper(componentModel = "spring")
public interface GroupTypeMeritMapper {
        GroupTypeMerit dtoCreateToEntity(GroupTypeMeritDTO.Create dto);
        GroupTypeMeritDTO.Info entityToDtoInfo(GroupTypeMerit entity);
        List<GroupTypeMeritDTO.Info> entityToDtoInfoList(List<GroupTypeMerit> entities);
        void update(@MappingTarget GroupTypeMerit entity, GroupTypeMeritDTO.Update dto);
}
