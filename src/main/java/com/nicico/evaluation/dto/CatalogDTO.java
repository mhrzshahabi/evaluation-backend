package com.nicico.evaluation.dto;

import io.swagger.annotations.ApiModel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.io.Serializable;

@Getter
@Setter
@Accessors(chain = true)
public class CatalogDTO implements Serializable {

    private String title;
    private String code;

    @Getter
    @Setter
    @Accessors(chain = true)
    @ApiModel("CatalogDTO - Info")
    public static class Info extends CatalogDTO {
        private Long id;
        private String description;
        private Long catalogTypeId;
    }

    @Getter
    @Setter
    @Accessors(chain = true)
    @ApiModel("CatalogDTO - Create")
    public static class Create extends CatalogDTO {
        private String description;
        private Long catalogTypeId;
    }

    @Getter
    @Setter
    @Accessors(chain = true)
    @ApiModel("CatalogDTO - Update")
    public static class Update {
        private String title;
        private String description;
    }
}
