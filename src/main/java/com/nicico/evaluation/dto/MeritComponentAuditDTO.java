package com.nicico.evaluation.dto;

import com.nicico.evaluation.model.compositeKey.MeritComponentAuditKey;
import io.swagger.annotations.ApiModel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.util.Date;
import java.util.List;

@Getter
@Setter
@Accessors(chain = true)
public abstract class MeritComponentAuditDTO {

    private MeritComponentAuditKey id;
    private Long rev;
    private String code;
    private String title;
    private String description;
    private Long statusCatalogId;

    @Getter
    @Setter
    @ApiModel("MeritComponentInfo")
    public static class Info extends MeritComponentAuditDTO {

        private Date createdDate;
        private String createdBy;
        private Date lastModifiedDate;
        private String lastModifiedBy;
        private CatalogDTO.Info statusCatalog;
    }

    @Getter
    @Setter
    @Accessors(chain = true)
    @ApiModel("MeritComponentAuditSpecResponse")
    public static class SpecResponse {
        private Response response;
    }

    @Getter
    @Setter
    @Accessors(chain = true)
    @ApiModel("MeritComponentAuditResponse")
    public static class Response {
        private List<Info> data;
        private Integer status;
        private Integer startRow;
        private Integer endRow;
        private Integer totalRows;
    }

}
