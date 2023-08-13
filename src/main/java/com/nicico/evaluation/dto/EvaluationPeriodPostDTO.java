package com.nicico.evaluation.dto;

import io.swagger.annotations.ApiModel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.util.List;

@Getter
@Setter
@Accessors(chain = true)
public class EvaluationPeriodPostDTO {
    private Long evaluationPeriodId;
    private String postCode;

    @Getter
    @Setter
    @Accessors(chain = true)
    @ApiModel("EvaluationPeriodPostInfoRq")
    public static class Info extends EvaluationPeriodPostDTO {
        private Long id;
    }

    @Getter
    @Setter
    @Accessors(chain = true)
    @ApiModel("EvaluationPeriodPostInfoRq")
    public static class InfoWithPostInfo extends EvaluationPeriodPostDTO {
        private Long id;
        private List<EvaluationPeriodPostDTO.PostInfoEvaluationPeriod> postInfoEvaluationPeriod;
    }

    @Getter
    @Setter
    @Accessors(chain = true)
    @ApiModel("PostInfoEvaluationPeriod")
    public static class PostInfoEvaluationPeriod {
        private Long id;
        private Long evaluationPeriodPostId;
        private String postCode;
        private String postTitle;
        private String postCodeParent;
        private String postTitleParent;
        private Long evaluationPeriodId;

    }

    @Getter
    @Setter
    @Accessors(chain = true)
    @ApiModel("EvaluationPeriodPostCreateRq")
    public static class Create extends EvaluationPeriodPostDTO {

    }

    @Getter
    @Setter
    @Accessors(chain = true)
    @ApiModel("EvaluationPeriodPostUpdateRq")
    public static class Update extends EvaluationPeriodPostDTO {
        private Long id;
    }

    @Getter
    @Setter
    @Accessors(chain = true)
    @ApiModel("EvaluationPeriodPostDeleteRq")
    public static class Delete extends EvaluationPeriodPostDTO {
        private Long id;
    }

    @Getter
    @Setter
    @Accessors(chain = true)
    @ApiModel("EvaluationPeriodPostDeleteRq")
    public static class InvalidPostExcel {
        private String groupPostCode;
        private String postTitle;
        private String postGradeTitle;
        private String groupTypeTitle;
        private String description;
        private String totalWeightGroup;
        private Long totalWeightDevelopment;
        private Long totalWeightOperational;
        private Long totalWeightBehavioral;
    }
}
