package com.nicico.evaluation.mapper;

import com.nicico.evaluation.dto.EvaluationItemDTO;
import com.nicico.evaluation.dto.GroupTypeMeritDTO;
import com.nicico.evaluation.dto.PostMeritComponentDTO;
import com.nicico.evaluation.model.EvaluationItem;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Mappings;

import java.util.List;

@Mapper(componentModel = "spring")
public abstract class EvaluationItemMapper {

    public abstract EvaluationItem dtoCreateToEntity(EvaluationItemDTO.Create dto);

    @Mappings({
            @Mapping(target = "groupTypeMerit.meritComponent.meritComponentTypes", ignore = true),
            @Mapping(target = "postMeritComponent.meritComponent.meritComponentTypes", ignore = true)
    })
    public abstract EvaluationItemDTO.Info entityToDtoInfo(EvaluationItem entity);

    public abstract List<EvaluationItemDTO.Info> entityToDtoInfoList(List<EvaluationItem> entities);

    public abstract List<EvaluationItemDTO.MeritTupleDTO> groupTypeMeritDtoToMeritInfoList(List<GroupTypeMeritDTO.Info> groupTypeMeritDTO);

    @Mapping(target = "groupTypeMeritId", source = "id")
    public abstract EvaluationItemDTO.MeritTupleDTO groupTypeMeritDtoToMeritInfo(GroupTypeMeritDTO.Info groupTypeMeritDTO);

    public abstract List<EvaluationItemDTO.MeritTupleDTO> postMeritDtoToMeritInfoList(List<PostMeritComponentDTO.Info> groupTypeMeritDTO);

    @Mapping(target = "postMeritId", source = "id")
    public abstract EvaluationItemDTO.MeritTupleDTO postMeritDtoToMeritInfo(PostMeritComponentDTO.Info groupTypeMeritDTO);

    public abstract void update(@MappingTarget EvaluationItem entity, EvaluationItemDTO.Update dto);
}
