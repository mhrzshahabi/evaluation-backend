package com.nicico.evaluation.mapper;

import com.nicico.evaluation.dto.GroupTypeMeritDTO;
import com.nicico.evaluation.dto.InstanceGroupTypeMeritDTO;
import com.nicico.evaluation.iservice.IGroupTypeService;
import com.nicico.evaluation.iservice.IInstanceGroupTypeMeritService;
import com.nicico.evaluation.model.GroupTypeMerit;
import org.mapstruct.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;

import java.util.List;

@Mapper(componentModel = "spring")
public abstract class GroupTypeMeritMapper {

    @Lazy
    @Autowired
    private IInstanceGroupTypeMeritService instanceGroupTypeMeritService;

    @Lazy
    @Autowired
    private IGroupTypeService groupTypeService;

    public abstract GroupTypeMerit dtoCreateToEntity(GroupTypeMeritDTO.Create dto);


    @Mappings({
            @Mapping(target = "hasInstance", source = "id", qualifiedByName = "getHasInstanceFromId"),
            @Mapping(target = "instance", source = "id", qualifiedByName = "getInstanceInfoFromId"),
            @Mapping(target = "groupId", source = "groupTypeId", qualifiedByName = "getGroupIdFromGroupTypeId"),
            @Mapping(target = "kpiTypeId", source = "groupTypeId", qualifiedByName = "getKpiTypeIdFromGroupTypeId")
    })
    public abstract GroupTypeMeritDTO.Info entityToDtoInfo(GroupTypeMerit entity);

    public abstract List<GroupTypeMeritDTO.Info> entityToDtoInfoList(List<GroupTypeMerit> entities);

    public abstract void update(@MappingTarget GroupTypeMerit entity, GroupTypeMeritDTO.Update dto);

    @Named("getHasInstanceFromId")
    Boolean getHasInstanceFromId(Long id) {
        int instances = instanceGroupTypeMeritService.getAllInstanceByGroupTypeMeritId(id).size();
        return instances != 0;
    }

    @Named("getInstanceInfoFromId")
    List<InstanceGroupTypeMeritDTO.InstanceTupleDTO> getInstanceInfoFromId(Long id) {
        List<InstanceGroupTypeMeritDTO.InstanceInfo> instanceInfoList = instanceGroupTypeMeritService.getAllInstanceByGroupTypeMeritId(id);
        return instanceInfoList.stream().map(InstanceGroupTypeMeritDTO.InstanceInfo::getInstance).toList();
    }

    @Named("getGroupIdFromGroupTypeId")
    Long getGroupIdFromGroupTypeId(Long groupTypeId) {
        return groupTypeService.get(groupTypeId).getGroupId();
    }

    @Named("getKpiTypeIdFromGroupTypeId")
    Long getKpiTypeIdFromGroupTypeId(Long groupTypeId) {
        return groupTypeService.get(groupTypeId).getKpiTypeId();
    }
}
