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
public abstract class PostMeritInstanceDTO {

    @NotNull
    private Long postMeritComponentId;

    @Getter
    @Setter
    @ApiModel("PostMeritInstanceInfo")
    public static class Info extends PostMeritInstanceDTO {

        private Long id;
        private PostMeritComponentDTO.Info postMeritComponent;
        private InstanceDTO.Info instance;

    }

    @Getter
    @Setter
    @Accessors(chain = true)
    @ApiModel("PostMeritInstanceCreateRq")
    public static class Create extends PostMeritInstanceDTO {
        @NotNull
        private Long instanceId;
    }

    @Getter
    @Setter
    @Accessors(chain = true)
    @ApiModel("PostMeritInstanceCreateRq")
    public static class CreateAll extends PostMeritInstanceDTO {

        @NotNull
        private List<Long> instanceIds;
    }

    @Getter
    @Setter
    @Accessors(chain = true)
    @ApiModel("PostMeritInstanceBatchCreateRq")
    public static class BatchCreate {

        @NotNull
        private String groupPostCode;
        @NotNull
        private String meritComponentCode;
        @NotNull
        private String instanceCode;
        @NotNull
        private Long weight;
    }

    @Getter
    @Setter
    @Accessors(chain = true)
    @ApiModel("PostMeritInstanceUpdateRq")
    public static class Update extends PostMeritInstanceDTO {

        private Long id;
    }

    @Getter
    @Setter
    @Accessors(chain = true)
    @ApiModel("PostMeritInstanceDeleteRq")
    public static class Delete extends PostMeritInstanceDTO {

        private Long id;
    }

    @Getter
    @Setter
    @Accessors(chain = true)
    @ApiModel("PostMeritInstanceSpecResponse")
    public static class SpecResponse {
        private Response response;
    }

    @Getter
    @Setter
    @Accessors(chain = true)
    @ApiModel("PostMeritInstanceResponse")
    public static class Response {
        private List<Info> data;
        private Integer status;
        private Integer startRow;
        private Integer endRow;
        private Integer totalRows;
    }

}
