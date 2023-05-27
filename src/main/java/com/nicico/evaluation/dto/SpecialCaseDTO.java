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
    @NotNull
    private String assessorNationalCode;
    @NotNull
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
}
    