package com.nicico.evaluation.mapper;

import com.nicico.evaluation.dto.SensitiveEventPersonDTO;
import com.nicico.evaluation.iservice.IPersonService;
import com.nicico.evaluation.model.SensitiveEventPerson;
import org.mapstruct.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@Mapper(componentModel = "spring")
public abstract class SensitiveEventPersonMapper {

    @Autowired
    IPersonService personService;

    public abstract SensitiveEventPerson dtoCreateToEntity(SensitiveEventPersonDTO.Create dto);

    @Mappings({
            @Mapping(target = "sensitiveEvent.eventDate", ignore = true),
            @Mapping(target = "sensitiveEvent.toDate", ignore = true),
            @Mapping(target = "firstName", source = "nationalCode", qualifiedByName = "getPersonFirstName"),
            @Mapping(target = "lastName", source = "nationalCode", qualifiedByName = "getPersonLastName")
    })
    public abstract SensitiveEventPersonDTO.Info entityToDtoInfo(SensitiveEventPerson entity);

    public abstract List<SensitiveEventPersonDTO.Info> entityToDtoInfoList(List<SensitiveEventPerson> entities);

    public abstract void update(@MappingTarget SensitiveEventPerson entity, SensitiveEventPersonDTO.Update dto);

    @Named("getPersonLastName")
    String getPersonLastName(String nationalCode) {
        return personService.getByNationalCode(nationalCode).getLastName();
    }

    @Named("getPersonFirstName")
    String getPersonFirstName(String nationalCode) {
        return personService.getByNationalCode(nationalCode).getFirstName();
    }
}
