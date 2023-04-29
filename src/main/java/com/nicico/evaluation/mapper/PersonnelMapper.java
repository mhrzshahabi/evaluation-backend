package com.nicico.evaluation.mapper;

import com.nicico.evaluation.dto.PersonnelDTO;
import com.nicico.evaluation.model.Personnel;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public  interface PersonnelMapper {

      PersonnelDTO.Info entityToDtoInfo(Personnel entity);

      List<PersonnelDTO.Info> entityToDtoInfoList(List<Personnel> entities);


}
