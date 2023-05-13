package com.nicico.evaluation.service;

import com.nicico.evaluation.dto.EvaluationPeriodPostDTO;
import com.nicico.evaluation.dto.PostDTO;
import com.nicico.evaluation.exception.EvaluationHandleException;
import com.nicico.evaluation.iservice.IEvaluationPeriodPostService;
import com.nicico.evaluation.iservice.IPostService;
import com.nicico.evaluation.mapper.EvaluationPeriodPostMapper;
import com.nicico.evaluation.model.EvaluationPeriodPost;
import com.nicico.evaluation.repository.EvaluationPeriodPostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class EvaluationPeriodPostService  implements IEvaluationPeriodPostService {

    private final EvaluationPeriodPostRepository repository;
    private final EvaluationPeriodPostMapper mapper;
    private final IPostService postService;

    @Override
    public List<EvaluationPeriodPostDTO.PostInfoEvaluationPeriod> getAllByEvaluationPeriodId(Long evaluationPeriodId) {
        List<EvaluationPeriodPost> evaluationPeriodPosts =  repository.findAllByEvaluationPeriodId(evaluationPeriodId);
        List<String> postCodes = evaluationPeriodPosts.stream().map(EvaluationPeriodPost::getPostCode).collect(Collectors.toList());
        List<PostDTO.Info> postInfo = postService.getAllByPostCode(postCodes);
        List<EvaluationPeriodPostDTO.PostInfoEvaluationPeriod> evaluationPeriodPostInfos = mapper.postInfoDtoToInfoPostInfoDto(postInfo);
        return  evaluationPeriodPostInfos;
    }

    @Override
    public List<EvaluationPeriodPostDTO.Info> createAll(Long evaluationPeriodId, Set<String> postCodes) {
        try {
            List<EvaluationPeriodPost> evaluationPeriodPosts = mapper.listPostCodeToEntites(evaluationPeriodId, postCodes);
            evaluationPeriodPosts = repository.saveAll(evaluationPeriodPosts);
            return mapper.entityToDtoInfoList(evaluationPeriodPosts);
        } catch (Exception exception) {
            throw new EvaluationHandleException(EvaluationHandleException.ErrorType.NotSave);
        }
    }

    @Override
    public void deleteByEvaluationPeriodId(Long evaluationPeriodId) {
        try {
            repository.deleteByEvaluationPeriodId(evaluationPeriodId);
        }catch (Exception exception) {
            throw new EvaluationHandleException(EvaluationHandleException.ErrorType.NotDeletable);
        }
    }
}
