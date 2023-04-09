package com.nicico.evaluation.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;

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
        private Date createdDate;
        private String createdBy;
        private Date lastModifiedDate;
        private String lastModifiedBy;
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
        private Long id;
        @NotNull
        @ApiModelProperty(required = true)
        private Integer version;
        private String title;
        private String description;
    }
}
