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
        private String postCompanyName;
        private String costCenterCode;
        private String costCenterTitle;
        private String mojtamaTitle;
        private String omoorTitle;
        private String ghesmatTitle;
        private String vahedTitle;
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
