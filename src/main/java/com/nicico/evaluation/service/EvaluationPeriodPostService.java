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

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class EvaluationPeriodPostService implements IEvaluationPeriodPostService {

    private final EvaluationPeriodPostRepository repository;
    private final EvaluationPeriodPostMapper mapper;
    private final IPostService postService;

    private List<String> getAllPostCodeByEvaluationPeriodId(Long evaluationPeriodId) {
        List<EvaluationPeriodPost> evaluationPeriodPosts = repository.findAllByEvaluationPeriodId(evaluationPeriodId);
        return evaluationPeriodPosts.stream().map(EvaluationPeriodPost::getPostCode).collect(Collectors.toList());
    }

    private Set<String> removeDuplicatePostCode(Long evaluationPeriodId, Set<String> postCodes) {
        List<String> postCodesInDb = getAllPostCodeByEvaluationPeriodId(evaluationPeriodId);
        Set<String> newPostCodes = new HashSet<>();
        for (String pc : postCodes) {
            if (postCodesInDb.stream().noneMatch(pc::equals))
                newPostCodes.add(pc);
        }
        return newPostCodes;
    }

    @Override
    public List<EvaluationPeriodPostDTO.PostInfoEvaluationPeriod> getAllByEvaluationPeriodId(Long evaluationPeriodId) {
        List<String> postCodes = getAllPostCodeByEvaluationPeriodId(evaluationPeriodId);
        List<PostDTO.Info> postInfo = postService.getAllByPostCode(postCodes);
        return mapper.postInfoDtoToInfoPostInfoDto(postInfo);
    }


    @Override
    public List<EvaluationPeriodPostDTO.Info> createAll(Long evaluationPeriodId, Set<String> postCodes) {
        try {
            postCodes = removeDuplicatePostCode(evaluationPeriodId, postCodes);
            if (postCodes.isEmpty())
                throw new Exception();
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
        } catch (Exception exception) {
            throw new EvaluationHandleException(EvaluationHandleException.ErrorType.NotDeletable);
        }
    }

    @Override
    public void deleteByEvaluationPeriodIdAndPostCode(Long evaluationPeriodId, String postCode) {
        try {
            repository.findByEvaluationPeriodIdAndPostCode(evaluationPeriodId, postCode)
                    .orElseThrow(() -> new EvaluationHandleException(EvaluationHandleException.ErrorType.NotFound));
            repository.deleteByEvaluationPeriodIdAndPostCode(evaluationPeriodId, postCode);
        } catch (Exception exception) {
            throw new EvaluationHandleException(EvaluationHandleException.ErrorType.NotDeletable);
        }
    }

}
