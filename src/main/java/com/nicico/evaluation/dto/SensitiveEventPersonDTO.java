package com.nicico.evaluation.dto;

import io.swagger.annotations.ApiModel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import java.util.List;

@Getter
@Setter
@Accessors(chain = true)
public abstract class SensitiveEventPersonDTO {

    private String nationalCode;
    private Long sensitiveEventId;
    private Long meritComponentId;
    @Min(value = 1, message = " درصد مشارکت باید عددی بین 1 تا 100 باشد")
    @Max(value = 100, message = " درصد مشارکت باید عددی بین 1 تا 100 باشد")
    private Long participation;
    private String firstName;
    private String lastName;

    @Getter
    @Setter
    @ApiModel("SensitiveEventPersonInfo")
    public static class Info extends SensitiveEventPersonDTO {
        private Long id;
        private SensitiveEventsDTO.Info sensitiveEvent;
        private MeritComponentTupleDTO meritComponent;
        private AttachmentDTO.AttachInfo attachment;
    }

    @Getter
    @Setter
    @ApiModel("SensitiveEventPersonLastActiveMeritInfo")
    public static class LastActiveMeritInfo extends SensitiveEventPersonDTO {
        private Long id;
        private SensitiveEventsDTO.Info sensitiveEvent;
        private MeritComponentDTO.Info meritComponent;
        private AttachmentDTO.AttachInfo attachment;
    }

    @Getter
    @Setter
    @ApiModel("SensitiveEventPersonPersonInfo")
    public static class PersonInfo {
        private String nationalCode;
        private String firstName;
        private String lastName;
        private MeritComponentDTO.SensitiveEventData meritComponent;
    }

    @Getter
    @Setter
    @Accessors(chain = true)
    @ApiModel("SensitiveEventPersonCreateRq")
    public static class Create extends SensitiveEventPersonDTO {

    }


    @Getter
    @Setter
    @Accessors(chain = true)
    @ApiModel("SensitiveEventPersonUpdateRq")
    public static class Update extends SensitiveEventPersonDTO {

    }

    @Getter
    @Setter
    @Accessors(chain = true)
    @ApiModel("SensitiveEventPersonDeleteRq")
    public static class Delete extends SensitiveEventPersonDTO {

        private Long id;
    }

    @Getter
    @Setter
    @Accessors(chain = true)
    @ApiModel("SensitiveEventPersonDeleteRq")
    public static class MeritComponentTupleDTO {
        private String id;
        private String code;
        private String title;
    }

    @Getter
    @Setter
    @Accessors(chain = true)
    @ApiModel("SensitiveEventPersonSpecResponse")
    public static class SpecResponse {
        private Response response;
    }

    @Getter
    @Setter
    @Accessors(chain = true)
    @ApiModel("SensitiveEventPersonResponse")
    public static class Response {
        private List<LastActiveMeritInfo> data;
        private Integer status;
        private Integer startRow;
        private Integer endRow;
        private Integer totalRows;
    }

}
