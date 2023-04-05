package com.nicico.evaluation.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotNull;
import java.util.Date;

@Getter
@Setter
@Accessors(chain = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public abstract class MeritComponentTypeDTO {

    private String meritComponentId;
    private String kpiTypeId;

    @Getter
    @Setter
    @ApiModel("MeritComponentTypeInfo")
    public static class Info extends MeritComponentTypeDTO {

        private Date createdDate;
        private String createdBy;
        private Date lastModifiedDate;
        private String lastModifiedBy;
    }

    @Getter
    @Setter
    @Accessors(chain = true)
    @ApiModel("MeritComponentTypeCreateRq")
    public static class Create extends MeritComponentTypeDTO {

    }

    @Getter
    @Setter
    @Accessors(chain = true)
    @ApiModel("MeritComponentTypeUpdateRq")
    public static class Update extends MeritComponentTypeDTO {

        private Long id;
        @NotNull
        @ApiModelProperty(required = true)
        private Integer version;
    }

    @Getter
    @Setter
    @Accessors(chain = true)
    @ApiModel("MeritComponentTypeDeleteRq")
    public static class Delete extends MeritComponentTypeDTO {

        private Long id;
    }

}
