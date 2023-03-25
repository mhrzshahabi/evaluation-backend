package com.nicico.evaluation.equipment.standard_test_status;


import org.mapstruct.Mapper;

import java.util.List;


@Mapper(componentModel = "spring"  )

public interface StandardTestStatusMapper {


    StandardTestStatus toStandardTest(StandardTestStatusDTO standardTestDTO);
    List<StandardTestStatus> toStandardTestList (List<StandardTestStatusDTO> standardTestDTOList);


        StandardTestStatusDTO toStandardTestDTO (StandardTestStatus standardTest);
    List<StandardTestStatusDTO> toStandardTestDTOList (List<StandardTestStatus> standardTestList);






}
