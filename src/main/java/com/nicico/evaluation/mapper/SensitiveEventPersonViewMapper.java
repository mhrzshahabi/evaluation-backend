package com.nicico.evaluation.mapper;

import com.nicico.evaluation.dto.SensitiveEventsDTO;
import com.nicico.evaluation.iservice.IMeritComponentService;
import com.nicico.evaluation.model.SensitiveEventPersonView;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.Named;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;

import java.util.Objects;

@Mapper(componentModel = "spring", uses = SpecialCaseMapper.class)
public abstract class SensitiveEventPersonViewMapper {

    @Lazy
    @Autowired
    private IMeritComponentService meritComponentService;

    @Mappings({
            @Mapping(target = "eventPolicyCatalog", source = "eventPolicyCatalog.title"),
            @Mapping(target = "typeCatalog", source = "typeCatalog.title"),
            @Mapping(target = "statusCatalog", source = "statusCatalog"),
            @Mapping(target = "eventDate", source = "eventDate", qualifiedByName = "convertStringToDate"),
            @Mapping(target = "meritComponentTitle", source = "meritComponentId", qualifiedByName = "getLastActiveMerit")
    })
    public abstract SensitiveEventsDTO.SensitiveEventPersonInfo entityToDtoInfo(SensitiveEventPersonView entity);

    @Mappings({
            @Mapping(target = "eventPolicyCatalog", source = "eventPolicyCatalog.title"),
            @Mapping(target = "typeCatalog", source = "typeCatalog.title"),
            @Mapping(target = "statusCatalog", source = "statusCatalog.title")
    })
    public abstract SensitiveEventsDTO.Excel entityToDtoExcel(SensitiveEventPersonView entity);

    @Named("getLastActiveMerit")
    String getLastActiveMerit(Long meritComponentId) {
        if (Objects.nonNull(meritComponentId))
            return meritComponentService.findLastActiveByMeritComponentId(meritComponentId).getTitle();
        else
            return null;
    }

}
