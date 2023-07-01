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
public abstract class GroupTypeMeritDTO {

    @NotNull
    private Long meritComponentId;
    @NotNull
    private Long groupTypeId;
    @Min(value = 1, message = " وزن باید عددی بین یک تا 100 باشد")
    @Max(value = 100, message = " وزن باید عددی بین صفر تا 100 باشد")
    @NotNull
    private Long weight;

    @Getter
    @Setter
    @ApiModel("GroupTypeMeritInfo")
    public static class Info extends GroupTypeMeritDTO {

        private Long id;
        private MeritComponentDTO.Info meritComponent;
        private GroupTypeDTO.Info groupType;
        private Boolean hasInstance;
        private List<InstanceGroupTypeMeritDTO.InstanceTupleDTO> instanceGroupTypeMerits;
        private Long totalComponentWeight;
    }

    @Getter
    @Setter
    @ApiModel("GroupTypeMeritInfoWithInstance")
    public static class InfoWithInstance extends GroupTypeMeritDTO {

        private Long id;
        private MeritComponentDTO.Info meritComponent;
        private GroupTypeDTO.Info groupType;
        private Boolean hasInstance;
        private List<InstanceGroupTypeMeritTupleDto> instanceGroupTypeMerits;
    }

    @Getter
    @Setter
    @Accessors(chain = true)
    @ApiModel("InstanceGroupTypeMeritRq")
    public static class InstanceGroupTypeMeritTupleDto {

        private Long id;
        private Long groupTypeMeritId;
        private InstanceGroupTypeMeritDTO.InstanceTupleDTO instance;

    }

    @Getter
    @Setter
    @Accessors(chain = true)
    @ApiModel("GroupTypeMeritCreateRq")
    public static class Create extends GroupTypeMeritDTO {

        private List<Long> instanceIds;
    }

    @Getter
    @Setter
    @Accessors(chain = true)
    @ApiModel("GroupTypeMeritBatchCreateRq")
    public static class BatchCreate extends GroupTypeMeritDTO {

        @NotNull
        private Long groupTypeId;
        @NotNull
        private Long meritComponentCode;
        @NotNull
        private String instanceCode;
        @NotNull
        private Long weight;
    }

    @Getter
    @Setter
    @Accessors(chain = true)
    @ApiModel("GroupTypeMeritUpdateRq")
    public static class Update extends GroupTypeMeritDTO {

        private List<Long> instanceIds;
    }

    @Getter
    @Setter
    @Accessors(chain = true)
    @ApiModel("GroupTypeMeritDeleteRq")
    public static class Delete extends GroupTypeMeritDTO {

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

    @Getter
    @Setter
    @ApiModel("GroupTypeMeritInfo")
    public static class Excel {
        private Long id;
        private String groupTypeTitle;
        private String kpiTypeTitle;
        private String meritComponentTitle;
        private Long weight;
        private String hasInstanceStr;
    }

}
