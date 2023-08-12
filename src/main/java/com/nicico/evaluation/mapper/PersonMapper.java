package com.nicico.evaluation.mapper;

import com.nicico.evaluation.dto.PersonDTO;
import com.nicico.evaluation.model.Person;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

import java.util.List;

@Mapper(componentModel = "spring", uses = SpecialCaseMapper.class)
public interface PersonMapper {

    @Mappings({
            @Mapping(target = "birthDate", source = "birthDate", qualifiedByName = "convertStringToDate"),
            @Mapping(target = "deathDate", source = "deathDate", qualifiedByName = "convertStringToDate")
    })
    PersonDTO.Info entityToDtoInfo(Person entity);

    List<PersonDTO.Info> entityToDtoInfoList(List<Person> entities);

}
