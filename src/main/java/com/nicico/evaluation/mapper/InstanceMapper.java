package com.nicico.evaluation.mapper;

import com.nicico.evaluation.dto.InstanceDTO;
import com.nicico.evaluation.model.Instance;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

import java.util.List;

@Mapper(componentModel = "spring")
public interface InstanceMapper {

    InstanceDTO.Excel entityToDtoExcel(Instance entity);
    List<InstanceDTO.Excel> entityToDtoExcelList(List<Instance> entities);

    Instance dtoCreateToEntity(InstanceDTO.Create dto);
    InstanceDTO.Info entityToDtoInfo(Instance entity);
    List<InstanceDTO.Info> entityToDtoInfoList(List<Instance> entities);
    void update(@MappingTarget Instance entity, InstanceDTO.Update dto);

}
