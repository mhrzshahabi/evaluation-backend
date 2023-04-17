package com.nicico.evaluation.mapper;

import com.nicico.copper.common.dto.search.SearchDTO;
import com.nicico.evaluation.dto.GradeDTO;
import com.nicico.evaluation.dto.GroupDTO;
import com.nicico.evaluation.dto.GroupGradeDTO;
import com.nicico.evaluation.iservice.IGroupGradeService;
import com.nicico.evaluation.model.Grade;
import com.nicico.evaluation.model.Group;
import com.nicico.evaluation.service.GroupService;

import java.util.Collections;

import org.mapstruct.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;

import java.util.List;

@Mapper(componentModel = "spring")
public abstract class GroupMapper {

    @Lazy
    @Autowired
    private IGroupGradeService groupGradeService;

    public abstract Group dtoCreateToEntity(GroupDTO.Create dto);

    @Mappings({
            @Mapping(target = "grade", source = "entity", qualifiedByName = "getGradeFromGroupGrade"),
    })
    public abstract GroupDTO.Info entityToDtoInfo(Group entity);

    public abstract List<GroupDTO.Info> entityToDtoInfoList(List<Group> entities);

    public abstract void update(@MappingTarget Group entity, GroupDTO.Update dto);

    @Named("getGradeFromGroupGrade")
    List<GradeDTO.Info> getGradeFromGroupGrade(Group group) {
        List<GroupGradeDTO.Info> infos = groupGradeService.getGroupGradeByGroup(group);
        List<GradeDTO.Info> list = infos.stream().map(GroupGradeDTO.Info::getGrade).toList();
        if (!list.isEmpty())
            return list;
        else
            return Collections.emptyList();
    }
}
