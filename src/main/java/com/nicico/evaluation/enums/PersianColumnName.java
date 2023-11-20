package com.nicico.evaluation.enums;

import lombok.AllArgsConstructor;

import java.util.Arrays;

@AllArgsConstructor
public enum PersianColumnName {
    CODE("کد"),
    TITLE("عنوان"),
    STATUS("وضعیت"),
    KPITYPETITLE("نوع"),
    GROUPTYPETITLE("گروه"),
    MERITCOMPONENTTITLE("مولفه شایستگی"),
    WEIGHT("وزن"),
    HASINSTANCESTR("مصداق"),
    DESCRIPTION("توضیحات"),
    EXCEPTIONTITLE("عنوان خطا"),
    STATUSCATALOG("وضعیت"),
    GROUPPOSTCODE("کد پست"),
    POSTTITLE("عنوان پست"),
    AVERAGESCORE("میانگین نمره"),
    POSTGRADETITLE("رده پستی"),
    TOTALWEIGHT("مجموع وزن مولفه ها"),
    MOJTAMATITLE("مجتمع"),
    MOAVENATTITLE("معاونت"),
    OMOORTITLE("امور"),
    GHESMATTITLE("قسمت"),
    VAHEDTITLE("واحد"),
    COSTCENTERCODE("کد مرکز هزینه"),
    COSTCENTERTITLE("شرح مرکز هزینه"),
    PERSONCOUNT("تعداد افراد ارزیابی شده"),
    AVERAGEBEHAVIORAL("نمره گروه نوع رفتاری"),
    AVERAGEDEVELOPMENT("نمره گروه نوع توسعه شایستگی ها"),
    AVERAGEOPERATIONAL("نمره گروه نوع نتیجه ای"),
    GROUPTITLE("گروه"),
    TOTALWEIGHTGROUP("مجموع وزن گروه"),
    TOTALWEIGHTDEVELOPMENT("مجموع وزن مولفه های توسعه شایستگی"),
    TOTALWEIGHTOPERATIONAL("مجموع وزن مولفه های نتیجه ای"),
    TOTALWEIGHTBEHAVIORAL("مجموع وزن مولفه های رفتاری"),
    POSTCOMPANYNAME("شرکت"),
    ASSESSFULLNAME("نام ارزیابی شونده"),
    ASSESSPOSTCODE("کدپست ارزیابی شونده"),
    ASSESSPOSTTITLE("پست ارزیابی شونده"),
    ASSESSORFULLNAME("نام ارزیابی کننده"),
    ASSESSORPOSTCODE("کدپست ارزیابی کننده"),
    ASSESSORPOSTTITLE("پست ارزیابی کننده"),
    EVALUATIONPERIODTITLE("دوره ارزیابی"),
    EVALUATIONPERIODSTARTDATEASSESSMENT("شروع ارزشیابی"),
    EVALUATIONPERIODENDDATEASSESSMENT("پایان ارزشیابی"),
    STATUSCATALOGTITLE("وضعیت"),
    STARTDATE("تاریخ شروع"),
    ENDDATE("تاریخ پایان "),
    STARTDATEASSESSMENT("تاریخ شروع ارزشیابی"),
    ENDDATEASSESSMENT("تاریخ پایان ارزشیابی "),
    POSTCODE("کد پست"),
    VALIDATIONSTARTDATE("تاریخ شروع اعتبار سنجی"),
    VALIDATIONENDDATE("تاریخ پایان اعتبار سنجی"),
    LEVELEFFECT("میزان تاثیر"),
    EVENTDATE("تاریخ رویداد"),
    EVENTPOLICYCATALOG("سیاست رویداد"),
    TYPECATALOG("نوع تاثیر"),
    FIRSTNAME("نام"),
    LASTNAME("نام خانوادگی"),
    PERSONNELCODE("شماره پرسنلی");

    private String persianName;

    public static String getPersianColumnName(String englishName) {
        if (englishName == null)
            return "";
        final String fEnglishName = englishName.toLowerCase();
        PersianColumnName ele = Arrays.stream(PersianColumnName.values()).filter(a -> a.name().toLowerCase().equals(fEnglishName)).findFirst().orElse(null);
        if (ele == null)
            return englishName;
        return ele.persianName;
    }
}
