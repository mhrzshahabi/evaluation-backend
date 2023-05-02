
package com.nicico.evaluation.mapper;

import com.nicico.evaluation.dto.SpecialCaseDTO;
import com.nicico.evaluation.model.SpecialCase;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

import java.util.List;

@Mapper(componentModel = "spring")
public interface SpecialCaseMapper {
    SpecialCase dtoCreateToEntity(SpecialCaseDTO.Create dto);

    SpecialCaseDTO.Info entityToDtoInfo(SpecialCase entity);

    List<SpecialCaseDTO.Info> entityToDtoInfoList(List<SpecialCase> entities);

    void update(@MappingTarget SpecialCase entity, SpecialCaseDTO.Update dto);
}
    