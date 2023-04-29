package com.nicico.evaluation.mapper;

import com.nicico.evaluation.dto.PersonDTO;
import com.nicico.evaluation.model.Person;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public  interface PersonMapper {

      PersonDTO.Info entityToDtoInfo(Person entity);

      List<PersonDTO.Info> entityToDtoInfoList(List<Person> entities);

}
