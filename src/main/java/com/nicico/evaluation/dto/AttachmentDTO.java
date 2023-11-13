package com.nicico.evaluation.dto;

import io.swagger.annotations.ApiModel;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.List;

@Getter
@Setter
@Accessors(chain = true)
public abstract class AttachmentDTO {

    private String fileName;
    private Long fileTypeId;
    private String description;
    @NotNull(message = "آی دی نمی تواند خالی باشد")
    private Long objectId;
    @NotNull(message = "نوع فایل نمی تواند خالی باشد")
    private String objectType;
    @NotNull
    private String groupId;
    @NotNull
    private String fmsKey;
    private Integer status;

    @Getter
    @Setter
    @Accessors(chain = true)
    @ApiModel("AttachmentCreateRq")
    public static class Create extends AttachmentDTO {
    }

    @Getter
    @Setter
    @Builder
    @Accessors(chain = true)
    @ApiModel("AttachmentCreateBlobFileRq")
    public static class CreateBlobFile {
        private String fileName;
        private Long objectId;
        private String objectType;
        private byte[] blobFile;
        private Integer status;
    }

    @Getter
    @Setter
    @ApiModel("AttachmentInfo")
    public static class Info extends AttachmentDTO {
        private Long id;

    }

    @Getter
    @Setter
    @Accessors(chain = true)
    @ApiModel("AttachmentBlobFileInfoRq")
    public static class BlobFileInfo {
        private Long id;
        private String objectType;
        private Date createdDate;
        private String createdTime;
        private String createdBy;
        private String fileName;
        private Integer status;
    }

    @Getter
    @Setter
    @Accessors(chain = true)
    @ApiModel("AttachmentDownloadBlobInfoRq")
    public static class DownloadBlobInfo {
        private String fileName;
        private byte[] blobFile;
    }

    @Getter
    @Setter
    @ApiModel("AttachmentInfo")
    public static class AttachInfo {
        private String fmsUrl;
        private List<AttachmentDTO.Info> infoList;

    }

    @Getter
    @Setter
    @ApiModel("FmsInfo")
    public static class FmsInfo {

        private String fmsGroupId;
        private String fmsUrl;
    }

    @Getter
    @Setter
    @Accessors(chain = true)
    @ApiModel("AttachmentUpdateRq")
    public static class Update extends AttachmentDTO {
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
        private List<?> data;
        private Integer status;
        private Integer startRow;
        private Integer endRow;
        private Integer totalRows;
    }
}
