package com.nicico.evaluation.mapper;

import com.nicico.evaluation.dto.PersonDTO;
import com.nicico.evaluation.model.Person;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public abstract class PersonMapper {

    public abstract PersonDTO.Info entityToDtoInfo(Person entity);

    public abstract List<PersonDTO.Info> entityToDtoInfoList(List<Person> entities);

}
