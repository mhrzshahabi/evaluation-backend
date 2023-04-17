package com.nicico.evaluation.mapper;

import com.nicico.evaluation.dto.GradeDTO;
import com.nicico.evaluation.dto.GroupGradeDTO;
import com.nicico.evaluation.model.GroupGrade;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = "spring")
public abstract class GroupGradeMapper {

    public abstract GroupGrade dtoCreateToEntity(GroupGradeDTO.Create dto);

    @Mappings({
            @Mapping(target = "grade", source = "entity", qualifiedByName = "getGradeFromGroupGrade"),
    })
    public abstract GroupGradeDTO.Info entityToDtoInfo(GroupGrade entity);

    public abstract List<GroupGradeDTO.Info> entityToDtoInfoList(List<GroupGrade> entities);

    public abstract void update(@MappingTarget GroupGrade entity, GroupGradeDTO.Update dto);

    @Named("getGradeFromGroupGrade")
    GradeDTO.Info getGradeFromGroupGrade(GroupGrade groupGrade) {
        GradeDTO.Info gradeDto = new GradeDTO.Info();
        gradeDto.setCode(groupGrade.getGradeCode());
        gradeDto.setTitle(groupGrade.getGradeTitle());
        gradeDto.setId(groupGrade.getGradeId());
        return gradeDto;
    }
}
