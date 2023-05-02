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
public abstract class AttachmentDTO {

    private String fileName;
    private Long fileTypeId;
    private String description;
    private Long objectId;
    private String objectType;
    private String fmsKey;
    private String groupId;

    @Getter
    @Setter
    @Accessors(chain = true)
    @ApiModel("AttachmentCreateRq")
    public static class Create extends AttachmentDTO {
    }

    @Getter
    @Setter
    @ApiModel("AttachmentInfo")
    public static class Info extends AttachmentDTO {
        private Long id;
    }

    @Getter
    @Setter
    @ApiModel("FmsInfo")
    public static class FmsInfo {

        private String fmsUrl;
        private String fmsGroupId;
    }

    @Getter
    @Setter
    @Accessors(chain = true)
    @ApiModel("AttachmentUpdateRq")
    public static class Update extends AttachmentDTO {
        @NotNull
        private Long id;
    }

    @Getter
    @Setter
    @Accessors(chain = true)
    @ApiModel("AttachmentDeleteRq")
    public static class Delete {
        @NotNull
        private Long id;
    }

    @Getter
    @Setter
    @Accessors(chain = true)
    @ApiModel("AttachmentSpecResponse")
    public static class SpecResponse {
        private Response response;
    }

    @Getter
    @Setter
    @Accessors(chain = true)
    @ApiModel("AttachmentResponse")
    public static class Response {
        private List<Info> data;
        private Integer status;
        private Integer startRow;
        private Integer endRow;
        private Integer totalRows;
    }
}
