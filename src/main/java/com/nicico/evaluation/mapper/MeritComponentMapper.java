package com.nicico.evaluation.mapper;

import com.nicico.evaluation.dto.CatalogDTO;
import com.nicico.evaluation.dto.MeritComponentDTO;
import com.nicico.evaluation.dto.MeritComponentTypeDTO;
import com.nicico.evaluation.iservice.ICatalogService;
import com.nicico.evaluation.iservice.IMeritComponentService;
import com.nicico.evaluation.model.MeritComponent;
import com.nicico.evaluation.model.MeritComponentAudit;
import com.nicico.evaluation.model.MeritComponentType;
import org.mapstruct.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;

import java.util.List;

@Mapper(componentModel = "spring")
public abstract class MeritComponentMapper {

    @Lazy
    @Autowired
    private ICatalogService catalogService;

    @Lazy
    @Autowired
    private IMeritComponentService meritComponentService;

    @Lazy
    @Autowired
    private MeritComponentTypeMapper meritComponentTypeMapper;

    public abstract MeritComponent dtoCreateToEntity(MeritComponentDTO.Create dto);

    @Mappings({
            @Mapping(target = "meritComponentTypes", source = "meritComponentTypes", qualifiedByName = "getKpiTypeByMeritComponentId"),
    })
    public abstract MeritComponentDTO.Info entityToDtoInfo(MeritComponent entity);

    public abstract List<MeritComponentDTO.Info> entityToDtoInfoList(List<MeritComponent> entities);

    public abstract void update(@MappingTarget MeritComponent entity, MeritComponentDTO.Update dto);

    @Mappings({
            @Mapping(target = "kpiTypeTitle", source = "meritComponentTypes", qualifiedByName = "getKpiTypeTitleByMeritComponentId"),
    })
    public abstract MeritComponentDTO.Excel entityToDtoExcel(MeritComponent entity);

    @Mappings({
            @Mapping(target = "meritComponentTypes", source = "id", qualifiedByName = "getKpiTypeInfoByMeritComponentId"),
            @Mapping(target = "statusCatalog", source = "statusCatalogId", qualifiedByName = "getStatusCatalogInfoByStatusCatalogId")
    })
    public abstract MeritComponentDTO.Info meritComponentAuditToDtoInfo(MeritComponentAudit meritComponentAudit);

    public abstract List<MeritComponentDTO.Info> meritComponentAuditToDtoInfoList(List<MeritComponentAudit> meritComponentAudits);

    @Named("getKpiTypeByMeritComponentId")
    MeritComponentTypeDTO.Info getKpiTypeByMeritComponentId(List<MeritComponentType> meritComponentTypeList) {
        if (meritComponentTypeList!=null && !meritComponentTypeList.isEmpty()){
            return  meritComponentTypeMapper.entityToDtoInfo(meritComponentTypeList.get(0));
        } else return null;
    }

    @Named("getKpiTypeInfoByMeritComponentId")
    MeritComponentTypeDTO.Info getKpiTypeInfoByMeritComponentId(Long meritComponentId) {
        return meritComponentService.get(meritComponentId).getMeritComponentTypes();
    }

    @Named("getStatusCatalogInfoByStatusCatalogId")
    CatalogDTO.Info getStatusCatalogInfoByStatusCatalogId(Long statusCatalogId) {
        return catalogService.get(statusCatalogId);
    }

    @Named("getKpiTypeTitleByMeritComponentId")
    String getKpiTypeTitleByMeritComponentId(List<MeritComponentType> meritComponentTypeList) {
        if (meritComponentTypeList!=null && !meritComponentTypeList.isEmpty()){
            MeritComponentType meritComponentType = meritComponentTypeList.get(0);
            if(meritComponentType != null && meritComponentType.getKpiType() != null)
                return  meritComponentType.getKpiType().getTitle();
        }
        return null;
    }
}
