package com.nicico.evaluation.mapper;

import com.nicico.evaluation.dto.GroupTypeMeritDTO;
import com.nicico.evaluation.model.GroupTypeMerit;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = "spring", uses = {MeritComponentMapper.class, InstanceGroupTypeMeritMapper.class})
public interface GroupTypeMeritMapper {

    GroupTypeMerit dtoCreateToEntity(GroupTypeMeritDTO.Create dto);

    @Mappings({
            @Mapping(target = "meritComponent.meritComponentTypes", source = "meritComponent.meritComponentTypes", qualifiedByName = "getKpiTypeByMeritComponentId")
    })
    GroupTypeMeritDTO.Info entityToDtoInfo(GroupTypeMerit entity);

    List<GroupTypeMeritDTO.Info> entityToDtoInfoList(List<GroupTypeMerit> entities);

    void update(@MappingTarget GroupTypeMerit entity, GroupTypeMeritDTO.Update dto);

}
