package com.nicico.evaluation.service;

import com.nicico.copper.common.dto.search.SearchDTO;
import com.nicico.evaluation.common.PageableMapper;
import com.nicico.evaluation.dto.CatalogDTO;
import com.nicico.evaluation.dto.EvaluationItemDTO;
import com.nicico.evaluation.dto.EvaluationItemInstanceDTO;
import com.nicico.evaluation.exception.EvaluationHandleException;
import com.nicico.evaluation.iservice.*;
import com.nicico.evaluation.mapper.EvaluationItemMapper;
import com.nicico.evaluation.model.Evaluation;
import com.nicico.evaluation.model.EvaluationItem;
import com.nicico.evaluation.model.GroupType;
import com.nicico.evaluation.repository.EvaluationItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static com.nicico.evaluation.utility.EvaluationConstant.*;

@RequiredArgsConstructor
@Service
public class EvaluationItemService implements IEvaluationItemService {

    private final EvaluationItemMapper mapper;
    private final EvaluationItemRepository repository;
    private final PageableMapper pageableMapper;
    private final IEvaluationService evaluationService;
    private final IGroupTypeService groupTypeService;
    private final IGroupTypeMeritService groupTypeMeritService;
    private final IPostMeritComponentService postMeritComponentService;
    private final ICatalogService catalogService;
    private final IEvaluationItemInstanceService evaluationItemInstanceService;


    @Override
    @Transactional(readOnly = true)
    @PreAuthorize("hasAuthority('R_EVALUATION_ITEM')")
    public EvaluationItemDTO.SpecResponse list(int count, int startIndex) {
        Pageable pageable = pageableMapper.toPageable(count, startIndex);
        Page<EvaluationItem> evaluationItems = repository.findAll(pageable);
        List<EvaluationItemDTO.Info> evaluationItemInfos = mapper.entityToDtoInfoList(evaluationItems.getContent());

        EvaluationItemDTO.Response response = new EvaluationItemDTO.Response();
        EvaluationItemDTO.SpecResponse specResponse = new EvaluationItemDTO.SpecResponse();

        if (evaluationItemInfos != null) {
            response.setData(evaluationItemInfos)
                    .setStartRow(startIndex)
                    .setEndRow(startIndex + count)
                    .setTotalRows((int) evaluationItems.getTotalElements());
            specResponse.setResponse(response);
        }
        return specResponse;
    }

    @Override
    @Transactional(readOnly = true)
    @PreAuthorize("hasAuthority('R_EVALUATION_ITEM')")
    public EvaluationItemDTO.Info get(Long id) {
        EvaluationItem evaluationItem = repository.findById(id).orElseThrow(() -> new EvaluationHandleException(EvaluationHandleException.ErrorType.NotFound));
        return mapper.entityToDtoInfo(evaluationItem);
    }

    @Override
    @Transactional(readOnly = true)
    @PreAuthorize("hasAuthority('R_EVALUATION_ITEM')")
    public List<EvaluationItemDTO.PostMeritTupleDTO> getAllPostMeritByEvalId(Long evaluationId) {
        List<EvaluationItem> allPostMeritByEvalId = repository.getAllPostMeritByEvalId(evaluationId);
        return mapper.entityToPostMeritInfoDtoList(allPostMeritByEvalId);
    }

    @Override
    @Transactional(readOnly = true)
    @PreAuthorize("hasAuthority('R_EVALUATION_ITEM')")
    public List<EvaluationItemDTO.MeritTupleDTO> getAllGroupTypeMeritByEvalId(Long evaluationId) {
        List<EvaluationItem> allGroupTypeMeritByEvalId = repository.getAllGroupTypeMeritByEvalId(evaluationId);
        return mapper.entityToUpdateInfoDtoList(allGroupTypeMeritByEvalId);
    }

    @Override
    @Transactional
    @PreAuthorize("hasAuthority('C_EVALUATION_ITEM')")
    public EvaluationItemDTO.Info create(EvaluationItemDTO.Create dto) {
        EvaluationItem entity = mapper.dtoCreateToEntity(dto);
        try {
            EvaluationItem evaluationItem = repository.save(entity);
            UpdateEvaluation(dto);
            createAllEvaluationInstance(dto, evaluationItem);

            return mapper.entityToDtoInfo(evaluationItem);
        } catch (Exception exception) {
            throw new EvaluationHandleException(EvaluationHandleException.ErrorType.NotSave);
        }
    }

    private void createAllEvaluationInstance(EvaluationItemDTO.Create dto, EvaluationItem evaluationItem) {

        List<EvaluationItemInstanceDTO.Create> createInstanceList = new ArrayList<>();
        if (Objects.nonNull(dto.getPostMeritInstanceList()) && !dto.getPostMeritInstanceList().isEmpty()) {
            dto.getPostMeritInstanceList().forEach(postMeritInstance -> {
                EvaluationItemInstanceDTO.Create createInstance = new EvaluationItemInstanceDTO.Create();
                createInstance.setInstanceId(postMeritInstance.getInstanceId());
                createInstance.setPostMeritInstanceId(postMeritInstance.getId());
                createInstance.setEvaluationItemId(evaluationItem.getId());
                createInstanceList.add(createInstance);
            });
        } else if (Objects.nonNull(dto.getInstanceGroupTypeMerits()) && !dto.getInstanceGroupTypeMerits().isEmpty()) {
            dto.getInstanceGroupTypeMerits().forEach(instanceGroupTypeMerit -> {
                EvaluationItemInstanceDTO.Create createInstance = new EvaluationItemInstanceDTO.Create();
                createInstance.setInstanceId(instanceGroupTypeMerit.getInstanceId());
                createInstance.setPostMeritInstanceId(instanceGroupTypeMerit.getId());
                createInstance.setEvaluationItemId(evaluationItem.getId());
                createInstanceList.add(createInstance);
            });
        }
        evaluationItemInstanceService.createAll(createInstanceList);
    }

    private void UpdateEvaluation(EvaluationItemDTO.Create dto) {
        Evaluation evaluation = evaluationService.getById(dto.getEvaluationId());
        evaluation.setAverageScore(dto.getAverageScore());
        Long statusByCatalogType = catalogService.catalogByCatalogTypeCode(EVALUATION_STATUS).stream()
                .filter(status -> status.getCode().equals(FINALIZED)).findFirst().orElseThrow().getId();
        evaluation.setStatusCatalogId(statusByCatalogType);
        evaluationService.update(dto.getEvaluationId(), evaluation);
    }

    @Override
    @Transactional
    @PreAuthorize("hasAuthority('C_EVALUATION_ITEM')")
    public List<EvaluationItemDTO.Info> createAll(List<EvaluationItemDTO.Create> requests) {
        return requests.stream().map(this::create).toList();
    }

    @Override
    @Transactional
    @PreAuthorize("hasAuthority('U_EVALUATION_ITEM')")
    public EvaluationItemDTO.Info update(Long id, EvaluationItemDTO.Update dto) {
        EvaluationItem evaluationItem = repository.findById(id).orElseThrow(() -> new EvaluationHandleException(EvaluationHandleException.ErrorType.NotFound));
        try {
            EvaluationItem save = repository.save(evaluationItem);
            Evaluation evaluation = evaluationService.getById(dto.getEvaluationId());
            evaluation.setAverageScore(dto.getAverageScore());
            evaluationService.update(dto.getEvaluationId(), evaluation);
            return mapper.entityToDtoInfo(save);
        } catch (Exception exception) {
            throw new EvaluationHandleException(EvaluationHandleException.ErrorType.NotEditable);
        }
    }

    @Override
    @Transactional
    @PreAuthorize("hasAuthority('D_EVALUATION_ITEM')")
    public void delete(Long id) {
        EvaluationItem evaluationItem = repository.findById(id).orElseThrow(() -> new EvaluationHandleException(EvaluationHandleException.ErrorType.NotFound));
        try {
            repository.delete(evaluationItem);
        } catch (DataIntegrityViolationException violationException) {
            throw new EvaluationHandleException(EvaluationHandleException.ErrorType.IntegrityConstraint);
        } catch (Exception exception) {
            throw new EvaluationHandleException(EvaluationHandleException.ErrorType.NotDeletable);
        }
    }

    @Override
    @Transactional(readOnly = true)
    @PreAuthorize("hasAuthority('R_EVALUATION_ITEM')")
    public SearchDTO.SearchRs<EvaluationItemDTO.Info> search(SearchDTO.SearchRq request) throws IllegalAccessException, NoSuchFieldException {
        return BaseService.optimizedSearch(repository, mapper::entityToDtoInfo, request);
    }

    @Override
    @Transactional(readOnly = true)
    @PreAuthorize("hasAuthority('R_EVALUATION_ITEM')")
    public List<EvaluationItemDTO.CreateItemInfo> getInfoByAssessPostCodeForCreate(String assessPostCode) {

        List<EvaluationItemDTO.CreateItemInfo> createItemInfoList = new ArrayList<>();
        getGroupTypeMeritInfoForCreate(assessPostCode, createItemInfoList);
        getPostMeritInfoForCreate(assessPostCode, createItemInfoList);
        calculateAndSetTotalWeight(createItemInfoList);

        return createItemInfoList;
    }

    private void calculateAndSetTotalWeight(List<EvaluationItemDTO.CreateItemInfo> createItemInfoList) {

        List<CatalogDTO.Info> answerListByCatalog = catalogService.catalogByCatalogTypeCode(QUESTIONNAIRE_ANSWERS);
        createItemInfoList.forEach(item -> {
            Double sumMeritsWeight = item.getMeritTuple().stream().mapToDouble(EvaluationItemDTO.MeritTupleDTO::getWeight).sum();

            item.getMeritTuple().forEach(merit -> {

                List<CatalogDTO.Info> convertedAnswerListByCatalog = new ArrayList<>();
                List<CatalogDTO.Info> groupTypeWeightConvertedByCatalog = new ArrayList<>();
                List<CatalogDTO.Info> totalItemWeightConvertedByCatalog = new ArrayList<>();

                answerListByCatalog.forEach(answer -> {

                    /* converted weight form each item */
                    CatalogDTO.Info catalogDTO = new CatalogDTO.Info();
                    catalogDTO.setValue((merit.getWeight() * answer.getValue()) / 100);
                    catalogDTO.setCode(answer.getCode());
                    catalogDTO.setId(answer.getId());
                    convertedAnswerListByCatalog.add(catalogDTO);

                    /* converted weight form each item by sum of merits weight => item / total items weight */
                    CatalogDTO.Info totalItemWeightConvertedCatalogDTO = new CatalogDTO.Info();
                    totalItemWeightConvertedCatalogDTO.setId(answer.getId());
                    totalItemWeightConvertedCatalogDTO.setCode(answer.getCode());
                    totalItemWeightConvertedCatalogDTO.setValue(catalogDTO.getValue() / sumMeritsWeight);
                    totalItemWeightConvertedByCatalog.add(totalItemWeightConvertedCatalogDTO);

                    /* converted weight form each item by group type weight => (item / total items weight) * group type weight */
                    CatalogDTO.Info groupTypeWightConvertedCatalogDTO = new CatalogDTO.Info();
                    groupTypeWightConvertedCatalogDTO.setId(answer.getId());
                    groupTypeWightConvertedCatalogDTO.setCode(answer.getCode());
                    groupTypeWightConvertedCatalogDTO.setValue(totalItemWeightConvertedCatalogDTO.getValue() * item.getGroupTypeWeight());
                    groupTypeWeightConvertedByCatalog.add(groupTypeWightConvertedCatalogDTO);

                });

                merit.setAnswerInfo(answerListByCatalog);
                merit.setAnswerConvertedInfo(convertedAnswerListByCatalog);
                merit.setTotalItemWeightConvertedByCatalog(totalItemWeightConvertedByCatalog);
                merit.setGroupTypeWeightConvertedByCatalog(groupTypeWeightConvertedByCatalog);

            });
        });
    }

    private void getGroupTypeMeritInfoForCreate(String assessPostCode, List<EvaluationItemDTO.CreateItemInfo> createItemInfoList) {
        List<GroupType> groupType = groupTypeService.getTypeByAssessPostCode(assessPostCode, LEVEL_DEF_GROUP);
        groupType.forEach(gType -> {
            EvaluationItemDTO.CreateItemInfo createItemInfo = new EvaluationItemDTO.CreateItemInfo();
            createItemInfo.setGroupTypeWeight(gType.getWeight());
            createItemInfo.setTypeTitle(gType.getKpiType().getTitle());
            List<EvaluationItemDTO.MeritTupleDTO> meritTupleDtoList = groupTypeMeritService.getAllByGroupType(gType.getId());
            createItemInfo.setMeritTuple(meritTupleDtoList);
            createItemInfoList.add(createItemInfo);

        });
    }

    private void getPostMeritInfoForCreate(String assessPostCode, List<EvaluationItemDTO.CreateItemInfo> createItemInfoList) {
        List<GroupType> groupType = groupTypeService.getTypeByAssessPostCode(assessPostCode, LEVEL_DEF_POST);
        groupType.forEach(gType -> {
            EvaluationItemDTO.CreateItemInfo createItemInfo = new EvaluationItemDTO.CreateItemInfo();
            createItemInfo.setGroupTypeWeight(gType.getWeight());
            createItemInfo.setTypeTitle(gType.getKpiType().getTitle());
            List<EvaluationItemDTO.MeritTupleDTO> meritTupleDTOS = postMeritComponentService.getByPostCode(assessPostCode);
            createItemInfo.setMeritTuple(meritTupleDTOS);
            createItemInfoList.add(createItemInfo);

        });
    }

    @Override
    @Transactional(readOnly = true)
    @PreAuthorize("hasAuthority('R_EVALUATION_ITEM')")
    public List<EvaluationItemDTO.CreateItemInfo> getInfoByEvaluationIdForUpdate(Long id) {

        String assessPostCode = evaluationService.get(id).getAssessPostCode();
        if (assessPostCode.contains("/"))
            assessPostCode = assessPostCode.substring(0, assessPostCode.indexOf("/"));
        List<EvaluationItemDTO.CreateItemInfo> createItemInfoList = new ArrayList<>();
        getGroupTypeMeritInfoForUpdate(id, assessPostCode, createItemInfoList);
        getPostMeritInfoForUpdate(id, createItemInfoList);
        calculateAndSetTotalWeight(createItemInfoList);
        return createItemInfoList;
    }

    private void getGroupTypeMeritInfoForUpdate(Long evaluationId, String assessPostCode, List<EvaluationItemDTO.CreateItemInfo> createItemInfoList) {
        List<GroupType> groupType = groupTypeService.getTypeByAssessPostCode(assessPostCode, LEVEL_DEF_GROUP);
        groupType.forEach(gType -> {
            EvaluationItemDTO.CreateItemInfo createItemInfo = new EvaluationItemDTO.CreateItemInfo();
            createItemInfo.setGroupTypeWeight(gType.getWeight());
            createItemInfo.setTypeTitle(gType.getKpiType().getTitle());
            List<EvaluationItemDTO.MeritTupleDTO> meritTupleDtoList = getAllGroupTypeMeritByEvalId(evaluationId);

            meritTupleDtoList.forEach(meritTupleDTO -> {
                if (Objects.nonNull(meritTupleDTO.getEvaluationItemInstance()) && !meritTupleDTO.getEvaluationItemInstance().isEmpty()) {
                    List<EvaluationItemDTO.InstanceTupleDTO> instanceTupleDTOList = new ArrayList<>();
                    meritTupleDTO.getEvaluationItemInstance().forEach(evaluationItemInstanceTuple ->
                            instanceTupleDTOList.add(evaluationItemInstanceTuple.getInstance())
                    );
                    meritTupleDTO.setInstances(instanceTupleDTOList);
                    meritTupleDTO.setEvaluationItemInstance(null);
                }

            });
            createItemInfo.setMeritTuple(meritTupleDtoList);
            createItemInfoList.add(createItemInfo);

        });
    }

    private void getPostMeritInfoForUpdate(Long evaluationId, List<EvaluationItemDTO.CreateItemInfo> createItemInfoList) {

        List<GroupType> groupType = groupTypeService.getTypeByEvaluationId(evaluationId, LEVEL_DEF_POST);
        groupType.forEach(gType -> {
            EvaluationItemDTO.CreateItemInfo createItemInfo = new EvaluationItemDTO.CreateItemInfo();
            createItemInfo.setGroupTypeWeight(gType.getWeight());
            createItemInfo.setTypeTitle(gType.getKpiType().getTitle());
            List<EvaluationItemDTO.PostMeritTupleDTO> meritTupleDtoList = getAllPostMeritByEvalId(evaluationId);
            List<EvaluationItemDTO.MeritTupleDTO> meritTupleDTOS = mapper.entityToMeritTupleInfoList(meritTupleDtoList);

            meritTupleDTOS.forEach(meritTupleDTO -> {
                if (Objects.nonNull(meritTupleDTO.getEvaluationItemInstance()) && !meritTupleDTO.getEvaluationItemInstance().isEmpty()) {
                    List<EvaluationItemDTO.InstanceTupleDTO> instanceTupleDTOList = new ArrayList<>();
                    meritTupleDTO.getEvaluationItemInstance().forEach(evaluationItemInstanceTuple ->
                            instanceTupleDTOList.add(evaluationItemInstanceTuple.getInstance())
                    );
                    meritTupleDTO.setInstances(instanceTupleDTOList);
                    meritTupleDTO.setEvaluationItemInstance(null);
                }

            });
            createItemInfo.setMeritTuple(meritTupleDTOS);
            createItemInfoList.add(createItemInfo);

        });
    }

}
