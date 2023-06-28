package com.nicico.evaluation.dto;

import io.swagger.annotations.ApiModel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.util.List;

@Getter
@Setter
@Accessors(chain = true)
public class PostRelationDTO {

    @Getter
    @Setter
    @Accessors(chain = true)
    @ApiModel("PostRelationInfo")
    public static class Info {
        private Long id;
        private String postCode;
        private String postTitle;
        private String postGroupCode;
        private String postCodeParent;
        private String postTitleParent;
        private String postCodeGrade;
        private String postTitleGrade;
        private String postCodeGradeParent;
        private String postTitleGradeParent;
        private String postMeritIds;
        private String mojtameTitle;
        private String moavenatTitle;
        private String omorTitle;
        private String ghesmatTitle;
        private String vahedTitle;
    }

    @Getter
    @Setter
    @Accessors(chain = true)
    @ApiModel("PostRelationSpecResponse")
    public static class SpecResponse {
        private PostRelationDTO.Response response;
    }

    @Getter
    @Setter
    @Accessors(chain = true)
    @ApiModel("PostRelationResponse")
    public static class Response {
        private List<Info> data;
        private Integer status;
        private Integer startRow;
        private Integer endRow;
        private Integer totalRows;
    }
}
