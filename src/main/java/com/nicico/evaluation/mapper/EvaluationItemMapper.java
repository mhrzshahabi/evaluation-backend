package com.nicico.evaluation.mapper;

import com.nicico.evaluation.dto.EvaluationItemDTO;
import com.nicico.evaluation.dto.GroupTypeMeritDTO;
import com.nicico.evaluation.model.EvaluationItem;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Mappings;

import java.util.List;

@Mapper(componentModel = "spring")
public interface EvaluationItemMapper {

    EvaluationItem dtoCreateToEntity(EvaluationItemDTO.Create dto);

    @Mappings({
            @Mapping(target = "groupTypeMerit.meritComponent.meritComponentTypes", ignore = true),
            @Mapping(target = "postMeritComponent.meritComponent.meritComponentTypes", ignore = true)
    })
    EvaluationItemDTO.Info entityToDtoInfo(EvaluationItem entity);

    List<EvaluationItemDTO.Info> entityToDtoInfoList(List<EvaluationItem> entities);

    @Mapping(target = "groupTypeMeritId", source = "id")
    List<EvaluationItemDTO.GroupTypeMeritTupleDTO> groupTypeMeritDTOToDtoInfoList(List<GroupTypeMeritDTO.Info> groupTypeMeritDTO);

    void update(@MappingTarget EvaluationItem entity, EvaluationItemDTO.Update dto);
}
