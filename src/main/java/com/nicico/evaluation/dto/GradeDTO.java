package com.nicico.evaluation.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.util.Date;

@Getter
@Setter
@Accessors(chain = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public abstract class GradeDTO {
    private Long id;
    private String code;
    private String title;

    @Getter
    @Setter
    @ApiModel("GradeInfo")
    public static class Info extends GradeDTO {
        private Date createdDate;
        private String createdBy;
        private Date lastModifiedDate;
        private String lastModifiedBy;
    }

}
