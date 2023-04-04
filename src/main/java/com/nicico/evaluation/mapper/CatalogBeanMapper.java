package com.nicico.evaluation.mapper;

import com.nicico.evaluation.dto.CatalogDTO;
import com.nicico.evaluation.model.Catalog;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;


@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.WARN)
public interface CatalogBeanMapper {

    Catalog createToCatalog (CatalogDTO.Create create);

    CatalogDTO.Info catalogToInfo (Catalog catalog);
}
