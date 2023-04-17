package com.nicico.evaluation.dto;

import io.swagger.annotations.ApiModel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.util.List;

@Getter
@Setter
@Accessors(chain = true)
public class InstanceDTO {

    @NotNull
    private String code;
    @NotNull
    private String title;

    @Getter
    @Setter
    @Accessors(chain = true)
    @ApiModel("InstanceCreateRq")
    public static class Create extends InstanceDTO {
    }

    @Getter
    @Setter
    @ApiModel("InstanceInfo")
    public static class Info extends InstanceDTO {
        private Long id;
    }

    @Getter
    @Setter
    @Accessors(chain = true)
    @ApiModel("InstanceUpdateRq")
    public static class Update extends InstanceDTO {
        @NotNull
        @Min(1)
        private Long id;
    }

    @Getter
    @Setter
    @Accessors(chain = true)
    @ApiModel("InstanceDeleteRq")
    public static class Delete {
        @NotNull
        @Min(1)
        private Long id;
    }

    @Getter
    @Setter
    @Accessors(chain = true)
    @ApiModel("InstanceSpecResponse")
    public static class SpecResponse {
        private Response response;
    }

    @Getter
    @Setter
    @Accessors(chain = true)
    @ApiModel("InstanceResponse")
    public static class Response {
        private List<Info> data;
        private Integer status;
        private Integer startRow;
        private Integer endRow;
        private Integer totalRows;
    }

}
