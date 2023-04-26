package com.nicico.evaluation.mapper;

import com.nicico.evaluation.dto.SensitiveEventsDTO;
import com.nicico.evaluation.model.SensitiveEvents;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

import java.util.List;

@Mapper(componentModel = "spring")
public interface SensitiveEventsMapper {

    SensitiveEvents dtoCreateToEntity(SensitiveEventsDTO.Create dto);

    SensitiveEventsDTO.Info entityToDtoInfo(SensitiveEvents entity);

    List<SensitiveEventsDTO.Info> entityToDtoInfoList(List<SensitiveEvents> entities);

    void update(@MappingTarget SensitiveEvents entity, SensitiveEventsDTO.Update dto);
}
