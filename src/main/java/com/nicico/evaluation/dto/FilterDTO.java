package com.nicico.evaluation.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class FilterDTO {
    private String field;
    private String type;
    private String operator;
    private List<Object> values;
    private List<Object> fromValues;
    private List<Object> toValues;
}

