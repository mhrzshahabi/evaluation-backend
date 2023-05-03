package com.nicico.evaluation.dto;

import io.swagger.annotations.ApiModel;
import lombok.*;
import lombok.experimental.Accessors;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.List;

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

    @Getter
    @Setter
    @Accessors(chain = true)
    @ApiModel("EvaluationPeriodInfoRq")
    public static class Info extends EvaluationPeriodDTO {
        private Long id;
    }

    @Getter
    @Setter
    @Accessors(chain = true)
    @ApiModel("EvaluationPeriodCreateRq")
    public static class Create extends EvaluationPeriodDTO {

    }

    @Getter
    @Setter
    @Accessors(chain = true)
    @ApiModel("EvaluationPeriodUpdateRq")
    public static class Update extends EvaluationPeriodDTO {

    }

    public static class Delete {
        @Min(1)
        private Long id;
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
        private List<EvaluationPeriodDTO.Info> data;
        private Integer status;
        private Integer startRow;
        private Integer endRow;
        private Integer totalRows;
    }

}
