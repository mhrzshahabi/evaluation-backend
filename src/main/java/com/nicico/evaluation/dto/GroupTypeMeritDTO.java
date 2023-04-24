package com.nicico.evaluation.dto;

import io.swagger.annotations.ApiModel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

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
    @NotNull
    private Long weight;

    @Getter
    @Setter
    @ApiModel("GroupTypeMeritInfo")
    public static class Info extends GroupTypeMeritDTO {

        private Long id;
        private MeritComponentDTO.Info meritComponent;
        private GroupTypeDTO.Info groupType;
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
    @ApiModel("GroupTypeMeritUpdateRq")
    public static class Update extends GroupTypeMeritDTO {

        private Long id;
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

}
