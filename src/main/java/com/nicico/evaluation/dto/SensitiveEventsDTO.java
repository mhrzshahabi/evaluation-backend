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
        private List<SensitiveEventPersonDTO.PersonInfo> sensitiveEventPersonList;
    }

    @Getter
    @Setter
    @ApiModel("SensitiveEventPersonInfo")
    public static class SensitiveEventPersonInfo {
        private Long id;
        private Long sensitiveEventId;
        private Long sensitiveEventPersonId;
        private String title;
        private Long levelEffect;
        private Date eventDate;
        private String eventPolicyCatalog;
        private String typeCatalog;
        private CatalogDTO statusCatalog;
        private String nationalCode;
        private String firstName;
        private String lastName;
        private String personnelCode;
    }

    @Getter
    @Setter
    @ApiModel("ExcelInfo")
    public static class Excel {
        private String title;
        private String eventPolicyCatalog;
        private String typeCatalog;
        private Long levelEffect;
        private String eventDate;
        private String statusCatalog;
        private String firstName;
        private String lastName;
        private String personnelCode;
    }

    @Getter
    @Setter
    @ApiModel("SensitiveEventsLevelEffectData")
    public static class LevelEffectData {
        private Long levelEffect;
        private String eventDate;
        private String nationalCode;
        private String meritComponentCode;
        private String meritComponentTitle;
        private String typeCode;
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
        private List<?> data;
        private Integer status;
        private Integer startRow;
        private Integer endRow;
        private Integer totalRows;
    }
}
