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
public abstract class PostMeritComponentDTO {

    @NotNull
    private String groupPostCode;
    @NotNull
    private Long meritComponentId;
    @Min(value = 0, message = " وزن باید عددی بین صفر تا 100 باشد")
    @Max(value = 100, message = " وزن باید عددی بین صفر تا 100 باشد")
    @NotNull
    private Long weight;

    @Getter
    @Setter
    @ApiModel("PostMeritComponentInfo")
    public static class Info extends PostMeritComponentDTO {

        private Long id;
        private MeritComponentDTO.Info meritComponent;
        private List<PostMeritInstanceDTO.InstanceTupleDTO> postMeritInstanceList;

    }

    @Getter
    @Setter
    @ApiModel("PostMeritComponentInfoWithInstance")
    public static class InfoWithInstance extends PostMeritComponentDTO {

        private Long id;
        private MeritComponentDTO.Info meritComponent;
        private List<PostMeritInstanceTupleDto> postMeritInstanceList;
    }

    @Getter
    @Setter
    @Accessors(chain = true)
    @ApiModel("PostMeritInstanceRq")
    public static class PostMeritInstanceTupleDto {

        private Long id;
        private Long postMeritComponentId;
        private InstanceGroupTypeMeritDTO.InstanceTupleDTO instance;

    }

    @Getter
    @Setter
    @Accessors(chain = true)
    @ApiModel("PostMeritComponentCreateRq")
    public static class Create extends PostMeritComponentDTO {

    }

    @Getter
    @Setter
    @Accessors(chain = true)
    @ApiModel("PostMeritComponentBatchCreateRq")
    public static class BatchCreate {

        @NotNull
        private String groupPostCode;
        @NotNull
        private String meritComponentCode;
        @NotNull
        private Long weight;
    }

    @Getter
    @Setter
    @Accessors(chain = true)
    @ApiModel("PostMeritComponentUpdateRq")
    public static class Update extends PostMeritComponentDTO {

    }

    @Getter
    @Setter
    @Accessors(chain = true)
    @ApiModel("PostMeritComponentDeleteRq")
    public static class Delete extends PostMeritComponentDTO {

        private Long id;
    }

    @Getter
    @Setter
    @Accessors(chain = true)
    @ApiModel("PostMeritComponentSpecResponse")
    public static class SpecResponse {
        private Response response;
    }

    @Getter
    @Setter
    @Accessors(chain = true)
    @ApiModel("PostMeritComponentResponse")
    public static class Response {
        private List<Info> data;
        private Integer status;
        private Integer startRow;
        private Integer endRow;
        private Integer totalRows;
    }

}
