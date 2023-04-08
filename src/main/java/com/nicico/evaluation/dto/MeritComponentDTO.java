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
public abstract class MeritComponentDTO {

    private String code;
    private String title;

    @Getter
    @Setter
    @ApiModel("MeritComponentInfo")
    public static class Info extends MeritComponentDTO {

        private Long id;
        private Date createdDate;
        private String createdBy;
        private Date lastModifiedDate;
        private String lastModifiedBy;
    }

    @Getter
    @Setter
    @Accessors(chain = true)
    @ApiModel("MeritComponentCreateRq")
    public static class Create extends MeritComponentDTO {

    }

    @Getter
    @Setter
    @Accessors(chain = true)
    @ApiModel("MeritComponentUpdateRq")
    public static class Update extends MeritComponentDTO {

        private Long id;
        @NotNull
        @ApiModelProperty(required = true)
        private Integer version;
    }

    @Getter
    @Setter
    @Accessors(chain = true)
    @ApiModel("MeritComponentDeleteRq")
    public static class Delete extends MeritComponentDTO {

        private Long id;
    }

}
