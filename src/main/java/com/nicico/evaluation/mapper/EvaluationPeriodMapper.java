package com.nicico.evaluation.mapper;

import com.nicico.evaluation.dto.EvaluationPeriodDTO;
import com.nicico.evaluation.model.EvaluationPeriod;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Mappings;

import java.util.List;

@Mapper(componentModel = "spring", uses = SpecialCaseMapper.class)
public interface EvaluationPeriodMapper {

    @Mappings({
            @Mapping(target = "startDate", source = "startDate", qualifiedByName = "convertDateToString"),
            @Mapping(target = "endDate", source = "endDate", qualifiedByName = "convertDateToString"),
            @Mapping(target = "startDateAssessment", source = "startDateAssessment", qualifiedByName = "convertDateToString"),
            @Mapping(target = "endDateAssessment", source = "endDateAssessment", qualifiedByName = "convertDateToString")
    })
    EvaluationPeriod dtoCreateToEntity(EvaluationPeriodDTO.Create dto);

    @Mappings({
            @Mapping(target = "startDate", source = "startDate", qualifiedByName = "convertStringToDate"),
            @Mapping(target = "endDate", source = "endDate", qualifiedByName = "convertStringToDate"),
            @Mapping(target = "startDateAssessment", source = "startDateAssessment", qualifiedByName = "convertStringToDate"),
            @Mapping(target = "endDateAssessment", source = "endDateAssessment", qualifiedByName = "convertStringToDate")
    })
    EvaluationPeriodDTO.Info entityToDtoInfo(EvaluationPeriod entity);

    List<EvaluationPeriodDTO.Info> entityToDtoInfoList(List<EvaluationPeriod> entities);

    @Mappings({
            @Mapping(target = "startDate", source = "startDate", qualifiedByName = "convertStringToDate"),
            @Mapping(target = "endDate", source = "endDate", qualifiedByName = "convertStringToDate"),
            @Mapping(target = "startDateAssessment", source = "startDateAssessment", qualifiedByName = "convertStringToDate"),
            @Mapping(target = "endDateAssessment", source = "endDateAssessment", qualifiedByName = "convertStringToDate")
    })
    EvaluationPeriodDTO.InfoWithPostInfoEvaluationPeriod entityToDtoInfoWithPostInfoEvaluationPeriod(EvaluationPeriod entity);

    @Mappings({
            @Mapping(target = "startDate", source = "startDate", qualifiedByName = "convertDateToString"),
            @Mapping(target = "endDate", source = "endDate", qualifiedByName = "convertDateToString"),
            @Mapping(target = "startDateAssessment", source = "startDateAssessment", qualifiedByName = "convertDateToString"),
            @Mapping(target = "endDateAssessment", source = "endDateAssessment", qualifiedByName = "convertDateToString")
    })
    void update(@MappingTarget EvaluationPeriod entity, EvaluationPeriodDTO.Update dto);
}
