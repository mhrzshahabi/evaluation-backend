package com.nicico.evaluation.mapper;

import com.nicico.evaluation.dto.SensitiveEventsDTO;
import com.nicico.evaluation.model.SensitiveEventPersonView;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

import java.util.List;

@Mapper(componentModel = "spring")
public interface SensitiveEventPersonViewMapper {

    @Mappings({
            @Mapping(target = "eventPolicyCatalog", source = "eventPolicyCatalog.title"),
            @Mapping(target = "typeCatalog", source = "typeCatalog.title"),
            @Mapping(target = "statusCatalog", source = "statusCatalog.title")
    })
    SensitiveEventsDTO.SensitiveEventPersonInfo entityToDtoInfo(SensitiveEventPersonView entity);

    List<SensitiveEventsDTO.SensitiveEventPersonInfo> entityToDtoInfoList(List<SensitiveEventPersonView> entities);

}
