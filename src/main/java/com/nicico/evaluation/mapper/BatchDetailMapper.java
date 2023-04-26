package com.nicico.evaluation.mapper;

import com.nicico.evaluation.dto.BatchDetailDTO;
import com.nicico.evaluation.model.BatchDetail;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface BatchDetailMapper {

    BatchDetail dtoCreateToEntity(BatchDetailDTO.Create dto);
//
//    BatchDetailDTO.Info entityToDtoInfo(BatchDetail entity);
//
//    List<BatchDetailDTO.Info> entityToDtoInfoList(List<BatchDetail> entities);
//
//    void update(@MappingTarget BatchDetail entity, BatchDetailDTO.Update dto);
}
