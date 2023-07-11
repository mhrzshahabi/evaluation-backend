package com.nicico.evaluation.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Getter
@Setter
@Accessors(chain = true)
public class WebSocketDTO {

    private Long id;
    private Integer total;
    private Integer successfulNumber;
    private Integer failedNumber;
    private Integer progressPercent;
}
