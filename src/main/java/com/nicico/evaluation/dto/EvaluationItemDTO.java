package com.nicico.evaluation.dto;

import io.swagger.annotations.ApiModel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.util.List;

@Getter
@Setter
@Accessors(chain = true)
public abstract class EvaluationItemDTO {

    @NotNull
    private Long evaluationId;
    @NotNull
    private Long groupTypeMeritId;
    @NotNull
    private Long postMeritComponentId;
    @NotNull
    private Long questionnaireAnswerCatalogId;
    private String description;

    @Getter
    @Setter
    @Accessors(chain = true)
    @ApiModel("EvaluationItemCreateRq")
    public static class Create extends EvaluationItemDTO {
    }

    @Getter
    @Setter
    @ApiModel("EvaluationItemInfo")
    public static class Info extends EvaluationItemDTO {

        private Long id;
        private EvaluationDTO.Info evaluation;
        private GroupTypeMeritDTO.Info groupTypeMerit;
        private PostMeritComponentDTO.Info postMeritComponent;

    }

    @Getter
    @Setter
    @ApiModel("CreateEvaluationItemInfo")
    public static class CreateItemInfo {

        private String typeTitle;
        private Long groupTypeWeight;
        private List<GroupTypeMeritTupleDTO> groupTypeMerit;

    }

    @Getter
    @Setter
    @ApiModel("GroupTypeMeritInfo")
    public static class GroupTypeMeritTupleDTO {

        private Long postMeritId;
        private Long groupTypeMeritId;
        private MeritComponentDTO.Info meritComponent;
        private List<InstanceGroupTypeMeritDTO.InstanceTupleDTO> instanceGroupTypeMerits;
        private String description;

    }

    @Getter
    @Setter
    @Accessors(chain = true)
    @ApiModel("InstanceTupleRq")
    public class InstanceTupleDTO {

        @NotNull
        private String id;
        @NotNull
        private String title;
    }

    @Getter
    @Setter
    @Accessors(chain = true)
    @ApiModel("EvaluationItemUpdateRq")
    public static class Update extends EvaluationItemDTO {

    }

    @Getter
    @Setter
    @Accessors(chain = true)
    @ApiModel("EvaluationItemDeleteRq")
    public static class Delete {
        @NotNull
        @Min(1)
        private Long id;
    }

    @Getter
    @Setter
    @Accessors(chain = true)
    @ApiModel("EvaluationItemSpecResponse")
    public static class SpecResponse {
        private Response response;
    }

    @Getter
    @Setter
    @Accessors(chain = true)
    @ApiModel("EvaluationItemResponse")
    public static class Response {
        private List<Info> data;
        private Integer status;
        private Integer startRow;
        private Integer endRow;
        private Integer totalRows;
    }
}
