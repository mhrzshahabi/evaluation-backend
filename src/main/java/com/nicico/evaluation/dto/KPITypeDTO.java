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
public abstract class KPITypeDTO {

    private String code;
    private String title;
    private Long levelDef;

    @Getter
    @Setter
    @ApiModel("KPITypeInfo")
    public static class Info extends KPITypeDTO {

        private Date createdDate;
        private String createdBy;
        private Date lastModifiedDate;
        private String lastModifiedBy;
    }

    @Getter
    @Setter
    @Accessors(chain = true)
    @ApiModel("KPITypeCreateRq")
    public static class Create extends KPITypeDTO {

    }

    @Getter
    @Setter
    @Accessors(chain = true)
    @ApiModel("KPITypeUpdateRq")
    public static class Update extends KPITypeDTO {

        private Long id;
        @NotNull
        @ApiModelProperty(required = true)
        private Integer version;
    }

    @Getter
    @Setter
    @Accessors(chain = true)
    @ApiModel("KPITypeDeleteRq")
    public static class Delete extends KPITypeDTO {

        private Long id;
    }

}
