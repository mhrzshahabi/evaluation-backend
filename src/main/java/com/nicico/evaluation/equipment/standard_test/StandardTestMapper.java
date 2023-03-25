package com.nicico.evaluation.equipment.standard_test;


import org.mapstruct.Mapper;

import java.util.List;


@Mapper(componentModel = "spring"  )

public interface StandardTestMapper {


    StandardTest toStandardTest(StandardTestDTO standardTestDTO);
    List<StandardTest> toStandardTestList (List<StandardTestDTO> standardTestDTOList);


        StandardTestDTO toStandardTestDTO (StandardTest standardTest);
    List<StandardTestDTO> toStandardTestDTOList (List<StandardTest> standardTestList);






}
