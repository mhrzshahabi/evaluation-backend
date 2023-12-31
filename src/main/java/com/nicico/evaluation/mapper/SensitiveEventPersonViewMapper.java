package com.nicico.evaluation.mapper;

import com.nicico.evaluation.dto.SensitiveEventsDTO;
import com.nicico.evaluation.model.SensitiveEventPersonView;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

@Mapper(componentModel = "spring", uses = SpecialCaseMapper.class)
public interface SensitiveEventPersonViewMapper {

    @Mappings({
            @Mapping(target = "eventPolicyCatalog", source = "eventPolicyCatalog.title"),
            @Mapping(target = "typeCatalog", source = "typeCatalog.title"),
            @Mapping(target = "statusCatalog", source = "statusCatalog"),
            @Mapping(target = "eventDate", source = "eventDate", qualifiedByName = "convertStringToDate")
    })
    SensitiveEventsDTO.SensitiveEventPersonInfo entityToDtoInfo(SensitiveEventPersonView entity);

    @Mappings({
            @Mapping(target = "eventPolicyCatalog", source = "eventPolicyCatalog.title"),
            @Mapping(target = "typeCatalog", source = "typeCatalog.title"),
            @Mapping(target = "statusCatalog", source = "statusCatalog.title")
    })
    SensitiveEventsDTO.Excel entityToDtoExcel(SensitiveEventPersonView entity);

}
