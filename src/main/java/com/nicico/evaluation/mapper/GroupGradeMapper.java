package com.nicico.evaluation.mapper;

import com.nicico.evaluation.dto.GroupGradeDTO;
import com.nicico.evaluation.model.GroupGrade;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

import java.util.List;

@Mapper(componentModel = "spring")
public interface GroupGradeMapper {

    GroupGrade dtoCreateToEntity(GroupGradeDTO.Create dto);

    GroupGradeDTO.Info entityToDtoInfo(GroupGrade entity);

    List<GroupGradeDTO.Info> entityToDtoInfoList(List<GroupGrade> entities);

    void update(@MappingTarget GroupGrade entity, GroupGradeDTO.Update dto);
}
