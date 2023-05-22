package com.nicico.evaluation.mapper;

import com.nicico.evaluation.dto.InstanceGroupTypeMeritDTO;
import com.nicico.evaluation.model.InstanceGroupTypeMerit;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Mappings;

import java.util.List;

@Mapper(componentModel = "spring")
public interface InstanceGroupTypeMeritMapper {

    InstanceGroupTypeMerit dtoCreateToEntity(InstanceGroupTypeMeritDTO.Create dto);

    InstanceGroupTypeMeritDTO.Info entityToDtoInfo(InstanceGroupTypeMerit entity);

    @Mappings({
            @Mapping(target = "id", source = "instance.id"),
            @Mapping(target = "title", source = "instance.title"),
    })
    InstanceGroupTypeMeritDTO.InstanceTupleDTO entityToTupleDTOInfo(InstanceGroupTypeMerit entity);

    List<InstanceGroupTypeMeritDTO.Info> entityToDtoInfoList(List<InstanceGroupTypeMerit> entities);

    List<InstanceGroupTypeMeritDTO.InstanceInfo> entityToDtoInstanceList(List<InstanceGroupTypeMerit> entities);

    List<InstanceGroupTypeMeritDTO.InstanceTupleDTO> entityToTupleDTOInstanceList(List<InstanceGroupTypeMerit> entities);

    void update(@MappingTarget InstanceGroupTypeMerit entity, InstanceGroupTypeMeritDTO.Update dto);
}
