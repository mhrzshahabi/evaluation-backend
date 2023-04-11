package com.nicico.evaluation.dto;

import io.swagger.annotations.ApiModel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import javax.validation.constraints.NotNull;
import java.util.List;

@Getter
@Setter
@Accessors(chain = true)
public abstract class GroupGradeDTO {

    @NotNull
    private Long groupId;

    @Getter
    @Setter
    @ApiModel("GroupGradeInfo")
    public static class Info extends GroupGradeDTO {

        private Long id;
        private String gradeCode;
        private String gradeTitle;
        private GroupDTO.Info group;
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
        private String gradeCode;
        @NotNull
        private String gradeTitle;
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

    @Getter
    @Setter
    @Accessors(chain = true)
    @ApiModel("GroupGradeSpecResponse")
    public static class SpecResponse {
        private Response response;
    }

    @Getter
    @Setter
    @Accessors(chain = true)
    @ApiModel("GroupGradeResponse")
    public static class Response {
        private List<Info> data;
        private Integer status;
        private Integer startRow;
        private Integer endRow;
        private Integer totalRows;
    }

}
