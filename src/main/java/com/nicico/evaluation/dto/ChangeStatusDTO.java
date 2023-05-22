package com.nicico.evaluation.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@Accessors(chain = true)
public abstract class ChangeStatusDTO {

    private List<Long> evaluationIds;
    private String status;


}
