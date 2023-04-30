package com.nicico.evaluation.enums;

import lombok.AllArgsConstructor;

import java.util.Arrays;

@AllArgsConstructor
public enum PersianColumnName {
    CODE("کد"),
    TITLE("عنوان");

    private String persianName;


    public static String getPersianColumnName(String englishName){
        if(englishName == null)
            return "";
        final String fEnglishName = englishName.toLowerCase();
        PersianColumnName ele = Arrays.stream(PersianColumnName.values()).filter(a -> a.name().toLowerCase().equals(fEnglishName)).findFirst().orElse(null);
        if(ele == null)
            return englishName;
        return ele.persianName;
    }
}