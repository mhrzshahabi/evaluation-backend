package com.nicico.evaluation.dto;

import io.swagger.annotations.ApiModel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.util.List;

@Getter
@Setter
@Accessors(chain = true)
public abstract class GradeDTO {
    private Long id;
    private String code;
    private String title;

    @Getter
    @Setter
    @ApiModel("GradeInfo")
    public static class Info extends GradeDTO {
        private Long groupId;
        private GroupDTO group;
    }

    @Getter
    @Setter
    @Accessors(chain = true)
    @ApiModel("GradeSpecResponse")
    public static class SpecResponse {
        private Response response;
    }

    @Getter
    @Setter
    @Accessors(chain = true)
    @ApiModel("GradeResponse")
    public static class Response {
        private List<Info> data;
        private Integer status;
        private Integer startRow;
        private Integer endRow;
        private Integer totalRows;
    }
}
