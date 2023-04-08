package com.nicico.evaluation.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.util.Date;


@Getter
@Setter
@Accessors(chain = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class GroupDTO {

    private String code;
    private String title;
    private Boolean definitionAllowed;

    @Getter
    @Setter
    @Accessors(chain = true)
    @ApiModel("GroupCreateRq")
    public static class Create extends GroupDTO{
    }

    @Getter
    @Setter
    @ApiModel("GroupInfo")
    public static class Info extends GroupDTO{
        private Long id;

        private Date createdDate;
        private String createdBy;
        private Date lastModifiedDate;
        private String lastModifiedBy;
        private Integer version;
    }

    @Getter
    @Setter
    @Accessors(chain = true)
    @ApiModel("GroupUpdateRq")
    public static class Update extends GroupDTO {
        @NotNull
        @Min(1)
        private Long id;
        @NotNull
        @ApiModelProperty(required = true)
        private Integer version;
    }

    @Getter
    @Setter
    @Accessors(chain = true)
    @ApiModel("GroupDeleteRq")
    public static class Delete {
        @NotNull
        @Min(1)
        private Long id;
    }
}
