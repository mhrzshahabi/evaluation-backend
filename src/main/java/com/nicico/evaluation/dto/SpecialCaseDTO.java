package com.nicico.evaluation.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.List;

@Getter
@Setter
@Accessors(chain = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SpecialCaseDTO {

    private String assessFullName;
    @NotNull
    private String assessNationalCode;
    @NotNull
    private String assessPostCode;
    private String assessRealPostCode;
    private String assessorFullName;
    private String assessorNationalCode;
    private String assessorPostCode;
    @NotNull
    private Date startDate;
    @NotNull
    private Date endDate;

    @Getter
    @Setter
    @Accessors(chain = true)
    @ApiModel("SpecialCaseCreateRq")
    public static class Create extends SpecialCaseDTO {

    }

    @Getter
    @Setter
    @ApiModel("SpecialCaseInfo")
    public static class Info extends SpecialCaseDTO {
        private Long id;
        private CatalogDTO.Info statusCatalog;
    }

    @Getter
    @Setter
    @Accessors(chain = true)
    @ApiModel("SpecialCaseUpdateRq")
    public static class Update extends SpecialCaseDTO {

    }

    @Getter
    @Setter
    @Accessors(chain = true)
    @ApiModel("SpecialCaseDeleteRq")
    public static class Delete {
        @NotNull
        private Long id;
    }

    @Getter
    @Setter
    @Accessors(chain = true)
    @ApiModel("SpecialCaseSpecResponseRq")
    public static class SpecResponse {
        private SpecialCaseDTO.Response response;
    }

    @Getter
    @Setter
    @Accessors(chain = true)
    @ApiModel("SpecialCaseResponse")
    public static class Response {
        private List<Info> data;
        private Integer status;
        private Integer startRow;
        private Integer endRow;
        private Integer totalRows;
    }

    @Getter
    @Setter
    @Accessors(chain = true)
    @ApiModel("SpecialCaseChangeAllStatusDTO")
    public static class ChangeAllStatusDTO {
        @NotNull
        private List<Long> specialCaseIds;
    }

    @Getter
    @Setter
    @Accessors(chain = true)
    @ApiModel("SpecialCaseChangeStatusDTO")
    public static class ChangeStatusDTO {
        @NotNull
        private Long id;
        private String assessFullName;
        private String assessPostCode;

    }

    @Getter
    @Setter
    @Accessors(chain = true)
    @ApiModel("SpecialCaseBatchChangeStatusRq")
    public static class BatchChangeStatus {

        @NotNull
        private List<Long> specialCaseIds;

    }

    @Getter
    @Setter
    @Accessors(chain = true)
    @ApiModel("SpecialCaseBatchCreateRq")
    public static class BatchCreate {

        private String assessFullName;
        @NotNull
        private String assessNationalCode;
        private String assessPostCode;
        @NotNull
        private String assessRealPostCode;
        private String assessorFullName;
        @NotNull
        private String assessorNationalCode;
        private String assessorPostCode;
        @NotNull
        private Date startDate;
        @NotNull
        private Date endDate;

    }
}
    