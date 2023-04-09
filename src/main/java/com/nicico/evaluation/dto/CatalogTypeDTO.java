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
        private Date createdDate;
        private String createdBy;
        private Date lastModifiedDate;
        private String lastModifiedBy;
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
        private Long id;
        @NotNull
        @ApiModelProperty(required = true)
        private Integer version;
        private String title;
        private String description;
    }
}
