package com.nicico.evaluation.dto;

import io.swagger.annotations.ApiModel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.List;

@Getter
@Setter
@Accessors(chain = true)
public class CatalogTypeDTO implements Serializable {

    private String title;
    private String code;

    @Getter
    @Setter
    @Accessors(chain = true)
    @ApiModel("CatalogTypeInfo")
    public static class Info extends CatalogTypeDTO {
        private Long id;
        private String description;
    }

    @Getter
    @Setter
    @Accessors(chain = true)
    @ApiModel("CatalogTypeCreateRq")
    public static class Create extends CatalogTypeDTO {
        private String description;
    }

    @Getter
    @Setter
    @Accessors(chain = true)
    @ApiModel("CatalogTypeUpdateRq")
    public static class Update {
        private String title;
        private String description;
    }

    @Getter
    @Setter
    @Accessors(chain = true)
    @ApiModel("CatalogTypeSpecResponse")
    public static class SpecResponse {
        private Response response;
    }

    @Getter
    @Setter
    @Accessors(chain = true)
    @ApiModel("CatalogTypeResponse")
    public static class Response {
        private List<Info> data;
        private Integer status;
        private Integer startRow;
        private Integer endRow;
        private Integer totalRows;
    }
}
