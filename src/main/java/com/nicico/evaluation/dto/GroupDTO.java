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
public class GroupDTO {

    private String code;
    private String title;
    private Boolean definitionAllowed;

    @Getter
    @Setter
    @Accessors(chain = true)
    @ApiModel("GroupCreateRq")
    public static class Create extends GroupDTO {
    }

    @Getter
    @Setter
    @ApiModel("GroupInfo")
    public static class Info extends GroupDTO{
        private Long id;
    }

    @Getter
    @Setter
    @Accessors(chain = true)
    @ApiModel("GroupUpdateRq")
    public static class Update extends GroupDTO {
        @NotNull
        @Min(1)
        private Long id;
    }

    @Getter
    @Setter
    @Accessors(chain = true)
    @ApiModel("GroupDeleteRq")
    public static class Delete {
        @NotNull
        @Min(1)
        private Long id;
    }

    @Getter
    @Setter
    @Accessors(chain = true)
    @ApiModel("GroupSpecResponse")
    public static class SpecResponse {
        private Response response;
    }

    @Getter
    @Setter
    @Accessors(chain = true)
    @ApiModel("GroupResponse")
    public static class Response {
        private List<Info> data;
        private Integer status;
        private Integer startRow;
        private Integer endRow;
        private Integer totalRows;
    }
}
