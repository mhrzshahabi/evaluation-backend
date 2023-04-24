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
public abstract class PostMeritComponentDTO {

    @NotNull
    private String groupPostCode;
    @NotNull
    private Long meritComponentId;
    private Long evaluationItemId;
    @NotNull
    private Long weight;

    @Getter
    @Setter
    @ApiModel("PostMeritComponentInfo")
    public static class Info extends PostMeritComponentDTO {

        private Long id;
        private MeritComponentDTO.Info meritComponent;

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
    @ApiModel("PostMeritComponentUpdateRq")
    public static class Update extends PostMeritComponentDTO {

        private Long id;
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
