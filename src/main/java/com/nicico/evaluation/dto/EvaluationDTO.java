package com.nicico.evaluation.dto;

import io.swagger.annotations.ApiModel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotNull;
import java.util.List;

@Getter
@Setter
@Accessors(chain = true)
public class EvaluationDTO {

    @NotNull
    private String assessNationalCode;
    @NotNull
    private String assessPostCode;
    @NotNull
    private String assessorNationalCode;
    @NotNull
    private String assessorPostCode;
    @NotNull
    private Long methodCatalogId;
    @NotNull
    private Long averageScore;
    @NotNull
    private Long statusCatalogId;
    @NotNull
    private Long evaluationPeriodId;

    private String description;

    @Getter
    @Setter
    @Accessors(chain = true)
    @ApiModel("EvaluationCreateRq")
    public static class Create extends EvaluationDTO {
    }

    @Getter
    @Setter
    @Accessors(chain = true)
    @ApiModel("EvaluationCreateListRq")
    public static class CreateList {
        private String postCode;
        @NotNull
        private Long evaluationPeriodId;
    }

    @Getter
    @Setter
    @ApiModel("EvaluationInfo")
    public static class Info extends EvaluationDTO {
        private Long id;
        private CatalogDTO.Info methodCatalog;
        private CatalogDTO.Info statusCatalog;
        private EvaluationPeriodTupleDTO evaluationPeriod;

        private String assessFullName;
        private String assessorFullName;
        private String assessPostTitle;
        private String assessorPostTitle;

        private String postGradeTitle;
        private String mojtamaTitle;
        private String moavenatTitle;
        private String omoorTitle;
        private String ghesmatTitle;
        private String personnelCode;
    }

    @Getter
    @Setter
    @ApiModel("EvaluationExcel")
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
    @ApiModel("EvaluationUpdateRq")
    public static class EvaluationPeriodTupleDTO {
        private Long id;
        private String title;
        private String startDateAssessment;
        private String endDateAssessment;
    }

    @Getter
    @Setter
    @Accessors(chain = true)
    @ApiModel("EvaluationUpdateRq")
    public static class Update extends EvaluationDTO {

    }

    @Getter
    @Setter
    @Accessors(chain = true)
    @ApiModel("EvaluationPeriodDashboardRq")
    public static class EvaluationPeriodDashboard {
        private Long id;
        private String title;
    }

    @Getter
    @Setter
    @Accessors(chain = true)
    @ApiModel("EvaluationAverageScoreDataRq")
    public static class EvaluationAverageScoreData {
        private Long averageScore;
        private Long behavioralAverageScore;
        private Long developmentAverageScore;
        private Long operationalAverageScore;
    }

    @Accessors(chain = true)
    @ApiModel("MostParticipationInFinalizedEvaluationRq")
    public interface MostParticipationInFinalizedEvaluation {
        Integer getOmoorFinalizedNumber();

        Setter getOmoorTitle();
    }

    @Getter
    @Setter
    @Accessors(chain = true)
    @ApiModel("EvaluationCostCenterInfo")
    public static class CostCenterInfo {
        private Long evaluationPeriodId;
        private String costCenterCode;
        private String costCenterTitle;
        private Integer personCount;
        private String averageBehavioral;
        private String averageDevelopment;
        private String averageOperational;
        private String averageScore;
    }

    @Getter
    @Setter
    @Accessors(chain = true)
    @ApiModel("EvaluationCostCenterExcel")
    public static class CostCenterExcel {
        private String costCenterCode;
        private String costCenterTitle;
        private Integer personCount;
        private String averageBehavioral;
        private String averageDevelopment;
        private String averageOperational;
        private String averageScore;
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
    @ApiModel("EvaluationResponse")
    public static class Response {
        private List<?> data;
        private Integer status;
        private Integer startRow;
        private Integer endRow;
        private Integer totalRows;
    }

    @Getter
    @Setter
    @Accessors(chain = true)
    @ApiModel("ChangeStatusDTO")
    public static class ChangeStatusDTO {
        @NotNull
        private List<Long> evaluationIds;
        @NotNull
        private String status;
    }

    @Accessors(chain = true)
    @ApiModel("AverageWeightDTO")
    public interface AverageWeightDTO {

        String getWeight();

        String getTitle();
    }

    @Accessors(chain = true)
    @ApiModel("BestAssessAverageScoreDTO")
    public interface BestAssessAverageScoreDTO {

        String getAssessFullName();

        String getPersonnelNo();

        String getPostTitle();

        Long getAverageScore();

        String getAssessNational();
    }

    @Setter
    @Getter
    @Accessors(chain = true)
    @ApiModel("BestAssessAverageScoreDTO")
    public static class ErrorResponseDTO {

        private String message;
        private int status;
        private List<Long> evaluationIds;

    }
}
