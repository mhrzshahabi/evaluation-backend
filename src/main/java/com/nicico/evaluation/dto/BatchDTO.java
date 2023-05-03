package com.nicico.evaluation.dto;

import io.swagger.annotations.ApiModel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.util.List;

@Getter
@Setter
@Accessors(chain = true)
public abstract class BatchDTO {


    @Getter
    @Setter
    @ApiModel("BatchInfo")
    public static class Info extends BatchDTO {
        private Long id;
        private String titleCatalog;
        private String statusCatalog;
        private Integer total;
        private Integer successfulNumber;
        private Integer failedNumber;
        private Float progress;
    }

    @Getter
    @Setter
    @Accessors(chain = true)
    @ApiModel("BatchCreateRq")
    public static class Create extends BatchDTO {
        private Long titleCatalogId;
        private List<Object> inputDetails;
    }

    @Getter
    @Setter
    @Accessors(chain = true)
    @ApiModel("BatchUpdateRq")
    public static class Update extends BatchDTO {

    }

    @Getter
    @Setter
    @Accessors(chain = true)
    @ApiModel("BatchDeleteRq")
    public static class Delete extends BatchDTO {
        private Long id;
    }

    @Getter
    @Setter
    @Accessors(chain = true)
    @ApiModel("BatchSpecResponse")
    public static class SpecResponse {
        private Response response;
    }

    @Getter
    @Setter
    @Accessors(chain = true)
    @ApiModel("BatchResponse")
    public static class Response {
        private List<Info> data;
        private Integer status;
        private Integer startRow;
        private Integer endRow;
        private Integer totalRows;
    }

}
