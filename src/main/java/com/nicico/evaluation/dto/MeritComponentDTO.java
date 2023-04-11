package com.nicico.evaluation.dto;

import io.swagger.annotations.ApiModel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.util.List;

@Getter
@Setter
@Accessors(chain = true)
public abstract class MeritComponentDTO {

    private String code;
    private String title;

    @Getter
    @Setter
    @ApiModel("MeritComponentInfo")
    public static class Info extends MeritComponentDTO {

        private Long id;
    }

    @Getter
    @Setter
    @Accessors(chain = true)
    @ApiModel("MeritComponentCreateRq")
    public static class Create extends MeritComponentDTO {

    }

    @Getter
    @Setter
    @Accessors(chain = true)
    @ApiModel("MeritComponentUpdateRq")
    public static class Update extends MeritComponentDTO {

        private Long id;
    }

    @Getter
    @Setter
    @Accessors(chain = true)
    @ApiModel("MeritComponentDeleteRq")
    public static class Delete extends MeritComponentDTO {

        private Long id;
    }

    @Getter
    @Setter
    @Accessors(chain = true)
    @ApiModel("MeritComponentSpecResponse")
    public static class SpecResponse {
        private Response response;
    }

    @Getter
    @Setter
    @Accessors(chain = true)
    @ApiModel("MeritComponentResponse")
    public static class Response {
        private List<Info> data;
        private Integer status;
        private Integer startRow;
        private Integer endRow;
        private Integer totalRows;
    }

}
