package com.nicico.evaluation.service;

import com.nicico.copper.common.dto.search.SearchDTO;
import com.nicico.evaluation.common.PageableMapper;
import com.nicico.evaluation.dto.EvaluationDTO;
import com.nicico.evaluation.dto.EvaluationItemDTO;
import com.nicico.evaluation.exception.EvaluationHandleException;
import com.nicico.evaluation.iservice.*;
import com.nicico.evaluation.mapper.EvaluationItemMapper;
import com.nicico.evaluation.mapper.EvaluationMapper;
import com.nicico.evaluation.model.Evaluation;
import com.nicico.evaluation.model.EvaluationItem;
import com.nicico.evaluation.model.GroupType;
import com.nicico.evaluation.repository.EvaluationItemRepository;
import com.nicico.evaluation.repository.EvaluationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

import static com.nicico.evaluation.utility.EvaluationConstant.LEVEL_DEF_GROUP;
import static com.nicico.evaluation.utility.EvaluationConstant.LEVEL_DEF_POST;

@RequiredArgsConstructor
@Service
public class EvaluationItemService implements IEvaluationItemService {

    private final EvaluationItemMapper mapper;
    private final EvaluationItemRepository repository;
    private final EvaluationRepository evaluationRepository;
    private final EvaluationMapper evaluationMapper;
    private final PageableMapper pageableMapper;
    private final IEvaluationService evaluationService;
    private final IGroupTypeService groupTypeService;
    private final IGroupTypeMeritService groupTypeMeritService;
    private final IPostMeritComponentService postMeritComponentService;


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
            Evaluation evaluation = evaluationRepository.findById(dto.getEvaluationId()).orElseThrow(() -> new EvaluationHandleException(EvaluationHandleException.ErrorType.NotFound));
            evaluation.setAverageScore(dto.getAverageScore());
            EvaluationDTO.Update evaluationDTO = evaluationMapper.entityToUpdateDto(evaluation);
            evaluationService.update(dto.getEvaluationId(), evaluationDTO);
            return mapper.entityToDtoInfo(evaluationItem);
        } catch (Exception exception) {
            throw new EvaluationHandleException(EvaluationHandleException.ErrorType.NotSave);
        }
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
            Evaluation evaluation = evaluationRepository.findById(dto.getEvaluationId()).orElseThrow(() -> new EvaluationHandleException(EvaluationHandleException.ErrorType.NotFound));
            evaluation.setAverageScore(dto.getAverageScore());
            EvaluationDTO.Update evaluationDTO = evaluationMapper.entityToUpdateDto(evaluation);
            evaluationService.update(dto.getEvaluationId(), evaluationDTO);
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
        return createItemInfoList;
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
    public List<EvaluationItemDTO.CreateItemInfo> getInfoByEvaluationIdForUpdate(Long id) {

        String assessPostCode = evaluationService.get(id).getAssessPostCode();
        if (assessPostCode.contains("/"))
            assessPostCode = assessPostCode.substring(0, assessPostCode.indexOf("/"));
        List<EvaluationItemDTO.CreateItemInfo> createItemInfoList = new ArrayList<>();
        getGroupTypeMeritInfoForUpdate(id, assessPostCode, createItemInfoList);
        getPostMeritInfoForUpdate(id, createItemInfoList);
        return createItemInfoList;
    }

    private void getGroupTypeMeritInfoForUpdate(Long evaluationId, String assessPostCode, List<EvaluationItemDTO.CreateItemInfo> createItemInfoList) {
        List<GroupType> groupType = groupTypeService.getTypeByAssessPostCode(assessPostCode, LEVEL_DEF_GROUP);
        groupType.forEach(gType -> {
            EvaluationItemDTO.CreateItemInfo createItemInfo = new EvaluationItemDTO.CreateItemInfo();
            createItemInfo.setGroupTypeWeight(gType.getWeight());
            createItemInfo.setTypeTitle(gType.getKpiType().getTitle());
            List<EvaluationItemDTO.MeritTupleDTO> meritTupleDtoList = getAllGroupTypeMeritByEvalId(evaluationId);
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
            createItemInfo.setMeritTuple(meritTupleDTOS);
            createItemInfoList.add(createItemInfo);

        });
    }

}
