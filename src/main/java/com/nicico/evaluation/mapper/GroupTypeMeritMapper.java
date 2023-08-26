package com.nicico.evaluation.mapper;

import com.nicico.evaluation.dto.EvaluationItemDTO;
import com.nicico.evaluation.dto.GroupTypeMeritDTO;
import com.nicico.evaluation.dto.InstanceGroupTypeMeritDTO;
import com.nicico.evaluation.dto.MeritComponentDTO;
import com.nicico.evaluation.iservice.IGroupTypeMeritService;
import com.nicico.evaluation.iservice.IMeritComponentService;
import com.nicico.evaluation.model.GroupTypeMerit;
import com.nicico.evaluation.model.InstanceGroupTypeMerit;
import org.mapstruct.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;

import java.util.List;

@Mapper(componentModel = "spring", uses = {MeritComponentMapper.class, InstanceGroupTypeMeritMapper.class})
public abstract class GroupTypeMeritMapper {

    @Lazy
    @Autowired
    private IGroupTypeMeritService groupTypeMeritService;

    @Lazy
    @Autowired
    private IMeritComponentService meritComponentService;

    public abstract GroupTypeMerit dtoCreateToEntity(GroupTypeMeritDTO.Create dto);

    @Mappings({
            @Mapping(target = "meritComponent.meritComponentTypes", source = "meritComponent.meritComponentTypes", qualifiedByName = "getKpiTypeByMeritComponentId"),
            @Mapping(target = "totalComponentWeight", source = "groupTypeId", qualifiedByName = "getTotalComponentWeightByGroupType")
    })
    public abstract GroupTypeMeritDTO.Info entityToDtoInfo(GroupTypeMerit entity);

    public abstract List<GroupTypeMeritDTO.Info> entityToDtoInfoList(List<GroupTypeMerit> entities);

    @Mappings({
            @Mapping(target = "meritComponent", source = "meritComponentId", qualifiedByName = "getLastActiveMerit"),
            @Mapping(target = "totalComponentWeight", source = "groupTypeId", qualifiedByName = "getTotalComponentWeightByGroupType")
    })
    public abstract GroupTypeMeritDTO.LastActiveMeritInfo entityToDtoLastActiveMeritInfo(GroupTypeMerit entity);

    public abstract List<GroupTypeMeritDTO.LastActiveMeritInfo> entityToDtoLastActiveMeritInfoList(List<GroupTypeMerit> entities);

    public abstract List<EvaluationItemDTO.MeritTupleDTO> entityToEvaluationItemDtoList(List<GroupTypeMerit> entities);

    @Mapping(target = "groupTypeMeritId", source = "id")
    @Mapping(target = "instances", source = "instanceGroupTypeMerits")
    @Mapping(target = "meritComponent", source = "meritComponentId", qualifiedByName = "getLastActiveMerit")
    public abstract EvaluationItemDTO.MeritTupleDTO entityToEvaluationItemDtoInfo(GroupTypeMerit entity);

    public abstract EvaluationItemDTO.InstanceTupleDTO instanceToPostMeritInstance(InstanceGroupTypeMeritDTO.InstanceTupleDTO postMeritInstance);

    @Mapping(target = "id", source = "instanceGroupTypeMerit.id")
    @Mapping(target = "instanceId", source = "instanceGroupTypeMerit.instanceId")
    public abstract EvaluationItemDTO.InstanceGroupTypeMeritTuple instanceGroupTypeMeritToInstanceGroupTypeMerit(InstanceGroupTypeMerit instanceGroupTypeMerit);

    public abstract void update(@MappingTarget GroupTypeMerit entity, GroupTypeMeritDTO.Update dto);

    @Mappings({
            @Mapping(target = "hasInstanceStr", expression = "java(hasInstanceSTR(entity.getHasInstance()))"),
            @Mapping(target = "groupTypeTitle", source = "groupType.group.title"),
            @Mapping(target = "kpiTypeTitle", source = "groupType.kpiType.title"),
            @Mapping(target = "meritComponentTitle", source = "meritComponent.title")
    })
    public abstract GroupTypeMeritDTO.Excel entityToDtoExcel(GroupTypeMerit entity);

    String hasInstanceSTR(Boolean hasInstance) {
        return hasInstance ? "دارد" : "ندارد";
    }

    @Named("getTotalComponentWeightByGroupType")
    Long getTotalComponentWeightByGroupType(Long groupTypeId) {
        return groupTypeMeritService.getTotalComponentWeightByGroupType(groupTypeId);
    }

    @Named("getLastActiveMerit")
    MeritComponentDTO.Info getLastActiveMerit(Long meritComponentId) {
        return meritComponentService.findLastActiveByMeritComponentId(meritComponentId);
    }
}
