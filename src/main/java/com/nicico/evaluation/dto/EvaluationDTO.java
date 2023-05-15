package com.nicico.evaluation.dto;

import io.swagger.annotations.ApiModel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.util.Date;
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
    private Date startDate;
    @NotNull
    private Date endDate;
    @NotNull
    private Long averageScore;
    @NotNull
    private Long statusCatalogId;
    @NotNull
    private Long evaluationPeriodId;

    @Getter
    @Setter
    @ApiModel("EvaluationExcel")
    public static class Excel {
        private Long id;
        private String code;
        private String title;
    }

    @Getter
    @Setter
    @Accessors(chain = true)
    @ApiModel("EvaluationCreateRq")
    public static class Create extends EvaluationDTO {
    }

    @Getter
    @Setter
    @ApiModel("EvaluationInfo")
    public static class Info extends EvaluationDTO {
        private Long id;
        private CatalogDTO.Info methodCatalog;
        private CatalogDTO.Info statusCatalog;
        private EvaluationPeriodTupleDTO evaluationPeriod;
        private String fullName;

    }


    @Getter
    @Setter
    @Accessors(chain = true)
    @ApiModel("EvaluationUpdateRq")
    public static class EvaluationPeriodTupleDTO {
        private Long id;
        private String title;
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
    @ApiModel("EvaluationDeleteRq")
    public static class Delete {
        @NotNull
        @Min(1)
        private Long id;
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
        private List<Info> data;
        private Integer status;
        private Integer startRow;
        private Integer endRow;
        private Integer totalRows;
    }

}
