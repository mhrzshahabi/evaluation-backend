package com.nicico.evaluation.mapper;

import com.nicico.evaluation.dto.GradeDTO;
import com.nicico.evaluation.dto.GroupDTO;
import com.nicico.evaluation.dto.GroupGradeDTO;
import com.nicico.evaluation.iservice.IGroupGradeService;
import com.nicico.evaluation.model.Grade;
import com.nicico.evaluation.model.GradeWithoutGroup;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.Named;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;

import java.util.List;

@Mapper(componentModel = "spring")
public abstract class GradeMapper {

    @Lazy
    @Autowired
    private IGroupGradeService groupGradeService;

    @Mappings({
            @Mapping(target = "group", source = "entity", qualifiedByName = "getGroupFromGroupGrade"),
            @Mapping(target = "groupId", source = "entity", qualifiedByName = "getGroupIdFromGroupGrade")
    })
    public abstract GradeDTO.Info entityToDtoInfo(Grade entity);

    public abstract GradeDTO.Info entityToDtoInfo(GradeWithoutGroup entity);

    public abstract List<GradeDTO.Info> entityToDtoInfoList(List<Grade> entities);

    public abstract List<Grade> dtoToEntityList(List<GradeDTO.Info> infos);

    @Named("getGroupFromGroupGrade")
    GroupDTO getGroupFromGroupGrade(Grade grade) {
        GroupGradeDTO.Info groupGradeDTO = groupGradeService.getGroupGradeByGrade(grade);
        if (groupGradeDTO != null && groupGradeDTO.getGroup() != null)
            return groupGradeDTO.getGroup();
        else
            return null;
    }

    @Named("getGroupIdFromGroupGrade")
    Long getGroupIdFromGroupGrade(Grade grade) {
        GroupGradeDTO.Info groupGradeDTO = groupGradeService.getGroupGradeByGrade(grade);
        if (groupGradeDTO != null && groupGradeDTO.getGroup() != null)
            return groupGradeDTO.getGroupId();
        else
            return null;
    }

}
