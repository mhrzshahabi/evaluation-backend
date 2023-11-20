package com.nicico.evaluation.dto;

import io.swagger.annotations.ApiModel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.util.List;

@Getter
@Setter
@Accessors(chain = true)
public class EvaluationGeneralReportDTO {

    @Getter
    @Setter
    @ApiModel("EvaluationGeneralReportInfo")
    public static class Info extends EvaluationGeneralReportDTO {
        private Long id;
        private EvaluationPeriodTupleDTO evaluationPeriod;

        private String assessFullName;
        private String assessPostTitle;
        private String assessPostCode;
        private String personnelCode;
        private String costCenterCode;
        private String costCenterTitle;
        private String postGradeTitle;
        private String mojtamaTitle;
        private String moavenatTitle;
        private String omoorTitle;
        private String ghesmatTitle;

        private Long averageScore;
        private Long statusCatalogId;
        private Long evaluationPeriodId;

        private String avgBehavioral;
        private String weightBehavioral;
        private String avgDevelopment;
        private String weightDevelopment;
        private String avgOperational;
        private String weightOperational;

        private Long countItem;
    }

    @Getter
    @Setter
    @ApiModel("EvaluationGeneralReportExcel")
    public static class Excel {
        private String assessFullName;
        private String assessPostCode;
        private String assessPostTitle;
        private String assessorFullName;
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
    @ApiModel("EvaluationPeriodGeneralReportRq")
    public static class EvaluationPeriodTupleDTO {
        private Long id;
        private String title;
        private String startDateAssessment;
        private String endDateAssessment;
    }

    @Getter
    @Setter
    @Accessors(chain = true)
    @ApiModel("EvaluationGeneralReportSpecResponse")
    public static class SpecResponse {
        private Response response;
    }

    @Getter
    @Setter
    @Accessors(chain = true)
    @ApiModel("EvaluationGeneralReportResponse")
    public static class Response {
        private List<?> data;
        private Integer status;
        private Integer startRow;
        private Integer endRow;
        private Integer totalRows;
    }
}
