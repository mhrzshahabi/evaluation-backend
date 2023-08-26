package com.nicico.evaluation;

import com.nicico.evaluation.dto.EvaluationItemDTO;
import com.nicico.evaluation.dto.InstanceGroupTypeMeritDTO;
import com.nicico.evaluation.dto.MeritComponentDTO;
import com.nicico.evaluation.dto.PostMeritInstanceDTO;
import com.nicico.evaluation.iservice.IInstanceGroupTypeMeritService;
import com.nicico.evaluation.iservice.IMeritComponentService;
import com.nicico.evaluation.iservice.IPostMeritInstanceService;
import com.nicico.evaluation.model.GroupTypeMerit;
import com.nicico.evaluation.model.PostMeritComponent;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.Named;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;

import java.util.ArrayList;
import java.util.List;

@Mapper(componentModel = "spring")
public abstract class ExternalMapper {

    @Lazy
    @Autowired
    IMeritComponentService meritComponentService;
    @Lazy
    @Autowired
    IPostMeritInstanceService postMeritInstanceService;
    @Lazy
    @Autowired
    IInstanceGroupTypeMeritService instanceGroupTypeMeritService;

    public abstract List<EvaluationItemDTO.MeritTupleDTO> entityToEvaluationItemDtoList(List<GroupTypeMerit> entities);

    @Mappings({
            @Mapping(target = "groupTypeMeritId", source = "id"),
            @Mapping(target = "meritComponent", source = "entity", qualifiedByName = "getMeritComponent"),
            @Mapping(target = "instances", source = "entity", qualifiedByName = "getInstanceGroupTypeMerits"),
            @Mapping(target = "instanceGroupTypeMerits", source = "entity", qualifiedByName = "getAllByGroupTypeMeritId")
    })
    public abstract EvaluationItemDTO.MeritTupleDTO entityToEvaluationItemDtoInfo(GroupTypeMerit entity);

    @Named("getMeritComponent")
    MeritComponentDTO.Info getMeritComponent(GroupTypeMerit entity) {
        return meritComponentService.get(entity.getMeritComponentId());
    }

    @Named("getInstanceGroupTypeMerits")
    List<EvaluationItemDTO.InstanceTupleDTO> getInstanceGroupTypeMerits(GroupTypeMerit entity) {
        List<InstanceGroupTypeMeritDTO.InstanceInfo> allInstanceByGroupTypeMeritId = instanceGroupTypeMeritService.getAllInstanceByGroupTypeMeritId(entity.getId());
        List<EvaluationItemDTO.InstanceTupleDTO> instanceTupleList = new ArrayList<>();
        allInstanceByGroupTypeMeritId.forEach(item -> {
            EvaluationItemDTO.InstanceTupleDTO instanceTuple = new EvaluationItemDTO.InstanceTupleDTO();
            instanceTuple.setTitle(item.getInstance().getTitle());
            instanceTuple.setId(item.getInstance().getId());
            instanceTupleList.add(instanceTuple);
        });
        return instanceTupleList;
    }

    @Named("getAllByGroupTypeMeritId")
    List<EvaluationItemDTO.InstanceGroupTypeMeritTuple> getAllByGroupTypeMeritId(GroupTypeMerit entity) {
        List<InstanceGroupTypeMeritDTO.Info> allByGroupTypeMeritId = instanceGroupTypeMeritService.getAllByGroupTypeMeritId(entity.getId());
        List<EvaluationItemDTO.InstanceGroupTypeMeritTuple> instanceTupleList = new ArrayList<>();
        allByGroupTypeMeritId.forEach(item -> {
            EvaluationItemDTO.InstanceGroupTypeMeritTuple instanceTuple = new EvaluationItemDTO.InstanceGroupTypeMeritTuple();
            instanceTuple.setInstanceId(item.getInstance().getId());
            instanceTuple.setId(item.getId());
            instanceTupleList.add(instanceTuple);
        });
        return instanceTupleList;
    }


    public abstract List<EvaluationItemDTO.MeritTupleDTO> postMeritToEvaluationItemDtoList(List<PostMeritComponent> entities);

    @Mappings({
            @Mapping(target = "postMeritId", source = "id"),
            @Mapping(target = "meritComponent", source = "entity", qualifiedByName = "getMeritComponentByPostMeritId"),
            @Mapping(target = "instances", source = "entity", qualifiedByName = "getPostMeritInstances"),
            @Mapping(target = "postMeritInstanceList", source = "entity", qualifiedByName = "getAllByPostMeritId")
    })
    public abstract EvaluationItemDTO.MeritTupleDTO postMeritToEvaluationItemDtoInfo(PostMeritComponent entity);

    @Named("getMeritComponentByPostMeritId")
    MeritComponentDTO.Info getMeritComponentByPostMeritId(PostMeritComponent entity) {
        return meritComponentService.get(entity.getMeritComponentId());
    }

    @Named("getPostMeritInstances")
    List<EvaluationItemDTO.InstanceTupleDTO> getPostMeritInstances(PostMeritComponent entity) {
        List<PostMeritInstanceDTO.Info> allByPostMeritComponentId = postMeritInstanceService.findAllByPostMeritComponentId(entity.getId());
        List<EvaluationItemDTO.InstanceTupleDTO> instanceTupleList = new ArrayList<>();
        allByPostMeritComponentId.forEach(item -> {
            EvaluationItemDTO.InstanceTupleDTO instanceTuple = new EvaluationItemDTO.InstanceTupleDTO();
            instanceTuple.setTitle(item.getInstance().getTitle());
            instanceTuple.setId(item.getInstance().getId());
            instanceTupleList.add(instanceTuple);
        });
        return instanceTupleList;
    }

    @Named("getAllByPostMeritId")
    List<EvaluationItemDTO.PostMeritInstanceTuple> getAllByPostMeritId(PostMeritComponent entity) {
        List<EvaluationItemDTO.PostMeritInstanceTuple> instanceTupleList = new ArrayList<>();
        List<PostMeritInstanceDTO.Info> allByPostMeritComponentId = postMeritInstanceService.findAllByPostMeritComponentId(entity.getId());
        allByPostMeritComponentId.forEach(item -> {
            EvaluationItemDTO.PostMeritInstanceTuple instanceTuple = new EvaluationItemDTO.PostMeritInstanceTuple();
            instanceTuple.setId(item.getId());
            instanceTuple.setId(item.getInstance().getId());
            instanceTupleList.add(instanceTuple);
        });
        return instanceTupleList;
    }

}
