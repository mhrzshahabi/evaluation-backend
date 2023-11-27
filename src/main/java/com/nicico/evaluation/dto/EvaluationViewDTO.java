package com.nicico.evaluation.dto;

import io.swagger.annotations.ApiModel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.util.List;

@Getter
@Setter
@Accessors(chain = true)
public class EvaluationViewDTO {

    @Getter
    @Setter
    @ApiModel(" EvaluationViewInfo")
    public static class Info extends EvaluationViewDTO {
        private String assessFullName;
        private String assessNationalCode;
        private String assessPostCode;
        private String assessPostTitle;
        private String assessorFullName;
        private String assessorNationalCode;
        private String assessorPostCode;
        private String assessorPostTitle;
        private String evaluationPeriodTitle;
        private String evaluationPeriodStartDateAssessment;
        private String evaluationPeriodEndDateAssessment;
        private Long averageScore;
        private String costCenterCode;
        private String costCenterTitle;
        private String postGradeTitle;
        private String mojtamaTitle;
        private String moavenatTitle;
        private String omoorTitle;
        private String ghesmatTitle;
        private String description;
        private String statusCatalogTitle;

    }

    @Getter
    @Setter
    @Accessors(chain = true)
    @ApiModel("EvaluationSpecResponse")
    public static class SpecResponse {
        private Response response;
    }

    @Getter
    @Setter
    @Accessors(chain = true)
    @ApiModel(" EvaluationViewResponse")
    public static class Response {
        private List<?> data;
        private Integer status;
        private Integer startRow;
        private Integer endRow;
        private Integer totalRows;
    }

}
