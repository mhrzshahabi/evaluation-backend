package com.nicico.evaluation.mapper;

import com.nicico.evaluation.dto.BatchDTO;
import com.nicico.evaluation.model.Batch;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

import java.util.List;

@Mapper(componentModel = "spring")
public interface BatchMapper {

    Batch dtoCreateToEntity(BatchDTO.Create dto);

    BatchDTO.Info entityToDtoInfo(Batch entity);

    List<BatchDTO.Info> entityToDtoInfoList(List<Batch> entities);

    void update(@MappingTarget Batch entity, BatchDTO.Update dto);
}
