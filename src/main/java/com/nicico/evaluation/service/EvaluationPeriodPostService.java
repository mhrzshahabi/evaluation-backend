package com.nicico.evaluation.service;

import com.nicico.evaluation.dto.EvaluationPeriodPostDTO;
import com.nicico.evaluation.dto.PostRelationDTO;
import com.nicico.evaluation.exception.EvaluationHandleException;
import com.nicico.evaluation.iservice.IEvaluationPeriodPostService;
import com.nicico.evaluation.iservice.IPostRelationService;
import com.nicico.evaluation.mapper.EvaluationPeriodPostMapper;
import com.nicico.evaluation.model.EvaluationPeriodPost;
import com.nicico.evaluation.repository.EvaluationPeriodPostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class EvaluationPeriodPostService implements IEvaluationPeriodPostService {

    private final EvaluationPeriodPostMapper mapper;
    private final IPostRelationService postRelationService;
    private final EvaluationPeriodPostRepository repository;

    @Override
    @Transactional(readOnly = true)
    public List<EvaluationPeriodPostDTO.PostInfoEvaluationPeriod> getAllByEvaluationPeriodId(Long evaluationPeriodId) {
        List<EvaluationPeriodPost> evaluationPeriodPosts = repository.findAllByEvaluationPeriodId(evaluationPeriodId);
        List<String> postCodes =  evaluationPeriodPosts.stream().map(EvaluationPeriodPost::getPostCode).collect(Collectors.toList());
        List<PostRelationDTO.Info> postInfo = postRelationService.getAllByPostCode(postCodes);
        return mapper.postInfoDtoToInfoPostInfoDto(evaluationPeriodPosts, postInfo);
    }

    @Override
    @Transactional
    public List<EvaluationPeriodPostDTO.Info> createAll(Long evaluationPeriodId, Set<String> postCodes) {
        try {
            postCodes = removeDuplicatePostCode(evaluationPeriodId, postCodes);
            if (postCodes.isEmpty())
                throw new Exception();
            List<EvaluationPeriodPost> evaluationPeriodPosts = mapper.listPostCodeToEntities(evaluationPeriodId, postCodes);
            evaluationPeriodPosts = repository.saveAll(evaluationPeriodPosts);
            return mapper.entityToDtoInfoList(evaluationPeriodPosts);
        } catch (Exception exception) {
            throw new EvaluationHandleException(EvaluationHandleException.ErrorType.NotSave);
        }
    }

    @Override
    @Transactional
    public void delete(Long id) {
        EvaluationPeriodPost evaluationPeriodPost = repository.findById(id).orElseThrow(() -> new EvaluationHandleException(EvaluationHandleException.ErrorType.NotFound));
        repository.delete(evaluationPeriodPost);
    }

    @Override
    @Transactional
    public void deleteByEvaluationPeriodId(Long evaluationPeriodId) {
        try {
            repository.deleteByEvaluationPeriodId(evaluationPeriodId);
        } catch (Exception exception) {
            throw new EvaluationHandleException(EvaluationHandleException.ErrorType.NotDeletable);
        }
    }

    @Override
    @Transactional
    public void deleteByEvaluationPeriodIdAndPostCode(Long evaluationPeriodId, String postCode) {
        try {
            repository.findByEvaluationPeriodIdAndPostCode(evaluationPeriodId, postCode)
                    .orElseThrow(() -> new EvaluationHandleException(EvaluationHandleException.ErrorType.NotFound));
            repository.deleteByEvaluationPeriodIdAndPostCode(evaluationPeriodId, postCode);
        } catch (Exception exception) {
            throw new EvaluationHandleException(EvaluationHandleException.ErrorType.NotDeletable);
        }
    }

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

}
