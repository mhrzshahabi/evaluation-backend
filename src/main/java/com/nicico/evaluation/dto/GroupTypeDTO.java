package com.nicico.evaluation.dto;

import io.swagger.annotations.ApiModel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.util.List;

@Getter
@Setter
@Accessors(chain = true)
public abstract class GroupTypeDTO {

    private Long weight;
    private Long kpiTypeId;
    private Long groupId;
    private String code;

    @Getter
    @Setter
    @ApiModel("GroupTypeInfo")
    public static class Info extends GroupTypeDTO {

        private Long id;
        private KPITypeDTO.Info kpiType;
        private GroupDTO.Info group;
    }

    @Getter
    @Setter
    @Accessors(chain = true)
    @ApiModel("GroupTypeCreateRq")
    public static class Create extends GroupTypeDTO {

    }

    @Getter
    @Setter
    @Accessors(chain = true)
    @ApiModel("GroupTypeUpdateRq")
    public static class Update extends GroupTypeDTO {

        private Long id;
    }

    @Getter
    @Setter
    @Accessors(chain = true)
    @ApiModel("GroupTypeDeleteRq")
    public static class Delete extends GroupTypeDTO {

        private Long id;
    }

    @Getter
    @Setter
    @Accessors(chain = true)
    @ApiModel("GroupTypeSpecResponse")
    public static class SpecResponse {
        private Response response;
    }

    @Getter
    @Setter
    @Accessors(chain = true)
    @ApiModel("GroupTypeResponse")
    public static class Response {
        private List<Info> data;
        private Integer status;
        private Integer startRow;
        private Integer endRow;
        private Integer totalRows;
    }

}
