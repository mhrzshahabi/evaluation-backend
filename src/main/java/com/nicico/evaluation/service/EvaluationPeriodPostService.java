package com.nicico.evaluation.service;

import com.nicico.evaluation.dto.EvaluationPeriodPostDTO;
import com.nicico.evaluation.dto.PostRelationDTO;
import com.nicico.evaluation.exception.EvaluationHandleException;
import com.nicico.evaluation.iservice.IEvaluationPeriodPostService;
import com.nicico.evaluation.iservice.IEvaluationPeriodService;
import com.nicico.evaluation.iservice.IPostRelationService;
import com.nicico.evaluation.mapper.EvaluationPeriodPostMapper;
import com.nicico.evaluation.model.EvaluationPeriod;
import com.nicico.evaluation.model.EvaluationPeriodPost;
import com.nicico.evaluation.repository.EvaluationPeriodPostRepository;
import com.nicico.evaluation.repository.EvaluationPeriodRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class EvaluationPeriodPostService implements IEvaluationPeriodPostService {

    private final EvaluationPeriodRepository evaluationPeriodRepository;
    private final EvaluationPeriodPostMapper mapper;
    private final IPostRelationService postRelationService;
    private final EvaluationPeriodPostRepository repository;
    private final ResourceBundleMessageSource messageSource;


    @Override
    @Transactional(readOnly = true)
    public List<EvaluationPeriodPostDTO.PostInfoEvaluationPeriod> getAllByEvaluationPeriodId(Long evaluationPeriodId) {
        List<EvaluationPeriodPost> evaluationPeriodPosts = repository.findAllByEvaluationPeriodId(evaluationPeriodId);
        List<String> postCode =  evaluationPeriodPosts.stream().map(EvaluationPeriodPost::getPostCode).collect(Collectors.toList());
        List<PostRelationDTO.Info> postInfo = postRelationService.getAllByPostCode(postCode);
        return mapper.postInfoDtoToInfoPostInfoDto(evaluationPeriodPosts, postInfo);
    }

    @Override
    @Transactional
    public List<EvaluationPeriodPostDTO.Info> createAll(EvaluationPeriod newEvaluationPeriod, Set<String> postCode) {
        try {
//            postCode = removeDuplicatePostCode(evaluationPeriod.getId(), postCode);
//            if (postCode.isEmpty())
//                throw new Exception();
            postCode = check1(newEvaluationPeriod, postCode);
            if (postCode.isEmpty())
                throw new Exception("equal");
            List<EvaluationPeriodPost> evaluationPeriodPosts = mapper.listPostCodeToEntities(newEvaluationPeriod.getId(), postCode);
            evaluationPeriodPosts = repository.saveAll(evaluationPeriodPosts);
            return mapper.entityToDtoInfoList(evaluationPeriodPosts);
        } catch (Exception exception) {
            if(exception.getMessage().equals("equal")){
                String errmsg = messageSource.getMessage("message.equal.evaluation.period", null, LocaleContextHolder.getLocale());
                throw new EvaluationHandleException(EvaluationHandleException.ErrorType.NotSave, null, errmsg);
            }
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

    private Set<String> check1(EvaluationPeriod newEvaluationPeriod, Set<String> postCode){
        Boolean canAdded = Boolean.FALSE;
        Set<String> newPostCodes = new HashSet<>();
        List<EvaluationPeriodPost> evaluationPeriodPosts = repository.findAllByPostCodeIn(postCode);
        for(String pc : postCode){
            if(evaluationPeriodPosts.stream().anyMatch(x -> x.getEvaluationPeriodId().equals(newEvaluationPeriod.getId()) && x.getPostCode().equals(pc)) )
                continue;
            List<EvaluationPeriodPost> evaluationPeriodPostsFilter =  evaluationPeriodPosts.stream().filter(x-> x.getPostCode().equals(pc)).collect(Collectors.toList());
            canAdded = Boolean.TRUE;
            for(EvaluationPeriodPost epp : evaluationPeriodPostsFilter) {
                EvaluationPeriod evaluationPeriod = evaluationPeriodRepository.findById(epp.getEvaluationPeriodId())
                        .orElseThrow(() -> new EvaluationHandleException(EvaluationHandleException.ErrorType.NotFound));
                if (newEvaluationPeriod.getStartDate().equals(evaluationPeriod.getStartDate()) &&
                        newEvaluationPeriod.getEndDate().equals(evaluationPeriod.getEndDate()) &&
                        newEvaluationPeriod.getStartDateAssessment().equals(evaluationPeriod.getStartDateAssessment()) &&
                        newEvaluationPeriod.getEndDateAssessment().equals(evaluationPeriod.getEndDateAssessment())
                ) {
                    canAdded = Boolean.FALSE;
                    break;
                }
            }
            if(canAdded)
                newPostCodes.add(pc);
        }
        return newPostCodes;
    }

    private Set<String> check(EvaluationPeriod newEvaluationPeriod, Set<String> postCode){
        Boolean canAdded = Boolean.FALSE;
        Set<String> newPostCodes = new HashSet<>();
        List<EvaluationPeriodPost> evaluationPeriodPosts = repository.findAllByEvaluationPeriodId(newEvaluationPeriod.getId());
        List<String> postCodesInDb = evaluationPeriodPosts.stream().map(EvaluationPeriodPost::getPostCode).collect(Collectors.toList());
        for(String pc : postCode){
            if (postCodesInDb.stream().noneMatch(pc::equals))
            {
                canAdded = Boolean.TRUE;
                List<EvaluationPeriodPost> evaluationPeriodPosts1 = repository.findAllByPostCode(pc);
                for(EvaluationPeriodPost epp : evaluationPeriodPosts1){
                    EvaluationPeriod evaluationPeriod = evaluationPeriodRepository.findById(epp.getEvaluationPeriodId())
                            .orElseThrow(() -> new EvaluationHandleException(EvaluationHandleException.ErrorType.NotFound));
                    if(newEvaluationPeriod.getStartDate().equals(evaluationPeriod.getStartDate()) &&
                            newEvaluationPeriod.getEndDate().equals(evaluationPeriod.getEndDate()) &&
                            newEvaluationPeriod.getStartDateAssessment().equals(evaluationPeriod.getStartDateAssessment()) &&
                            newEvaluationPeriod.getEndDateAssessment().equals(evaluationPeriod.getEndDateAssessment())
                    ) {
                        canAdded = Boolean.FALSE;
                        break;
                    }
                }
            }
            if(canAdded)
                newPostCodes.add(pc);
        }
        return newPostCodes;
    }

//    private List<String> getAllPostCodeByEvaluationPeriodId(Long evaluationPeriodId) {
//        List<EvaluationPeriodPost> evaluationPeriodPosts = repository.findAllByEvaluationPeriodId(evaluationPeriodId);
//        return evaluationPeriodPosts.stream().map(EvaluationPeriodPost::getPostCode).collect(Collectors.toList());
//    }
//
//    private Set<String> removeDuplicatePostCode(Long evaluationPeriodId, Set<String> postCode) {
//        List<String> postCodesInDb = getAllPostCodeByEvaluationPeriodId(evaluationPeriodId);
//        Set<String> newPostCodes = new HashSet<>();
//        for (String pc : postCode) {
//            if (postCodesInDb.stream().noneMatch(pc::equals))
//                newPostCodes.add(pc);
//        }
//        return newPostCodes;
//    }

}
