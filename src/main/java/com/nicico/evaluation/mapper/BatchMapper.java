package com.nicico.evaluation.mapper;

import com.nicico.evaluation.dto.BatchDTO;
import com.nicico.evaluation.model.Batch;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = "spring")
public interface BatchMapper {

    Batch dtoCreateToEntity(BatchDTO.Create dto);

    @Mappings({
            @Mapping(target = "titleCatalog", source = "titleCatalog.title"),
            @Mapping(target = "statusCatalog", source = "statusCatalog.title")
    })
    BatchDTO.Info entityToDtoInfo(Batch entity);

    List<BatchDTO.Info> entityToDtoInfoList(List<Batch> entities);

    void update(@MappingTarget Batch entity, BatchDTO.Update dto);

}
