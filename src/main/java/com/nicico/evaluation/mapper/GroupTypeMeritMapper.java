package com.nicico.evaluation.mapper;

import com.nicico.evaluation.dto.GroupTypeMeritDTO;
import com.nicico.evaluation.dto.InstanceGroupTypeMeritDTO;
import com.nicico.evaluation.iservice.IInstanceGroupTypeMeritService;
import com.nicico.evaluation.model.GroupTypeMerit;
import org.mapstruct.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;

import java.util.List;

@Mapper(componentModel = "spring",uses = MeritComponentMapper.class)
public abstract class GroupTypeMeritMapper {

    @Lazy
    @Autowired
    private IInstanceGroupTypeMeritService instanceGroupTypeMeritService;

    public abstract GroupTypeMerit dtoCreateToEntity(GroupTypeMeritDTO.Create dto);

    @Mappings({
//            @Mapping(target = "hasInstance", source = "id", qualifiedByName = "getHasInstanceFromId"),
            @Mapping(target = "meritComponent.meritComponentTypes", source = "meritComponent.meritComponentTypes", qualifiedByName = "getKpiTypeByMeritComponentId"),
            @Mapping(target = "instance", source = "id", qualifiedByName = "getInstanceInfoFromId"),
    })
    public abstract GroupTypeMeritDTO.Info entityToDtoInfo(GroupTypeMerit entity);

    public abstract List<GroupTypeMeritDTO.Info> entityToDtoInfoList(List<GroupTypeMerit> entities);

    public abstract void update(@MappingTarget GroupTypeMerit entity, GroupTypeMeritDTO.Update dto);

//    @Named("getHasInstanceFromId")
//    Boolean getHasInstanceFromId(Long id) {
//        int instances = instanceGroupTypeMeritService.getAllInstanceByGroupTypeMeritId(id).size();
//        return instances != 0;
//    }

    @Named("getInstanceInfoFromId")
    List<InstanceGroupTypeMeritDTO.InstanceTupleDTO> getInstanceInfoFromId(Long id) {
        List<InstanceGroupTypeMeritDTO.InstanceInfo> instanceInfoList = instanceGroupTypeMeritService.getAllInstanceByGroupTypeMeritId(id);
        return instanceInfoList.stream().map(InstanceGroupTypeMeritDTO.InstanceInfo::getInstance).toList();
    }

}
