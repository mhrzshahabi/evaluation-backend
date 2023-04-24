package com.nicico.evaluation.dto;

import io.swagger.annotations.ApiModel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.util.List;

@Getter
@Setter
@Accessors(chain = true)
public abstract class MeritComponentTypeDTO {

    private Long meritComponentId;
    private Long kpiTypeId;

    @Getter
    @Setter
    @ApiModel("MeritComponentTypeInfo")
    public static class Info extends MeritComponentTypeDTO {

        private KPITypeDTO.Info kpiType;
        private Long id;
    }

    @Getter
    @Setter
    @Accessors(chain = true)
    @ApiModel("MeritComponentTypeCreateRq")
    public static class Create extends MeritComponentTypeDTO {

    }

    @Getter
    @Setter
    @Accessors(chain = true)
    @ApiModel("MeritComponentTypeUpdateRq")
    public static class Update extends MeritComponentTypeDTO {

        private Long id;
    }

    @Getter
    @Setter
    @Accessors(chain = true)
    @ApiModel("MeritComponentTypeDeleteRq")
    public static class Delete extends MeritComponentTypeDTO {

        private Long id;
    }

    @Getter
    @Setter
    @Accessors(chain = true)
    @ApiModel("MeritComponentTypeSpecResponse")
    public static class SpecResponse {
        private Response response;
    }

    @Getter
    @Setter
    @Accessors(chain = true)
    @ApiModel("MeritComponentTypeResponse")
    public static class Response {
        private List<Info> data;
        private Integer status;
        private Integer startRow;
        private Integer endRow;
        private Integer totalRows;
    }

}
