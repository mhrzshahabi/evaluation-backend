package com.nicico.evaluation.dto;

import io.swagger.annotations.ApiModel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.util.List;

@Getter
@Setter
@Accessors(chain = true)
public abstract class CostCenterDTO {

    @Getter
    @Setter
    @ApiModel("DepartmentInfo")
    public static class Info {
        private Long id;
        private String costCenterCode;
        private String costCenterTitle;
    }

    @Getter
    @Setter
    @Accessors(chain = true)
    @ApiModel("DepartmentSpecResponse")
    public static class SpecResponse {
        private Response response;
    }

    @Getter
    @Setter
    @Accessors(chain = true)
    @ApiModel("DepartmentResponse")
    public static class Response {
        private List<Info> data;
        private Integer status;
        private Integer startRow;
        private Integer endRow;
        private Integer totalRows;
    }
}
