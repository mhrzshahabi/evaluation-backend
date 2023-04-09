package com.nicico.evaluation.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
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
public abstract class GroupGradeDTO {

    private List<Long> gradeIds;
    private Long groupId;
    private String title;
    private String code;

    @Getter
    @Setter
    @ApiModel("GroupGradeInfo")
    public static class Info extends GroupGradeDTO {

        private GroupDTO.Info group;
        private GradeDTO.Info grade;

        private Date createdDate;
        private String createdBy;
        private Date lastModifiedDate;
        private String lastModifiedBy;
    }

    @Getter
    @Setter
    @Accessors(chain = true)
    @ApiModel("GroupGradeCreateRq")
    public static class Create extends GroupGradeDTO {

    }

    @Getter
    @Setter
    @Accessors(chain = true)
    @ApiModel("GroupGradeUpdateRq")
    public static class Update extends GroupGradeDTO {

        private Long id;
        @NotNull
        @ApiModelProperty(required = true)
        private Integer version;
    }

    @Getter
    @Setter
    @Accessors(chain = true)
    @ApiModel("GroupGradeDeleteRq")
    public static class Delete extends GroupGradeDTO {

        private Long id;
    }

}
