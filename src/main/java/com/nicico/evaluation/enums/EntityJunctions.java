package com.nicico.evaluation.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum EntityJunctions {

    TBL_GROUP_TYPE("گروه - نوع"),
    TBL_GROUP_GRADE("گروه - رده");


    private final String title;

}
