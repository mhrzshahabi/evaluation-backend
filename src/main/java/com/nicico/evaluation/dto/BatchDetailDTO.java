package com.nicico.evaluation.dto;

import io.swagger.annotations.ApiModel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.util.List;

@Getter
@Setter
@Accessors(chain = true)
public abstract class BatchDetailDTO {

    private Long statusCatalogId;
    private Long batchId;

    @Getter
    @Setter
    @ApiModel("BatchDetailInfo")
    public static class Info extends BatchDetailDTO {
        private Long id;
        private String description;
        private String exceptionTitle;
        private String inputDTO;
        private String statusCatalog;
    }

    @Getter
    @Setter
    @Accessors(chain = true)
    @ApiModel("BatchDetailCreateRq")
    public static class Create extends BatchDetailDTO{

        private String description;
        private String inputDTO;
    }

    @Getter
    @Setter
    @Accessors(chain = true)
    @ApiModel("BatchDetailCreateListRq")
    public static class CreateList {

        private Long batchId;
        private String serviceType;
        private List<Object> inputDetails;
    }

    @Getter
    @Setter
    @Accessors(chain = true)
    @ApiModel("BatchDetailUpdateRq")
    public static class Update extends BatchDetailDTO {
        private Long id;
    }

    @Getter
    @Setter
    @Accessors(chain = true)
    @ApiModel("BatchDetailDeleteRq")
    public static class Delete extends BatchDetailDTO {
        private Long id;
    }

    @Getter
    @Setter
    @Accessors(chain = true)
    @ApiModel("BatchDetailSpecResponse")
    public static class SpecResponse {
        private Response response;
    }

    @Getter
    @Setter
    @Accessors(chain = true)
    @ApiModel("BatchDetailResponse")
    public static class Response {
        private List<Info> data;
        private Integer status;
        private Integer startRow;
        private Integer endRow;
        private Integer totalRows;
    }

}
