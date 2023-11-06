package com.nicico.evaluation.mapper;

import com.nicico.evaluation.dto.EvaluationDTO;
import com.nicico.evaluation.model.EvaluationView;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

import java.util.List;

@Mapper(componentModel = "spring")
public interface EvaluationViewMapper {

    EvaluationDTO.Info entityToDtoInfo(EvaluationView evaluationView);

    List<EvaluationDTO.Info> entityToDtoInfoList(List<EvaluationView> evaluationViews);

    @Mappings({
            @Mapping(target = "assessPostTitle", source = "postTitle"),
            @Mapping(target = "evaluationPeriodTitle", source = "evaluationView.evaluationPeriod.title"),
            @Mapping(target = "evaluationPeriodStartDateAssessment", source = "evaluationView.evaluationPeriod.startDateAssessment"),
            @Mapping(target = "evaluationPeriodEndDateAssessment", source = "evaluationView.evaluationPeriod.endDateAssessment"),
            @Mapping(target = "statusCatalogTitle", source = "evaluationView.statusCatalog.title")
    })
    EvaluationDTO.Excel entityToDtoExcel(EvaluationView evaluationView);

}
