package com.nicico.evaluation.dto;

import io.swagger.annotations.ApiModel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.io.Serializable;

@Getter
@Setter
@Accessors(chain = true)
public class CatalogTypeDTO implements Serializable {

    private String title;
    private String code;

    @Getter
    @Setter
    @Accessors(chain = true)
    @ApiModel("CatalogTypeDTO - Info")
    public static class Info extends CatalogTypeDTO {
        private Long id;
        private String description;
    }

    @Getter
    @Setter
    @Accessors(chain = true)
    @ApiModel("CatalogTypeDTO - Create")
    public static class Create extends CatalogTypeDTO {
        private String description;
    }

    @Getter
    @Setter
    @Accessors(chain = true)
    @ApiModel("CatalogTypeDTO - Update")
    public static class Update {
        private String title;
        private String description;
    }
}
