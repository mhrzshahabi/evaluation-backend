package com.nicico.evaluation.dto;

import io.swagger.annotations.ApiModel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.List;
import java.util.Set;

@Getter
@Setter
@Accessors(chain = true)
public class EvaluationPeriodDTO {

    @NotNull
    private String title;
    @NotNull
    private Date startDate;
    @NotNull
    private Date endDate;
    @NotNull
    private Date startDateAssessment;
    @NotNull
    private Date endDateAssessment;
    private String description;

    private Date validationStartDate;
    private Date validationEndDate;

    @Getter
    @Setter
    @Accessors(chain = true)
    @ApiModel("EvaluationPeriodInfoRq")
    public static class Info extends EvaluationPeriodDTO {
        private Long id;
        private CatalogDTO.Info statusCatalog;
    }

    @Getter
    @Setter
    @Accessors(chain = true)
    @ApiModel("EvaluationPeriodInfoRq")
    public static class InfoWithPostInfoEvaluationPeriod extends EvaluationPeriodDTO {
        private Long id;
        private List<EvaluationPeriodPostDTO.PostInfoEvaluationPeriod> postInfoEvaluationPeriod;
    }

    @Getter
    @Setter
    @Accessors(chain = true)
    @ApiModel("EvaluationPeriodRq")
    public static class InfoWithPost extends EvaluationPeriodDTO {
        private Long id;
        private CatalogDTO.PureInfo statusCatalog;
        private List<EvaluationPeriodPostDTO.Info> evaluationPeriodPostList;
    }

    @Getter
    @Setter
    @Accessors(chain = true)
    @ApiModel("EvaluationPeriodCreateRq")
    public static class Create extends EvaluationPeriodDTO {
        private Set<String> postCode;
    }

    @Getter
    @Setter
    @Accessors(chain = true)
    @ApiModel("EvaluationPeriodCreatePostRq")
    public static class CreatePost {
        private Long id;
        private Set<String> postCode;
    }

    @Getter
    @Setter
    @Accessors(chain = true)
    @ApiModel("EvaluationPeriodUpdateRq")
    public static class Update extends EvaluationPeriodDTO {

    }

    @Getter
    @Setter
    @Accessors(chain = true)
    @ApiModel("EvaluationPeriodSpecResponseRq")
    public static class SpecResponse {
        private EvaluationPeriodDTO.Response response;
    }

    @Getter
    @Setter
    @Accessors(chain = true)
    @ApiModel("EvaluationPeriodResponse")
    public static class Response {
        private List<Info> data;
        private Integer status;
        private Integer startRow;
        private Integer endRow;
        private Integer totalRows;
    }

    public interface RemainDate {

        String getPeriodTitle();

        String getRemainDate();
    }

    @Getter
    @Setter
    @ApiModel("EvaluationPeriodExcel")
    public static class Excel {
        private Long id;
        private String title;
        private String startDate;
        private String endDate;
        private String startDateAssessment;
        private String endDateAssessment;
        private String description;
        private String validationStartDate;
        private String validationEndDate;
        private String postCode;
        private String statusCatalog;
    }

    @Getter
    @Setter
    @ApiModel("EvaluationPeriodDateInfo")
    public static class DateInfo {
        private String startDate;
        private String endDate;
    }

}
