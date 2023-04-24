package com.nicico.evaluation.mapper;

import com.nicico.evaluation.dto.GroupTypeMeritDTO;
import com.nicico.evaluation.iservice.IInstanceGroupTypeMeritService;
import com.nicico.evaluation.model.GroupTypeMerit;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;

import java.util.List;

@Mapper(componentModel = "spring")
public abstract class GroupTypeMeritMapper {

    @Lazy
    @Autowired
    private IInstanceGroupTypeMeritService instanceGroupTypeMeritService;

    public abstract GroupTypeMerit dtoCreateToEntity(GroupTypeMeritDTO.Create dto);

    @Mapping(target = "hasInstance", source = "id", qualifiedByName = "getHasInstanceFromId")
    public abstract GroupTypeMeritDTO.Info entityToDtoInfo(GroupTypeMerit entity);

    public abstract List<GroupTypeMeritDTO.Info> entityToDtoInfoList(List<GroupTypeMerit> entities);

    public abstract void update(@MappingTarget GroupTypeMerit entity, GroupTypeMeritDTO.Update dto);

    @Named("getHasInstanceFromId")
    Boolean getHasInstanceFromId(Long id) {
        int instances = instanceGroupTypeMeritService.getAllInstanceByGroupTypeMeritId(id).size();
        return instances != 0;
    }
}
