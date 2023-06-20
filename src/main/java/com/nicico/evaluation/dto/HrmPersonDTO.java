package com.nicico.evaluation.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Getter
@Setter
@Accessors(chain = true)
public class HrmPersonDTO {
    private String firstName;
    private String lastName;
    private String nationalCode;
    private String imageProfile;
}
