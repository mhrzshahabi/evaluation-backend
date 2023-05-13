package com.nicico.evaluation.dto;

import io.swagger.annotations.ApiModel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;

@Getter
@Setter
@Accessors(chain = true)
public class InstanceDTO {

    @NotNull
    @Size(max = 255, message = "کد نمی تواند بیشتر از 255 کاراکتر باشد")
    private String code;
    @NotNull
    @Size(max = 255, message = "عنوان نمی تواند بیشتر از 255 کاراکتر باشد")
    private String title;

    @Getter
    @Setter
    @ApiModel("InstanceExcel")
    public static class Excel {
        private Long id;
        private String code;
        private String title;
    }

    @Getter
    @Setter
    @Accessors(chain = true)
    @ApiModel("InstanceCreateRq")
    public static class Create extends InstanceDTO {
    }

    @Getter
    @Setter
    @ApiModel("InstanceInfo")
    public static class Info extends InstanceDTO {
        private Long id;
        //آیا این مصداق در ارزیابی استفاده شده است یا نه ؟
        //todo
        private Boolean hasEvaluation = false;
        List<PostMeritInstanceTupleDTO> postMeritInstances;
    }

    @Getter
    @Setter
    @ApiModel(" PostMeritInstanceTupleInfo")
    public static class PostMeritInstanceTupleDTO {
        private Long id;
        private PostMeritComponentTupleDTO postMeritComponent;
    }

    @Getter
    @Setter
    @ApiModel("PostMeritComponentTupleInfo")
    public static class PostMeritComponentTupleDTO {
        private Long id;
    }

    @Getter
    @Setter
    @Accessors(chain = true)
    @ApiModel("InstanceUpdateRq")
    public static class Update extends InstanceDTO {

    }

    @Getter
    @Setter
    @Accessors(chain = true)
    @ApiModel("InstanceDeleteRq")
    public static class Delete {
        @NotNull
        @Min(1)
        private Long id;
    }

    @Getter
    @Setter
    @Accessors(chain = true)
    @ApiModel("InstanceSpecResponse")
    public static class SpecResponse {
        private Response response;
    }

    @Getter
    @Setter
    @Accessors(chain = true)
    @ApiModel("InstanceResponse")
    public static class Response {
        private List<Info> data;
        private Integer status;
        private Integer startRow;
        private Integer endRow;
        private Integer totalRows;
    }

}
