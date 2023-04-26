package com.nicico.evaluation.dto;

import io.swagger.annotations.ApiModel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotNull;
import java.util.List;

@Getter
@Setter
@Accessors(chain = true)
public abstract class MeritComponentDTO {

    @NotNull
    private String code;
    @NotNull
    private String title;

    @Getter
    @Setter
    @ApiModel("MeritComponentInfo")
    public static class Info extends MeritComponentDTO {

        private Long id;
        //آیا این مصداق در ارزیابی استفاده شده است یا نه ؟
        //todo
        private Boolean hasEvaluation = false;
        //بعدا امکان داره لیست بشه
//        private List<KPITypeDTO.Info> kpiType;
        private MeritComponentTypeDTO.Info meritComponentTypes;
    }

    @Getter
    @Setter
    @Accessors(chain = true)
    @ApiModel("MeritComponentCreateRq")
    public static class Create extends MeritComponentDTO {

        private List<Long> kpiTypeId;
    }

    @Getter
    @Setter
    @Accessors(chain = true)
    @ApiModel("MeritComponentUpdateRq")
    public static class Update extends MeritComponentDTO {

        private Long id;
        private List<Long> kpiTypeId;
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
    public static class KpiTypeTupleDTO   {

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

}
