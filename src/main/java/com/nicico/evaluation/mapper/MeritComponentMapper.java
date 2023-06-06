package com.nicico.evaluation.mapper;

import com.nicico.evaluation.dto.MeritComponentDTO;
import com.nicico.evaluation.dto.MeritComponentTypeDTO;
import com.nicico.evaluation.model.MeritComponent;
import com.nicico.evaluation.model.MeritComponentType;
import org.mapstruct.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;

import java.util.List;

@Mapper(componentModel = "spring")
public abstract class MeritComponentMapper {

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

    @Named("getKpiTypeByMeritComponentId")
    MeritComponentTypeDTO.Info getKpiTypeByMeritComponentId(List<MeritComponentType> meritComponentTypeList) {
        if (meritComponentTypeList!=null && !meritComponentTypeList.isEmpty()){
            //todo ui change object to list
            return  meritComponentTypeMapper.entityToDtoInfo(meritComponentTypeList.get(0));
        } else return null;
    }

    @Mappings({
            @Mapping(target = "kpiTypeTitle", source = "meritComponentTypes", qualifiedByName = "getKpiTypeTitleByMeritComponentId"),
            @Mapping(target = "status", source = "id", qualifiedByName = "getStatus"),
    })
    public abstract MeritComponentDTO.Excel entityToDtoExcel(MeritComponent entity);
    @Named("getStatus")
    String getStatus(Long id){
        return  "استفاده نشده";
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
