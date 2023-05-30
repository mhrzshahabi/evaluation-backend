package com.nicico.evaluation.mapper;

import com.nicico.copper.common.util.date.DateUtil;
import com.nicico.evaluation.dto.SpecialCaseDTO;
import com.nicico.evaluation.model.SpecialCase;
import org.mapstruct.*;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

@Mapper(componentModel = "spring")
public interface SpecialCaseMapper {

    @Mappings({
            @Mapping(target = "startDate", source = "startDate", qualifiedByName = "convertDateToString"),
            @Mapping(target = "endDate", source = "endDate", qualifiedByName = "convertDateToString")
    })
     SpecialCase dtoCreateToEntity(SpecialCaseDTO.Create dto);

    @Mappings({
            @Mapping(target = "startDate", source = "startDate", qualifiedByName = "convertStringToDate"),
            @Mapping(target = "endDate", source = "endDate", qualifiedByName = "convertStringToDate")
    })
     SpecialCaseDTO.Info entityToDtoInfo(SpecialCase entity);

     List<SpecialCaseDTO.Info> entityToDtoInfoList(List<SpecialCase> entities);

    @Mappings({
            @Mapping(target = "startDate", source = "startDate", qualifiedByName = "convertDateToString"),
            @Mapping(target = "endDate", source = "endDate", qualifiedByName = "convertDateToString")
    })
     void update(@MappingTarget SpecialCase entity, SpecialCaseDTO.Update dto);

    @Named("convertDateToString")
   default String convertDateToString(Date date) {
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        dateFormat.setTimeZone(TimeZone.getTimeZone("Asia/Tehran"));
        return DateUtil.convertMiToKh(dateFormat.format(date));
    }

    @Named("convertStringToDate")
    default Date convertStringToDate(String date) throws ParseException {
        String miDate = DateUtil.convertKhToMi1(date);
        return new SimpleDateFormat("yyyy-MM-dd").parse(miDate);
    }
}
    