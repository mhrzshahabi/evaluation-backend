package com.nicico.evaluation.mapper;

import com.nicico.evaluation.dto.GroupPostDTO;
import com.nicico.evaluation.iservice.IPostMeritComponentService;
import com.nicico.evaluation.model.GroupPost;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.Named;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;

import java.util.List;

@Mapper(componentModel = "spring")
public abstract class GroupPostMapper {

    @Lazy
    @Autowired
    private IPostMeritComponentService postMeritComponentService;

    @Mappings({
            @Mapping(target = "totalWeight", source = "groupPostCode", qualifiedByName = "getTotalComponentWeightByGroupPostCode")
    })
    public abstract GroupPostDTO.Info entityToDtoInfo(GroupPost entity);

    public abstract List<GroupPostDTO.Info> entityToDtoInfoList(List<GroupPost> entities);

    @Mappings({
            @Mapping(target = "totalWeight", source = "groupPostCode", qualifiedByName = "getTotalComponentWeightByGroupPostCode")
    })
    public abstract GroupPostDTO.Excel entityToDtoExcel(GroupPost entity);

    @Named("getTotalComponentWeightByGroupPostCode")
    Long getTotalComponentWeightByGroupPostCode(String groupPostCode) {
        return postMeritComponentService.getTotalWeight(groupPostCode);
    }
}
