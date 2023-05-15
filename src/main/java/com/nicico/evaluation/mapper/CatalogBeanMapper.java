package com.nicico.evaluation.mapper;

import com.nicico.evaluation.dto.CatalogDTO;
import com.nicico.evaluation.model.Catalog;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

import java.util.List;


@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.WARN)
public interface CatalogBeanMapper {

    Catalog dtoCreateToEntity(CatalogDTO.Create create);
    CatalogDTO.Info entityToDtoInfo(Catalog catalog);
    List<CatalogDTO.Info> entityToDtoInfoList(List<Catalog> catalog);
    CatalogDTO.PureInfo entityToDtoPureInfo(Catalog catalog);
}
