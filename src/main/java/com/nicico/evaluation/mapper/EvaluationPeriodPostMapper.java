package com.nicico.evaluation.mapper;

import com.nicico.evaluation.dto.EvaluationPeriodPostDTO;
import com.nicico.evaluation.dto.PostRelationDTO;
import com.nicico.evaluation.model.EvaluationPeriodPost;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;


@Mapper(componentModel = "spring")
public interface EvaluationPeriodPostMapper {

    EvaluationPeriodPostDTO.Info entityToDtoInfo(EvaluationPeriodPost entity);

    List<EvaluationPeriodPostDTO.Info> entityToDtoInfoList(List<EvaluationPeriodPost> entities);


    default List<EvaluationPeriodPostDTO.PostInfoEvaluationPeriod> objectToInfoPostInfoDto(List<?> data) {
        if (data != null) {
            data.forEach(p ->
                    {
                        Object[] objects = (Object[]) p;
                        EvaluationPeriodPostDTO.PostInfoEvaluationPeriod item = new EvaluationPeriodPostDTO.PostInfoEvaluationPeriod();
                        item.setId(objects[5] == null ? null : Long.parseLong(objects[5].toString()));
                        item.setEvaluationPeriodPostId(objects[0] == null ? null : Long.parseLong(objects[0].toString()));
                        item.setPostCode(objects[1] == null ? null : objects[1].toString());
                        item.setPostTitle(objects[2] == null ? null : objects[2].toString());
                        item.setPostCodeParent(objects[3] == null ? null : objects[3].toString());
                        item.setPostTitleParent(objects[4] == null ? null : objects[4].toString());
                    }
            );
        }
        return new ArrayList<>();

    }

    default List<EvaluationPeriodPost> listPostCodeToEntities(Long evaluationPeriodId, Set<String> postCode) {
        List<EvaluationPeriodPost> evaluationPeriodPosts = new ArrayList<>();
        for (String postcode : postCode) {
            EvaluationPeriodPost temp = new EvaluationPeriodPost();
            temp.setEvaluationPeriodId(evaluationPeriodId);
            temp.setPostCode(postcode.trim());
            evaluationPeriodPosts.add(temp);
        }
        return evaluationPeriodPosts;
    }
}
