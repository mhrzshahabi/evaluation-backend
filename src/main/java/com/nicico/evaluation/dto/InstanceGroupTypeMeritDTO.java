package com.nicico.evaluation.dto;

import io.swagger.annotations.ApiModel;
import liquibase.pro.packaged.S;
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
    @ApiModel("InstanceGroupTypeMeritInfo")
    public static class Info extends InstanceGroupTypeMeritDTO {

        private Long id;
        private GroupTypeMeritTupleDTO groupTypeMerit;
        private InstanceTupleDTO instance;
    }

    @Getter
    @Setter
    @Accessors(chain = true)
    @ApiModel("InstanceGroupTypeMeritCreateRq")
    public static class Create extends InstanceGroupTypeMeritDTO {

    }

    @Getter
    @Setter
    @Accessors(chain = true)
    @ApiModel("InstanceGroupTypeMeritUpdateRq")
    public static class Update extends InstanceGroupTypeMeritDTO {

        private Long id;
    }

    @Getter
    @Setter
    @Accessors(chain = true)
    @ApiModel("InstanceGroupTypeMeritDeleteRq")
    public static class Delete extends InstanceGroupTypeMeritDTO {

        private Long id;
    }

    @Getter
    @Setter
    @Accessors(chain = true)
    @ApiModel("GroupTypeMeritDTORq")
    public static class GroupTypeMeritTupleDTO {

        private Long weight;
        private MeritComponentTupleDTO meritComponent;
        private GroupTypeTupleDTO groupType;
    }

    @Getter
    @Setter
    public static class MeritComponentTupleDTO {

        private String title;
    }

    @Getter
    @Setter
    public static class GroupTypeTupleDTO {

        private String title;

    }

    @Getter
    @Setter
    @ApiModel("InstanceInfo")
    public static class InstanceTupleDTO {

        private String title;
        private Boolean hasInstance;

    }

    @Getter
    @Setter
    @Accessors(chain = true)
    @ApiModel("InstanceGroupTypeMeritSpecResponse")
    public static class SpecResponse {
        private Response response;
    }

    @Getter
    @Setter
    @Accessors(chain = true)
    @ApiModel("InstanceGroupTypeMeritResponse")
    public static class Response {
        private List<Info> data;
        private Integer status;
        private Integer startRow;
        private Integer endRow;
        private Integer totalRows;
    }

}
