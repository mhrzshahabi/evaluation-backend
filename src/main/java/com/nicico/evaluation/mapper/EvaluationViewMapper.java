package com.nicico.evaluation.mapper;

import com.nicico.evaluation.dto.EvaluationDTO;
import com.nicico.evaluation.iservice.IPostService;
import com.nicico.evaluation.model.EvaluationView;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.Named;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;

import java.util.List;

@Mapper(componentModel = "spring")
public abstract class EvaluationViewMapper {

    @Lazy
    @Autowired
    private IPostService postService;

    public abstract EvaluationDTO.Info entityToDtoInfo(EvaluationView evaluationView);

    public abstract List<EvaluationDTO.Info> entityToDtoInfoList(List<EvaluationView> evaluationViews);

    @Mappings({
            @Mapping(target = "assessPostTitle", source = "evaluationView.assessPostCode", qualifiedByName = "getPostTitle"),
            @Mapping(target = "assessorPostTitle", source = "evaluationView.assessorPostCode", qualifiedByName = "getPostTitle"),
            @Mapping(target = "evaluationPeriodTitle", source = "evaluationView.evaluationPeriod.title"),
            @Mapping(target = "evaluationPeriodStartDateAssessment", source = "evaluationView.evaluationPeriod.startDateAssessment"),
            @Mapping(target = "evaluationPeriodEndDateAssessment", source = "evaluationView.evaluationPeriod.endDateAssessment"),
            @Mapping(target = "statusCatalogTitle", source = "evaluationView.statusCatalog.title")
    })
    public abstract EvaluationDTO.Excel entityToDtoExcel(EvaluationView evaluationView);

    @Named("getPostTitle")
    String getPostTitle(String postCode) {
        return postService.getByPostCode(postCode).getPostTitle();
    }
}
