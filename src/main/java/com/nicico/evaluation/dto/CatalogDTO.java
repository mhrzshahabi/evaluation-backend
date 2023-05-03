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
public class CatalogDTO implements Serializable {

    private String title;
    private String code;

    @Getter
    @Setter
    @Accessors(chain = true)
    @ApiModel("CatalogInfo")
    public static class Info extends CatalogDTO {
        private Long id;
        private String description;
        private Long catalogTypeId;
        private CatalogTypeDTO catalogType;
    }

    @Getter
    @Setter
    @Accessors(chain = true)
    @ApiModel("CatalogCreateRq")
    public static class Create extends CatalogDTO {
        private String description;
        private Long catalogTypeId;
    }

    @Getter
    @Setter
    @Accessors(chain = true)
    @ApiModel("CatalogUpdateRq")
    public static class Update {
        private String title;
        private String description;
    }

    @Getter
    @Setter
    @Accessors(chain = true)
    @ApiModel("CatalogSpecResponse")
    public static class SpecResponse {
        private Response response;
    }

    @Getter
    @Setter
    @Accessors(chain = true)
    @ApiModel("CatalogResponse")
    public static class Response {
        private List<Info> data;
        private Integer status;
        private Integer startRow;
        private Integer endRow;
        private Integer totalRows;
    }
}
