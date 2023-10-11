package com.nicico.evaluation.dto;

import io.swagger.annotations.ApiModel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Getter
@Setter
@Accessors(chain = true)
public abstract class WorkSpaceDTO {

    @Getter
    @Setter
    @ApiModel("WorkSpaceInfo")
    public static class Info {

        private String code;
        private String title;
        private String description;
        private Integer number;
    }
}
