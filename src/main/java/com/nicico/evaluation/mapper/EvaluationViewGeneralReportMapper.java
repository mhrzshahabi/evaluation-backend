package com.nicico.evaluation.mapper;

import com.nicico.evaluation.dto.EvaluationDTO;
import com.nicico.evaluation.model.EvaluationGeneralReportView;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

import java.util.List;

@Mapper(componentModel = "spring")
public interface EvaluationViewGeneralReportMapper {

    EvaluationDTO.Info entityToDtoInfo(EvaluationGeneralReportView evaluationView);

    List<EvaluationDTO.Info> entityToDtoInfoList(List<EvaluationGeneralReportView> evaluationViews);

    @Mappings({
            @Mapping(target = "assessPostTitle", source = "postTitle"),
            @Mapping(target = "evaluationPeriodTitle", source = "evaluationView.evaluationPeriod.title"),
            @Mapping(target = "evaluationPeriodStartDateAssessment", source = "evaluationView.evaluationPeriod.startDateAssessment"),
            @Mapping(target = "evaluationPeriodEndDateAssessment", source = "evaluationView.evaluationPeriod.endDateAssessment"),
            @Mapping(target = "statusCatalogTitle", source = "evaluationView.statusCatalog.title")
    })
    EvaluationDTO.Excel entityToDtoExcel(EvaluationGeneralReportView evaluationView);

    @Mappings({
            @Mapping(target = "evaluationPeriodTitle", source = "evaluationView.evaluationPeriod.title"),
            @Mapping(target = "evaluationPeriodStartDateAssessment", source = "evaluationView.evaluationPeriod.startDateAssessment"),
            @Mapping(target = "evaluationPeriodEndDateAssessment", source = "evaluationView.evaluationPeriod.endDateAssessment"),
            @Mapping(target = "statusCatalogTitle", source = "evaluationView.statusCatalog.title")
    })
    EvaluationDTO.Excel infoDtoToDtoExcel(EvaluationDTO.Info evaluationView);

    List<EvaluationDTO.Excel> infoDtoToDtoExcelList(List<EvaluationDTO.Info> evaluationViewList);

}
