package com.nicico.evaluation.mapper;

import com.nicico.evaluation.dto.EvaluationItemDTO;
import com.nicico.evaluation.dto.GroupTypeMeritDTO;
import com.nicico.evaluation.dto.InstanceGroupTypeMeritDTO;
import com.nicico.evaluation.model.GroupTypeMerit;
import com.nicico.evaluation.model.InstanceGroupTypeMerit;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Mappings;

import java.util.List;

@Mapper(componentModel = "spring", uses = {MeritComponentMapper.class, InstanceGroupTypeMeritMapper.class})
public interface GroupTypeMeritMapper {

    GroupTypeMerit dtoCreateToEntity(GroupTypeMeritDTO.Create dto);

    @Mappings({
            @Mapping(target = "meritComponent.meritComponentTypes", source = "meritComponent.meritComponentTypes", qualifiedByName = "getKpiTypeByMeritComponentId")
    })
    GroupTypeMeritDTO.Info entityToDtoInfo(GroupTypeMerit entity);

    List<GroupTypeMeritDTO.Info> entityToDtoInfoList(List<GroupTypeMerit> entities);

    List<EvaluationItemDTO.MeritTupleDTO> entityToEvaluationItemDtoList(List<GroupTypeMerit> entities);

    @Mapping(target = "groupTypeMeritId", source = "id")
    @Mapping(target = "instances", source = "instanceGroupTypeMerits")
    EvaluationItemDTO.MeritTupleDTO entityToEvaluationItemDtoInfo(GroupTypeMerit entity);

    EvaluationItemDTO.InstanceTupleDTO instanceToPostMeritInstance(InstanceGroupTypeMeritDTO.InstanceTupleDTO postMeritInstance);

    @Mapping(target = "id", source = "instanceGroupTypeMerit.id")
    @Mapping(target = "instanceId", source = "instanceGroupTypeMerit.instanceId")
    EvaluationItemDTO.InstanceGroupTypeMeritTuple instanceGroupTypeMeritToInstanceGroupTypeMerit(InstanceGroupTypeMerit instanceGroupTypeMerit);

    void update(@MappingTarget GroupTypeMerit entity, GroupTypeMeritDTO.Update dto);

    @Mappings({
            @Mapping(target = "hasInstanceStr", expression = "java(hasInstanceSTR(entity.getHasInstance()))"),
            @Mapping(target = "groupTypeTitle", source = "groupType.group.title"),
            @Mapping(target = "kpiTypeTitle", source = "groupType.kpiType.title"),
            @Mapping(target = "meritComponentTitle", source = "meritComponent.title")

    })
    GroupTypeMeritDTO.Excel entityToDtoExcel(GroupTypeMerit entity);

    default String hasInstanceSTR(Boolean hasInstance) {
        return hasInstance ? "دارد" : "ندارد";
    }

}
