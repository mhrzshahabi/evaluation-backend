package com.nicico.evaluation.mapper;

import com.nicico.evaluation.dto.MeritComponentAuditDTO;
import com.nicico.evaluation.model.MeritComponentAudit;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = "spring", uses = MeritComponentMapper.class)
public interface MeritComponentAuditMapper {

    @Mappings({
            @Mapping(target = "statusCatalog", source = "statusCatalogId", qualifiedByName = "getStatusCatalogInfoByStatusCatalogId"),
            @Mapping(target = "id", source = "auditId.id"),
            @Mapping(target = "rev", source = "auditId.rev")
    })
    MeritComponentAuditDTO.Info entityToDtoInfo(MeritComponentAudit entity);

    List<MeritComponentAuditDTO.Info> entityToDtoInfoList(List<MeritComponentAudit> entities);
}
