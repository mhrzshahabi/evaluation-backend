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
    DESCRIPTION("شرح"),
    EXCEPTIONTITLE("عنوان خطا"),
    STATUSCATALOG("وضعیت"),
    GROUPPOSTCODE("کد پست"),
    POSTTITLE("عنوان پست"),
    POSTGRADETITLE("رده پستی"),
    TOTALWEIGHT("مجموع وزن مولفه ها"),
    MOJTAMATITLE("مجتمع"),
    OMOORTITLE("امور"),
    GHESMATTITLE("قسمت"),
    VAHEDTITLE("واحد"),
    COSTCENTERCODE("کد مرکز هزینه");


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
