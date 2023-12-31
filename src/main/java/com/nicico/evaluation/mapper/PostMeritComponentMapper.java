package com.nicico.evaluation.mapper;

import com.nicico.evaluation.dto.EvaluationItemDTO;
import com.nicico.evaluation.dto.PostMeritComponentDTO;
import com.nicico.evaluation.dto.PostMeritInstanceDTO;
import com.nicico.evaluation.model.PostMeritComponent;
import com.nicico.evaluation.model.PostMeritInstance;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = "spring", uses = {MeritComponentMapper.class, PostMeritInstanceMapper.class, GroupTypeMeritMapper.class})
public abstract class PostMeritComponentMapper {

    public abstract PostMeritComponent dtoCreateToEntity(PostMeritComponentDTO.Create dto);

    public abstract PostMeritComponent dtoBatchCreateToEntity(PostMeritComponentDTO.BatchCreate dto);

    @Mappings({
            @Mapping(target = "meritComponent.meritComponentTypes", source = "meritComponent.meritComponentTypes", qualifiedByName = "getKpiTypeByMeritComponentId")
    })
    public abstract PostMeritComponentDTO.Info entityToDtoInfo(PostMeritComponent entity);

    public abstract List<PostMeritComponentDTO.Info> entityToDtoInfoList(List<PostMeritComponent> entities);

    @Mappings({
            @Mapping(target = "meritComponent", source = "meritComponentId", qualifiedByName = "getLastActiveMerit")
    })
    public abstract PostMeritComponentDTO.LastActiveMeritInfo entityToDtoLastActiveMeritInfo(PostMeritComponent entity);

    public abstract List<PostMeritComponentDTO.LastActiveMeritInfo> entityToDtoLastActiveMeritInfoList(List<PostMeritComponent> entities);

    public abstract void update(@MappingTarget PostMeritComponent entity, PostMeritComponentDTO.Update dto);

    public abstract List<EvaluationItemDTO.MeritTupleDTO> postMeritDtoToEvaluationItemInfoList(List<PostMeritComponent> postMeritComponents);

    @Mapping(target = "postMeritId", source = "id")
    @Mapping(target = "instances", source = "postMeritInstanceList")
    @Mapping(target = "meritComponent", source = "meritComponentId", qualifiedByName = "getLastActiveMerit")
    public abstract EvaluationItemDTO.MeritTupleDTO postMeritDtoToEvaluationItemInfo(PostMeritComponent postMeritComponent);

    @Mapping(target = "id", source = "postMeritInstance.id")
    @Mapping(target = "instanceId", source = "postMeritInstance.instanceId")
    public abstract EvaluationItemDTO.PostMeritInstanceTuple postMeritInstanceTupleToPostMeritInstance(PostMeritInstance postMeritInstance);

    public abstract EvaluationItemDTO.InstanceTupleDTO instanceToPostMeritInstance(PostMeritInstanceDTO.InstanceTupleDTO postMeritInstance);

}
