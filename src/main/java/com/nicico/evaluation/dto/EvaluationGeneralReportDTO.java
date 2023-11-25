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
    public static class Info {
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
        private String avgDevelopment;
        private String avgOperational;
        private String weightBehavioral;
        private String weightDevelopment;
        private String weightOperational;

        private Long countItem;
    }

    @Getter
    @Setter
    @ApiModel("EvaluationGeneralReportDetailInfo")
    public static class DetailInfo {

        Long evalId;
        String meritCode;
        String meritTitle;
        String effectiveScore;
        String kpiTitle;
        String description;
        String meritWeight;
    }


    @Getter
    @Setter
    @ApiModel("EvaluationGeneralReportExcel")
    public static class Excel {
        private String assessFullName;
        private String assessPostCode;
        private String personnelCode;
        private String costCenterCode;
        private String costCenterTitle;
        private String mojtamaTitle;
        private String moavenatTitle;
        private String omoorTitle;
        private String ghesmatTitle;
        private Long averageScore;
        private String avgBehavioral;
        private String avgDevelopment;
        private String avgOperational;
        private String weightBehavioral;
        private String weightDevelopment;
        private String weightOperational;
        private Long countItem;

        Long   evalId;
        String meritCode;
        String meritTitle;
        String effectiveScore;
        String kpiTitle;
        String description;
        String meritWeight;

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
