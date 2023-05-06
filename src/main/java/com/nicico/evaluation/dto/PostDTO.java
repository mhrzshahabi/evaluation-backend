package com.nicico.evaluation.dto;

import io.swagger.annotations.ApiModel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.util.List;

@Getter
@Setter
@Accessors(chain = true)
public abstract class PostDTO {

    @Getter
    @Setter
    @ApiModel("PostInfo")
    public static class Info extends PostDTO {

        private Long id;
        private Long postId;
        private Long postGradeId;
        private String postGradeCode;
        private String postGradeTitle;
        private String postGradeCodeParent;
        private String postGradeTitleParent;
        private String postGroupCode;
        private String postCode;
        private String postTitle;
        private Long postParentId;
        private Long postLevel;
        private String postGullPathCode;
        private Long postMosavab;
        private Long postCompanyId;
        private String postCompanyCode;
        private String postCompanyName;
        private Long costCenterId;
        private String costCenterCode;
        private String costCenterTitle;
        private String hozeCode;
        private String hozeTitle;
        private String mojtamaCode;
        private String mojtamaTitle;
        private String omoorCode;
        private String omoorTitle;
        private String moavenatCode;
        private String moavenatTitle;
        private String ghesmatCode;
        private String ghesmatTitle;
        private String vahedCode;
        private String vahedTitle;
    }

    @Getter
    @Setter
    @Accessors(chain = true)
    @ApiModel("PostInfoEvaluationPeriod")
    public static class InfoEvaluationPeriod extends PostDTO {
        private Long id;
        private Long postId;
        private String postCode;
        private String postTitle;
        private Long postParentId;
        private Long postLevel;
    }

    @Getter
    @Setter
    @Accessors(chain = true)
    @ApiModel("PostSpecResponse")
    public static class SpecResponse {
        private Response response;
    }

    @Getter
    @Setter
    @Accessors(chain = true)
    @ApiModel("PostResponse")
    public static class Response {
        private List<Info> data;
        private Integer status;
        private Integer startRow;
        private Integer endRow;
        private Integer totalRows;
    }
}
