package com.nicico.evaluation.mapper;

import com.nicico.evaluation.dto.CatalogTypeDTO;
import com.nicico.evaluation.model.CatalogType;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

import java.util.List;


@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.WARN)
public interface CatalogTypeBeanMapper {

    CatalogType dtoCreateToEntity(CatalogTypeDTO.Create create);
    CatalogTypeDTO.Info entityToDtoInfo(CatalogType catalogType);
    List<CatalogTypeDTO.Info> entityToDtoInfoList(List<CatalogType> catalogTypeList);
}
