package com.nicico.evaluation.mapper;

import com.nicico.evaluation.dto.InstanceGroupTypeMeritDTO;
import com.nicico.evaluation.model.InstanceGroupTypeMerit;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

import java.util.List;

@Mapper(componentModel = "spring")
public interface InstanceGroupTypeMeritMapper {

    InstanceGroupTypeMerit dtoCreateToEntity(InstanceGroupTypeMeritDTO.Create dto);

    InstanceGroupTypeMeritDTO.Info entityToDtoInfo(InstanceGroupTypeMerit entity);

    List<InstanceGroupTypeMeritDTO.Info> entityToDtoInfoList(List<InstanceGroupTypeMerit> entities);

    void update(@MappingTarget InstanceGroupTypeMerit entity, InstanceGroupTypeMeritDTO.Update dto);
}
