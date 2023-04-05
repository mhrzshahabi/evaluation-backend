package com.nicico.evaluation.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import javax.validation.constraints.Max;
import javax.validation.constraints.NotNull;


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
    @ApiModel("GroupeCreateRq")
    public static class Create extends GroupDTO{
    }

    @Getter
    @Setter
    @ApiModel("GroupInfo")
    public static class Info extends GroupDTO{
        private Long id;
    }

    @Getter
    @Setter
    @Accessors(chain = true)
    @ApiModel("GroupUpdateRq")
    public static class Update extends GroupDTO {
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
        private Long id;
    }
}
