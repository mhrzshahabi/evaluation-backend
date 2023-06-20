package com.nicico.evaluation.mapper;

import com.nicico.evaluation.dto.EvaluationItemDTO;
import com.nicico.evaluation.dto.PostMeritComponentDTO;
import com.nicico.evaluation.dto.PostMeritInstanceDTO;
import com.nicico.evaluation.dto.PostRelationDTO;
import com.nicico.evaluation.iservice.IPostRelationService;
import com.nicico.evaluation.model.PostMeritComponent;
import com.nicico.evaluation.model.PostMeritInstance;
import org.mapstruct.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;

import java.util.List;

@Mapper(componentModel = "spring", uses = {MeritComponentMapper.class, PostMeritInstanceMapper.class})
public abstract class PostMeritComponentMapper {
    @Lazy
    @Autowired
    private IPostRelationService postRelationService;

    public abstract PostMeritComponent dtoCreateToEntity(PostMeritComponentDTO.Create dto);

    public abstract PostMeritComponent dtoBatchCreateToEntity(PostMeritComponentDTO.BatchCreate dto);

    @Mappings({
            @Mapping(target = "meritComponent.meritComponentTypes", source = "meritComponent.meritComponentTypes", qualifiedByName = "getKpiTypeByMeritComponentId"),
            @Mapping(target = "postRelation", source = "entity", qualifiedByName = "getPostRelation"),
    })
    public abstract PostMeritComponentDTO.Info entityToDtoInfo(PostMeritComponent entity);

    public abstract List<PostMeritComponentDTO.Info> entityToDtoInfoList(List<PostMeritComponent> entities);

    public abstract void update(@MappingTarget PostMeritComponent entity, PostMeritComponentDTO.Update dto);

    public abstract List<EvaluationItemDTO.MeritTupleDTO> postMeritDtoToEvaluationItemInfoList(List<PostMeritComponent> groupTypeMerits);

    @Mapping(target = "postMeritId", source = "id")
    @Mapping(target = "instances", source = "postMeritInstanceList")
    public abstract EvaluationItemDTO.MeritTupleDTO postMeritDtoToEvaluationItemInfo(PostMeritComponent groupTypeMerit);

    @Mapping(target = "id", source = "postMeritInstance.id")
    @Mapping(target = "instanceId", source = "postMeritInstance.instanceId")
    public abstract EvaluationItemDTO.PostMeritInstanceTuple postMeritInstanceTupleToPostMeritInstance(PostMeritInstance postMeritInstance);

    public abstract EvaluationItemDTO.InstanceTupleDTO instanceToPostMeritInstance(PostMeritInstanceDTO.InstanceTupleDTO postMeritInstance);

    @Named("getPostRelation")
    PostRelationDTO.Info getPostRelation(PostMeritComponent postMeritComponent) {
        return null;
        //return  postRelationService.getByPostCode(postMeritComponent.getGroupPostCode());
    }
}
