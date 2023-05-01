package com.nicico.evaluation.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum EntityManagement {

    TBL_GROUP_TYPE("گروه - نوع"),
    TBL_GROUP_GRADE("گروه - رده"),
    TBL_BATCH("اسناد گروهی"),
    TBL_BATCH_DETAIL("جزییات اسناد گروهی"),
    TBL_CATALOG("کاتالوگ "),
    TBL_GROUP("گروه"),
    TBL_GROUP_TYPE_MERIT("گروه - نوع - شایستگی"),
    TBL_INSTANCE("مصداق"),
    TBL_INSTANCE_GROUP_TYPE_MERIT("مصداق - گروه - نوع - شایستگی"),
    TBL_MERIT_COMPONENT("شایستگی"),
    TBL_MERIT_COMPONENT_TYPE("نوع - شایستگی"),
    TBL_POST_MERIT_COMPONENT("پست - شایستگی "),
    TBL_POST_MERIT_INSTANCE("پست - شایستگی - مصداق"),
    TBL_SENSITIVE_EVENT_PERSON("وقایع حساس - اشخاص"),
    TBL_SENSITIVE_EVENTS("وقایع حساس"),
    TBL_CATALOG_TYPE("نوع کاتالوگ"),
    VIEW_GRADE("رده"),
    VIEW_PERSON("اشخاص"),
    VIEW_PERSONNEL("پرسنل"),
    VIEW_POST("پست"),
    VIEW_GROUP_POST("گروه - پست"),
    TBL_KPI_TYPE("نوع");


    private final String title;

}
