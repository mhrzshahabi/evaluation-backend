package com.nicico.evaluation.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.util.List;

@Getter
@Setter
@Accessors(chain = true)
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
