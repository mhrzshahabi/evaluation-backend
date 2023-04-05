package com.nicico.evaluation.mapper;

import com.nicico.evaluation.dto.MeritComponentDTO;
import com.nicico.evaluation.model.MeritComponent;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

import java.util.List;

@Mapper(componentModel = "spring")
public interface MeritComponentMapper {
        MeritComponent dtoCreateToEntity(MeritComponentDTO.Create dto);
        MeritComponentDTO.Info entityToDtoInfo(MeritComponent entity);
        List<MeritComponentDTO.Info> entityToDtoInfoList(List<MeritComponent> entities);
        void update(@MappingTarget MeritComponent entity, MeritComponentDTO.Update dto);
}
