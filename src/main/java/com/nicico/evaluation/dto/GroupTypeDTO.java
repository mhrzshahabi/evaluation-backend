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
public abstract class GroupTypeDTO {

    private Long weight;
    private Long typeId;
    private Long groupId;

    @Getter
    @Setter
    @ApiModel("GroupTypeInfo")
    public static class Info extends GroupTypeDTO {

        private GroupTypeDTO.Info kpiType;
        private GroupDTO.Info group;

        private Date createdDate;
        private String createdBy;
        private Date lastModifiedDate;
        private String lastModifiedBy;
    }

    @Getter
    @Setter
    @Accessors(chain = true)
    @ApiModel("GroupTypeCreateRq")
    public static class Create extends GroupTypeDTO {

    }

    @Getter
    @Setter
    @Accessors(chain = true)
    @ApiModel("GroupTypeUpdateRq")
    public static class Update extends GroupTypeDTO {

        private Long id;
        @NotNull
        @ApiModelProperty(required = true)
        private Integer version;
    }

    @Getter
    @Setter
    @Accessors(chain = true)
    @ApiModel("GroupTypeDeleteRq")
    public static class Delete extends GroupTypeDTO {

        private Long id;
    }

}
