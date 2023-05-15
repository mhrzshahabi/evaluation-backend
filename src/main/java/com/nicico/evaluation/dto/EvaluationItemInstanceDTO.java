package com.nicico.evaluation.dto;

import io.swagger.annotations.ApiModel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.util.List;

@Getter
@Setter
@Accessors(chain = true)
public abstract class EvaluationItemInstanceDTO {

    @NotNull
    private Long evaluationItemId;
    private Long postMeritInstanceId;
    private Long instanceGroupTypeMeritId;


    @Getter
    @Setter
    @Accessors(chain = true)
    @ApiModel("EvaluationItemInstanceCreateRq")
    public static class Create extends EvaluationItemInstanceDTO {
    }

    @Getter
    @Setter
    @ApiModel("EvaluationItemInstanceInfo")
    public static class Info extends EvaluationItemInstanceDTO {

        private Long id;
    }

    @Getter
    @Setter
    @Accessors(chain = true)
    @ApiModel("EvaluationItemInstanceUpdateRq")
    public static class Update extends EvaluationItemInstanceDTO {

    }

    @Getter
    @Setter
    @Accessors(chain = true)
    @ApiModel("EvaluationItemInstanceDeleteRq")
    public static class Delete {
        @NotNull
        @Min(1)
        private Long id;
    }

    @Getter
    @Setter
    @Accessors(chain = true)
    @ApiModel("EvaluationItemInstanceSpecResponse")
    public static class SpecResponse {
        private Response response;
    }

    @Getter
    @Setter
    @Accessors(chain = true)
    @ApiModel("EvaluationItemInstanceResponse")
    public static class Response {
        private List<Info> data;
        private Integer status;
        private Integer startRow;
        private Integer endRow;
        private Integer totalRows;
    }
}
