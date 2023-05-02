package com.nicico.evaluation.mapper;

import com.nicico.evaluation.dto.BatchDetailDTO;
import com.nicico.evaluation.model.BatchDetail;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import java.util.List;

@Mapper(componentModel = "spring", uses = BatchMapper.class)
public interface BatchDetailMapper {

    BatchDetail dtoCreateToEntity(BatchDetailDTO.Create dto);

    @Mapping(target = "statusCatalog", source = "statusCatalog.title")
    BatchDetailDTO.Info entityToDtoInfo(BatchDetail entity);

    List<BatchDetailDTO.Info> entityToDtoInfoList(List<BatchDetail> entities);

    void update(@MappingTarget BatchDetail entity, BatchDetailDTO.Update dto);

}
