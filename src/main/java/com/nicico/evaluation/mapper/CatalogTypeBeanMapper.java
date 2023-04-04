package com.nicico.evaluation.mapper;

import com.nicico.evaluation.dto.CatalogTypeDTO;
import com.nicico.evaluation.model.CatalogType;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;


@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.WARN)
public interface CatalogTypeBeanMapper {

    CatalogType createToCatalogType (CatalogTypeDTO.Create create);

    CatalogTypeDTO.Info catalogTypeToInfo (CatalogType catalogType);
}
