package com.nicico.evaluation.mapper;

import com.nicico.evaluation.dto.EvaluationPeriodPostDTO;
import com.nicico.evaluation.model.EvaluationPeriodPost;
import org.mapstruct.Mapper;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;


@Mapper(componentModel = "spring")
public interface EvaluationPeriodPostMapper {
    EvaluationPeriodPostDTO.Info entityToDtoInfo(EvaluationPeriodPost entity);
    List<EvaluationPeriodPostDTO.Info> entityToDtoInfoList(List<EvaluationPeriodPost> entities);
    default List<EvaluationPeriodPost> listPostCodeToEntites(Long evaluationPeriodId, Set<String> postCodes){
        List<EvaluationPeriodPost> evaluationPeriodPosts = new ArrayList<>();
        for(String postcode : postCodes){
            EvaluationPeriodPost temp = new EvaluationPeriodPost();
            temp.setEvaluationPeriodId(evaluationPeriodId);
            temp.setPostCode(postcode);
            evaluationPeriodPosts.add(temp);
        }
        return evaluationPeriodPosts;
    }
}
