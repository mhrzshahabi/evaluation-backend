package com.nicico.evaluation.dto;

import io.swagger.annotations.ApiModel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.util.List;

@Getter
@Setter
@Accessors(chain = true)
public abstract class InstanceGroupTypeMeritDTO {

    private Long instanceId;
    private Long groupTypeMeritId;

    @Getter
    @Setter
    @ApiModel("GroupTypeMeritInfo")
    public static class Info extends InstanceGroupTypeMeritDTO {

        private Long id;
        private GroupTypeMeritDTO.Info groupTypeMerit;
        private InstanceDTO.Info instance;
    }

    @Getter
    @Setter
    @Accessors(chain = true)
    @ApiModel("GroupTypeMeritCreateRq")
    public static class Create extends InstanceGroupTypeMeritDTO {

    }

    @Getter
    @Setter
    @Accessors(chain = true)
    @ApiModel("GroupTypeMeritUpdateRq")
    public static class Update extends InstanceGroupTypeMeritDTO {

        private Long id;
    }

    @Getter
    @Setter
    @Accessors(chain = true)
    @ApiModel("GroupTypeMeritDeleteRq")
    public static class Delete extends InstanceGroupTypeMeritDTO {

        private Long id;
    }

    @Getter
    @Setter
    @Accessors(chain = true)
    @ApiModel("GroupTypeMeritSpecResponse")
    public static class SpecResponse {
        private Response response;
    }

    @Getter
    @Setter
    @Accessors(chain = true)
    @ApiModel("GroupTypeMeritResponse")
    public static class Response {
        private List<Info> data;
        private Integer status;
        private Integer startRow;
        private Integer endRow;
        private Integer totalRows;
    }

}
