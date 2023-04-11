package com.nicico.evaluation.mapper;

import com.nicico.evaluation.dto.MeritComponentTypeDTO;
import com.nicico.evaluation.model.MeritComponentType;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

import java.util.List;

@Mapper(componentModel = "spring")
public interface MeritComponentTypeMapper {

    MeritComponentType dtoCreateToEntity(MeritComponentTypeDTO.Create dto);

    MeritComponentTypeDTO.Info entityToDtoInfo(MeritComponentType entity);

    List<MeritComponentTypeDTO.Info> entityToDtoInfoList(List<MeritComponentType> entities);

    void update(@MappingTarget MeritComponentType entity, MeritComponentTypeDTO.Update dto);
}
