package com.nicico.evaluation.dto;

import io.swagger.annotations.ApiModel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;

@Getter
@Setter
@Accessors(chain = true)
public abstract class MeritComponentDTO {

    @NotNull
    @Size(max = 255, message = "کد نمی تواند بیشتر از 255 کاراکتر باشد")
    private String code;
    @NotNull
    @Size(max = 255, message = "عنوان نمی تواند بیشتر از 255 کاراکتر باشد")
    private String title;
    private String description;
    private Long statusCatalogId;

    @Getter
    @Setter
    @ApiModel("MeritComponentInfo")
    public static class Info extends MeritComponentDTO {

        private Long id;
        private Long rev;
        //آیا این مصداق در ارزیابی استفاده شده است یا نه ؟
        //todo
        private Boolean hasEvaluation = false;
        private MeritComponentTypeDTO.Info meritComponentTypes;
        private CatalogDTO.Info statusCatalog;
    }

    @Getter
    @Setter
    @ApiModel("MeritComponentInfo")
    public static class InfoDetail extends MeritComponentDTO {
        private Long id;
    }

    @Getter
    @Setter
    @Accessors(chain = true)
    @ApiModel("MeritComponentCreateRq")
    public static class Create extends MeritComponentDTO {

        private List<Long> kpiTypeId;
        private String createType;
    }

    @Getter
    @Setter
    @Accessors(chain = true)
    @ApiModel("MeritComponentUpdateRq")
    public static class Update extends MeritComponentDTO {

        private List<Long> kpiTypeId;
    }

    @Getter
    @Setter
    @Accessors(chain = true)
    @ApiModel("ChangeStatusRq")
    public static class ChangeStatus {

        private String statusCode;
        private String description;
        private String title;
    }

    @Getter
    @Setter
    @Accessors(chain = true)
    @ApiModel("SensitiveEventDataRq")
    public static class SensitiveEventData {

        private String title;
        private CatalogDTO.Info statusCatalog;
    }

    @Getter
    @Setter
    @Accessors(chain = true)
    @ApiModel("MeritComponentDeleteRq")
    public static class Delete extends MeritComponentDTO {

        private Long id;
    }

    @Getter
    @Setter
    @Accessors(chain = true)
    @ApiModel("KpiTypeTupleDTORq")
    public static class KpiTypeTupleDTO {

        private String code;
        private String title;
    }

    @Getter
    @Setter
    @Accessors(chain = true)
    @ApiModel("MeritComponentSpecResponse")
    public static class SpecResponse {
        private Response response;
    }

    @Getter
    @Setter
    @Accessors(chain = true)
    @ApiModel("MeritComponentResponse")
    public static class Response {
        private List<Info> data;
        private Integer status;
        private Integer startRow;
        private Integer endRow;
        private Integer totalRows;
    }

    @Getter
    @Setter
    @ApiModel("MeritComponentExcel")
    public static class Excel {
        private Long id;
        private String code;
        private String title;
        private String kpiTypeTitle;
        private String status;
    }

    @Getter
    @Setter
    @ApiModel("MeritComponentBatchCreate")
    public static class BatchCreate {
        private String code;
        private String title;
        private String kpiTypeCode;
    }

}
