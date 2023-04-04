package com.nicico.evaluation.group;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Max;
import javax.validation.constraints.NotNull;

public class GroupDTO {

    @Getter
    @Setter
    @AllArgsConstructor
    public static class create {
        @NotNull
        private String code;

        @NotNull
        private String title;

        @NotNull
        private Boolean definitionAllowed;
    }

    @Getter
    @Setter
    @AllArgsConstructor
    public static class retrieve{
        private Integer id;
        private String code;
        private String title;
        private Boolean definitionAllowed;
    }
}
