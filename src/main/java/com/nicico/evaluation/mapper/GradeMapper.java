package com.nicico.evaluation.mapper;

import com.nicico.evaluation.dto.GradeDTO;
import com.nicico.evaluation.model.Grade;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface GradeMapper {
        GradeDTO.Info entityToDtoInfo(Grade entity);
        List<GradeDTO.Info> entityToDtoInfoList(List<Grade> entities);
}
