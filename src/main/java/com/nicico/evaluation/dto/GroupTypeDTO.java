package com.nicico.evaluation.dto;

import io.swagger.annotations.ApiModel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.util.List;

@Getter
@Setter
@Accessors(chain = true)
public abstract class GroupTypeDTO {

    @Min(value =0, message = " وزن باید عددی بین صفر تا 100 باشد")
    @Max(value = 100, message = " وزن باید عددی بین صفر تا 100 باشد")
    @NotNull
    private Long weight;
    @NotNull
    private Long kpiTypeId;
    @NotNull
    private Long groupId;
    @NotNull
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
