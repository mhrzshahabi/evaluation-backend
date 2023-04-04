package com.nicico.evaluation.mapper;

import com.nicico.evaluation.DTO.KPITypeDTO;
import com.nicico.evaluation.model.KPIType;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

import java.util.List;

@Mapper(componentModel = "spring")
public interface KPITypeMapper  {
        KPIType dtoCreateToEntity(KPITypeDTO.Create dto);
        KPITypeDTO.Info entityToDtoInfo(KPIType entity);
        List<KPITypeDTO.Info> entityToDtoInfoList(List<KPIType> entities);
        void update(@MappingTarget KPIType entity, KPITypeDTO.Update dto);
}
