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

    @NotNull
    private Long groupId;

    @Getter
    @Setter
    @ApiModel("GroupGradeInfo")
    public static class Info extends GroupGradeDTO {

        private Long id;
        private String code;
        private String title;
        private GroupDTO.Info group;
        private GradeDTO.Info grade;
    }

    @Getter
    @Setter
    @Accessors(chain = true)
    @ApiModel("GroupGradeCreateAllRq")
    public static class CreateAll extends GroupGradeDTO {

        @NotNull
        private List<String> gradeCodes;
    }

    @Getter
    @Setter
    @Accessors(chain = true)
    @ApiModel("GroupGradeCreateRq")
    public static class Create extends GroupGradeDTO {

        @NotNull
        private String code;
        @NotNull
        private String title;
        @NotNull
        private Long gradeId;
    }

    @Getter
    @Setter
    @Accessors(chain = true)
    @ApiModel("GroupGradeUpdateRq")
    public static class Update extends GroupGradeDTO {

        private Long id;
    }

    @Getter
    @Setter
    @Accessors(chain = true)
    @ApiModel("GroupGradeDeleteRq")
    public static class Delete extends GroupGradeDTO {

        private Long id;
    }

}
