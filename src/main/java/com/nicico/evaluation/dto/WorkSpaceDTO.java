package com.nicico.evaluation.dto;

import io.swagger.annotations.ApiModel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Getter
@Setter
@Accessors(chain = true)
public abstract class WorkSpaceDTO {

    @Getter
    @Setter
    @ApiModel("WorkSpaceInfo")
    public static class Info {

        private String code;
        private String title;
        private String description;
        private Integer number;
    }

//    @Getter
//    @Setter
//    @Accessors(chain = true)
//    @ApiModel("WorkSpaceSpecResponse")
//    public static class SpecResponse {
//        private Response response;
//    }
//
//    @Getter
//    @Setter
//    @Accessors(chain = true)
//    @ApiModel("WorkSpaceResponse")
//    public static class Response {
//        private List<Info> data;
//        private Integer status;
//        private Integer startRow;
//        private Integer endRow;
//        private Integer totalRows;
//    }

}
