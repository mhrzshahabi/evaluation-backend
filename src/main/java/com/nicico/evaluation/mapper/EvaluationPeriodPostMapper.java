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


    default List<EvaluationPeriodPostDTO.PostInfoEvaluationPeriod> postInfoDtoToInfoPostInfoDto(List<EvaluationPeriodPost> entities, List<PostRelationDTO.Info> postInfo){
        List<EvaluationPeriodPostDTO.PostInfoEvaluationPeriod> tmp = new ArrayList<>();
        for(EvaluationPeriodPost epp : entities){
            PostRelationDTO.Info postInfoTemp =  postInfo.stream().filter(x-> x.getPostCode().equals(epp.getPostCode())).findFirst().orElse(null);
            if(postInfoTemp != null){
                EvaluationPeriodPostDTO.PostInfoEvaluationPeriod postInfoEvaluationPeriod = new EvaluationPeriodPostDTO.PostInfoEvaluationPeriod();
                postInfoEvaluationPeriod.setEvaluationPeriodPostId(epp.getId());
                postInfoEvaluationPeriod.setId(postInfoTemp.getId());
                postInfoEvaluationPeriod.setPostCode(postInfoTemp.getPostCode());
                postInfoEvaluationPeriod.setPostTitle(postInfoTemp.getPostTitle());
                postInfoEvaluationPeriod.setPostCodeParent(postInfoTemp.getPostCodeParent());
                postInfoEvaluationPeriod.setPostTitleParent(postInfoTemp.getPostTitleParent());
                tmp.add(postInfoEvaluationPeriod);
            }
        }
        return tmp;
    }

    default List<EvaluationPeriodPost> listPostCodeToEntities(Long evaluationPeriodId, Set<String> postCodes){
        List<EvaluationPeriodPost> evaluationPeriodPosts = new ArrayList<>();
        for(String postcode : postCodes){
            EvaluationPeriodPost temp = new EvaluationPeriodPost();
            temp.setEvaluationPeriodId(evaluationPeriodId);
            temp.setPostCode(postcode.trim());
            evaluationPeriodPosts.add(temp);
        }
        return evaluationPeriodPosts;
    }
}
