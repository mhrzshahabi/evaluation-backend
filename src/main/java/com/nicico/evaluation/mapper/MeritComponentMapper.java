package com.nicico.evaluation.mapper;

import com.nicico.evaluation.dto.KPITypeDTO;
import com.nicico.evaluation.dto.MeritComponentDTO;
import com.nicico.evaluation.dto.MeritComponentTypeDTO;
import com.nicico.evaluation.iservice.IMeritComponentTypeService;
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
//            @Mapping(target = "kpiType", source = "id", qualifiedByName = "getAllKpiTypeByMeritComponentId"),
            @Mapping(target = "meritComponentTypes", source = "meritComponentTypes", qualifiedByName = "getKpiTypeByMeritComponentId"),
    })
    public abstract MeritComponentDTO.Info entityToDtoInfo(MeritComponent entity);

    public abstract List<MeritComponentDTO.Info> entityToDtoInfoList(List<MeritComponent> entities);

    public abstract void update(@MappingTarget MeritComponent entity, MeritComponentDTO.Update dto);

//    @Named("getAllKpiTypeByMeritComponentId")
//    List<KPITypeDTO.Info> getAllKpiTypeByMeritComponentId(Long id) {
//        return meritComponentTypeService.findAllByMeritComponentId(id).stream().map(MeritComponentTypeDTO.Info::getKpiType).toList();
//    }
    @Named("getKpiTypeByMeritComponentId")
    MeritComponentTypeDTO.Info getKpiTypeByMeritComponentId(List<MeritComponentType> meritComponentTypeList) {
        if (meritComponentTypeList!=null && !meritComponentTypeList.isEmpty()){
            //todo ui change object to list
            return  meritComponentTypeMapper.entityToDtoInfo(meritComponentTypeList.get(0));
        } else return null;
    }
}
