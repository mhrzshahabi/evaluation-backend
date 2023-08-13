package com.nicico.evaluation.dto;

import io.swagger.annotations.ApiModel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.util.List;


@Getter
@Setter
@Accessors(chain = true)
public abstract class GroupPostDTO {

    @Getter
    @Setter
    @Accessors(chain = true)
    @ApiModel("GroupPostCreateRq")
    public static class Create extends GroupPostDTO {
    }

    @Getter
    @Setter
    @ApiModel("GroupPostInfo")
    public static class Info extends GroupPostDTO {
        private Long id;
        private String groupPostCode;
        private String postTitle;
        private String postGradeTitle;
        private Long totalWeight;
        private Long postGradeId;
        private String mojtamaTitle;
        private String omoorTitle;
        private String ghesmatTitle;
        private String vahedTitle;
        private String postCompanyName;
        private String costCenterCode;
        private String costCenterTitle;
    }

    @Getter
    @Setter
    @ApiModel("GroupPostExcel")
    public static class Excel {
        private String groupPostCode;
        private String postTitle;
        private String postGradeTitle;
        private Long totalWeight;
        private String mojtamaTitle;
        private String omoorTitle;
        private String ghesmatTitle;
        private String vahedTitle;
        private String costCenterCode;
        private String postCompanyName;
    }

    @Getter
    @Setter
    @Accessors(chain = true)
    @ApiModel("GroupPostSpecResponse")
    public static class SpecResponse {
        private Response response;
    }

    @Getter
    @Setter
    @Accessors(chain = true)
    @ApiModel("GroupPostResponse")
    public static class Response {
        private List<Info> data;
        private Integer status;
        private Integer startRow;
        private Integer endRow;
        private Integer totalRows;
    }
}
