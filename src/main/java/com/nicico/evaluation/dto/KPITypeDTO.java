package com.nicico.evaluation.dto;

import javax.validation.constraints.NotNull;
import io.swagger.annotations.ApiModel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.util.List;

@Getter
@Setter
@Accessors(chain = true)
public abstract class KPITypeDTO {

    @NotNull
    private String code;
    @NotNull
    private String title;
    @NotNull
    private Long levelDefCatalogId;

    @Getter
    @Setter
    @ApiModel("KPITypeInfo")
    public static class Info extends KPITypeDTO {

        private Long id;
        private CatalogDTO.Info levelDefCatalog;
    }

    @Getter
    @Setter
    @Accessors(chain = true)
    @ApiModel("KPITypeCreateRq")
    public static class Create extends KPITypeDTO {

    }

    @Getter
    @Setter
    @Accessors(chain = true)
    @ApiModel("KPITypeUpdateRq")
    public static class Update extends KPITypeDTO {

    }

    @Getter
    @Setter
    @Accessors(chain = true)
    @ApiModel("KPITypeDeleteRq")
    public static class Delete extends KPITypeDTO {

        private Long id;
    }

    @Getter
    @Setter
    @Accessors(chain = true)
    @ApiModel("KPITypeSpecResponse")
    public static class SpecResponse {
        private Response response;
    }

    @Getter
    @Setter
    @Accessors(chain = true)
    @ApiModel("KPITypeResponse")
    public static class Response {
        private List<Info> data;
        private Integer status;
        private Integer startRow;
        private Integer endRow;
        private Integer totalRows;
    }

}
