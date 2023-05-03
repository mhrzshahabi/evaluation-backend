package com.nicico.evaluation.dto;

import io.swagger.annotations.ApiModel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.util.Date;
import java.util.List;

@Getter
@Setter
@Accessors(chain = true)
public abstract class SensitiveEventsDTO {

    private String title;
    private String description;
    private Date eventDate;
    private Date toDate;
    private Long levelEffect;
    private Long statusCatalogId;
    private Long eventPolicyCatalogId;
    private Long typeCatalogId;

    @Getter
    @Setter
    @ApiModel("SensitiveEventsInfo")
    public static class Info extends SensitiveEventsDTO {

        private Long id;
        private CatalogDTO.Info statusCatalog;
        private CatalogDTO.Info eventPolicyCatalog;
        private CatalogDTO.Info typeCatalog;
        private String nationalCode;
        private AttachmentDTO.AttachInfo attachment;

    }

    @Getter
    @Setter
    @Accessors(chain = true)
    @ApiModel("SensitiveEventsCreateRq")
    public static class Create extends SensitiveEventsDTO {

    }

    @Getter
    @Setter
    @Accessors(chain = true)
    @ApiModel("SensitiveEventsUpdateRq")
    public static class Update extends SensitiveEventsDTO {

    }

    @Getter
    @Setter
    @Accessors(chain = true)
    @ApiModel("SensitiveEventsDeleteRq")
    public static class Delete extends SensitiveEventsDTO {

        private Long id;
    }

    @Getter
    @Setter
    @Accessors(chain = true)
    @ApiModel("SensitiveEventsSpecResponse")
    public static class SpecResponse {
        private Response response;
    }

    @Getter
    @Setter
    @Accessors(chain = true)
    @ApiModel("SensitiveEventsResponse")
    public static class Response {
        private List<Info> data;
        private Integer status;
        private Integer startRow;
        private Integer endRow;
        private Integer totalRows;
    }
}
