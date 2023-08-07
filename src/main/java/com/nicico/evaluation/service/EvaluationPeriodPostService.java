package com.nicico.evaluation.service;

import com.nicico.copper.common.dto.search.SearchDTO;
import com.nicico.evaluation.dto.EvaluationDTO;
import com.nicico.evaluation.dto.EvaluationPeriodPostDTO;
import com.nicico.evaluation.exception.EvaluationHandleException;
import com.nicico.evaluation.iservice.ICatalogService;
import com.nicico.evaluation.iservice.IEvaluationPeriodPostService;
import com.nicico.evaluation.mapper.EvaluationPeriodPostMapper;
import com.nicico.evaluation.model.EvaluationPeriod;
import com.nicico.evaluation.model.EvaluationPeriodPost;
import com.nicico.evaluation.repository.EvaluationPeriodPostRepository;
import com.nicico.evaluation.repository.EvaluationPeriodRepository;
import com.nicico.evaluation.repository.PostRelationRepository;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.Range;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class EvaluationPeriodPostService implements IEvaluationPeriodPostService {

    private final ICatalogService catalogService;
    private final EvaluationPeriodPostMapper mapper;
    private final EvaluationService evaluationService;
    private final EvaluationPeriodPostRepository repository;
    private final ResourceBundleMessageSource messageSource;
    private final PostRelationRepository postRelationRepository;
    private final EvaluationPeriodRepository evaluationPeriodRepository;


    @Override
    @Transactional(readOnly = true)
    public List<EvaluationPeriodPostDTO.Info> getByEvaluationPeriodId(Long evaluationPeriodId) {
        List<EvaluationPeriodPost> list = repository.findAllByEvaluationPeriodId(evaluationPeriodId);
        return mapper.entityToDtoInfoList(list);
    }

    @Override
    @Transactional(readOnly = true)
    public List<EvaluationPeriodPostDTO.PostInfoEvaluationPeriod> getAllByEvaluationPeriodId(Long evaluationPeriodId) {
        List<?> list = postRelationRepository.getPostInfoEvaluationPeriodList(evaluationPeriodId);
        return mapper.objectToInfoPostInfoDto(list);
    }

    @Override
    @Transactional
    public List<EvaluationPeriodPostDTO.Info> createAll(EvaluationPeriod newEvaluationPeriod, Set<String> postCode) {
        try {
            postCode = checkPostCodesForAdd(newEvaluationPeriod, postCode);
            if (postCode.isEmpty())
                throw new Exception("equal");
            List<EvaluationPeriodPost> evaluationPeriodPosts = mapper.listPostCodeToEntities(newEvaluationPeriod.getId(), postCode);
            evaluationPeriodPosts = repository.saveAll(evaluationPeriodPosts);
            return mapper.entityToDtoInfoList(evaluationPeriodPosts);
        } catch (Exception exception) {
            if (exception.getMessage().equals("equal")) {
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

        EvaluationDTO.Info evaluation = evaluationService.getAllByPeriodIdAndAssessPostCode(evaluationPeriodId, postCode);
        if (Objects.nonNull(evaluation))
            throw new EvaluationHandleException(EvaluationHandleException.ErrorType.NotDeletable, "", messageSource.getMessage(
                    "message.cant.remove.used.post",
                    new Object[]{evaluation.getEvaluationPeriod().getTitle()}, LocaleContextHolder.getLocale()));
        try {
            repository.findByEvaluationPeriodIdAndPostCode(evaluationPeriodId, postCode)
                    .orElseThrow(() -> new EvaluationHandleException(EvaluationHandleException.ErrorType.NotFound));
            repository.deleteByEvaluationPeriodIdAndPostCode(evaluationPeriodId, postCode);
        } catch (Exception exception) {
            throw new EvaluationHandleException(EvaluationHandleException.ErrorType.NotDeletable);
        }
    }

    private Set<String> checkPostCodesForAdd(EvaluationPeriod evaluationPeriod, Set<String> postCodes) {

        List<EvaluationPeriod> overlappedEvaluationPeriodList = new ArrayList<>();
        Long evalPeriodAwaitingId =  catalogService.getByCode("period-awaiting-review").getId();
        Range<String> range1 = Range.between(evaluationPeriod.getStartDate(), evaluationPeriod.getEndDate());
        List<EvaluationPeriod> evaluationPeriodList = evaluationPeriodRepository.findAllByStatusCatalogId(evalPeriodAwaitingId);
        evaluationPeriodList.forEach(item -> {
            Range<String> range2 = Range.between(item.getStartDate(), item.getEndDate());
            if (range1.isOverlappedBy(range2))
                overlappedEvaluationPeriodList.add(item);
        });

        Set<String> postCodeList = repository.findAllByEvaluationPeriodIdIn(overlappedEvaluationPeriodList.stream().map(EvaluationPeriod::getId).collect(Collectors.toSet()))
                .stream().map(EvaluationPeriodPost::getPostCode).collect(Collectors.toSet());
        postCodes.removeAll(postCodeList);
        return postCodes;
    }

//    private Set<String> check1(EvaluationPeriod newEvaluationPeriod, Set<String> postCode) {
//        Boolean canAdded = Boolean.FALSE;
//        Set<String> newPostCodes = new HashSet<>();
//        List<EvaluationPeriodPost> evaluationPeriodPosts = repository.findAllByPostCodeIn(postCode);
//        for (String pc : postCode) {
//            if (evaluationPeriodPosts.stream().anyMatch(x -> x.getEvaluationPeriodId().equals(newEvaluationPeriod.getId()) && x.getPostCode().equals(pc)))
//                continue;
//            List<EvaluationPeriodPost> evaluationPeriodPostsFilter = evaluationPeriodPosts.stream().filter(x -> x.getPostCode().equals(pc)).collect(Collectors.toList());
//            canAdded = Boolean.TRUE;
//            for (EvaluationPeriodPost epp : evaluationPeriodPostsFilter) {
//                EvaluationPeriod evaluationPeriod = evaluationPeriodRepository.findById(epp.getEvaluationPeriodId())
//                        .orElseThrow(() -> new EvaluationHandleException(EvaluationHandleException.ErrorType.NotFound));
//                if (newEvaluationPeriod.getStartDate().equals(evaluationPeriod.getStartDate()) &&
//                        newEvaluationPeriod.getEndDate().equals(evaluationPeriod.getEndDate()) &&
//                        newEvaluationPeriod.getStartDateAssessment().equals(evaluationPeriod.getStartDateAssessment()) &&
//                        newEvaluationPeriod.getEndDateAssessment().equals(evaluationPeriod.getEndDateAssessment())
//                ) {
//                    canAdded = Boolean.FALSE;
//                    break;
//                }
//            }
//            if (canAdded)
//                newPostCodes.add(pc);
//        }
//        return newPostCodes;
//    }


    @Override
    @Transactional(readOnly = true)
    public List<String> getUnUsedPostCodeByEvaluationPeriodId(Long evaluationPeriodId) {
        return postRelationRepository.getUnUsedPostCodeByEvaluationPeriodId(evaluationPeriodId);
    }

    @Override
    public SearchDTO.SearchRs<EvaluationPeriodPostDTO.Info> search(SearchDTO.SearchRq request) throws IllegalAccessException, NoSuchFieldException {
        return BaseService.optimizedSearch(repository, mapper::entityToDtoInfo, request);
    }

}
