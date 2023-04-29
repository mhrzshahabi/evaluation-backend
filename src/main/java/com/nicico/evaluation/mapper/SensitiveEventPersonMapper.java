package com.nicico.evaluation.mapper;

import com.nicico.evaluation.dto.SensitiveEventPersonDTO;
import com.nicico.evaluation.model.SensitiveEventPerson;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

import java.util.List;

@Mapper(componentModel = "spring")
public interface SensitiveEventPersonMapper {

    SensitiveEventPerson dtoCreateToEntity(SensitiveEventPersonDTO.Create dto);

    SensitiveEventPersonDTO.Info entityToDtoInfo(SensitiveEventPerson entity);

    List<SensitiveEventPersonDTO.Info> entityToDtoInfoList(List<SensitiveEventPerson> entities);

    void update(@MappingTarget SensitiveEventPerson entity, SensitiveEventPersonDTO.Update dto);
}
