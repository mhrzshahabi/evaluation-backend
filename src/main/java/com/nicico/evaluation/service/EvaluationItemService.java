package com.nicico.evaluation.service;

import com.nicico.copper.common.dto.search.SearchDTO;
import com.nicico.evaluation.common.PageableMapper;
import com.nicico.evaluation.dto.EvaluationItemDTO;
import com.nicico.evaluation.dto.GroupTypeMeritDTO;
import com.nicico.evaluation.dto.PostMeritComponentDTO;
import com.nicico.evaluation.exception.EvaluationHandleException;
import com.nicico.evaluation.iservice.IEvaluationItemService;
import com.nicico.evaluation.iservice.IGroupTypeMeritService;
import com.nicico.evaluation.iservice.IGroupTypeService;
import com.nicico.evaluation.iservice.IPostMeritComponentService;
import com.nicico.evaluation.mapper.EvaluationItemMapper;
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

import static com.nicico.evaluation.utility.EvaluationConstant.LEVEL_DEF_GROUP;
import static com.nicico.evaluation.utility.EvaluationConstant.LEVEL_DEF_POST;

@RequiredArgsConstructor
@Service
public class EvaluationItemService implements IEvaluationItemService {

    private final EvaluationItemMapper mapper;
    private final EvaluationItemRepository repository;
    private final PageableMapper pageableMapper;
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
    @Transactional
    @PreAuthorize("hasAuthority('C_EVALUATION_ITEM')")
    public EvaluationItemDTO.Info create(EvaluationItemDTO.Create dto) {
        EvaluationItem evaluationItem = repository.save(mapper.dtoCreateToEntity(dto));
        return mapper.entityToDtoInfo(evaluationItem);
    }

    @Override
    @Transactional(readOnly = true)
    @PreAuthorize("hasAuthority('R_EVALUATION_ITEM')")
    public List<EvaluationItemDTO.CreateItemInfo> getItemInfoByAssessPostCode(String assessPostCode) {
        List<EvaluationItemDTO.CreateItemInfo> createItemInfoList = new ArrayList<>();
        setInfoForGroupTypeMerit(assessPostCode, createItemInfoList);
        setInfoForPostMerit(assessPostCode, createItemInfoList);
        return createItemInfoList;
    }

    private void setInfoForGroupTypeMerit(String assessPostCode, List<EvaluationItemDTO.CreateItemInfo> createItemInfoList) {
        List<GroupType> groupType = groupTypeService.getTypeByAssessPostCode(assessPostCode, LEVEL_DEF_GROUP);
        groupType.forEach(gType -> {
            EvaluationItemDTO.CreateItemInfo createItemInfo = new EvaluationItemDTO.CreateItemInfo();
            createItemInfo.setGroupTypeWeight(gType.getWeight());
            createItemInfo.setTypeTitle(gType.getKpiType().getTitle());
            List<GroupTypeMeritDTO.Info> groupTypeMerits = groupTypeMeritService.getAllByGroupTypeId(gType.getId());
            List<EvaluationItemDTO.MeritTupleDTO> meritTupleDTOS = mapper.groupTypeMeritDtoToMeritInfoList(groupTypeMerits);
            createItemInfo.setMeritTuple(meritTupleDTOS);
            createItemInfoList.add(createItemInfo);

        });
    }

    private void setInfoForPostMerit(String assessPostCode, List<EvaluationItemDTO.CreateItemInfo> createItemInfoList) {
        List<GroupType> groupType = groupTypeService.getTypeByAssessPostCode(assessPostCode, LEVEL_DEF_POST);
        groupType.forEach(gType -> {
            EvaluationItemDTO.CreateItemInfo createItemInfo = new EvaluationItemDTO.CreateItemInfo();
            createItemInfo.setGroupTypeWeight(gType.getWeight());
            createItemInfo.setTypeTitle(gType.getKpiType().getTitle());
            List<PostMeritComponentDTO.Info> postMerits = postMeritComponentService.getByPostCode(assessPostCode);
            List<EvaluationItemDTO.MeritTupleDTO> meritTupleDTOS = mapper.postMeritDtoToMeritInfoList(postMerits);
            createItemInfo.setMeritTuple(meritTupleDTOS);
            createItemInfoList.add(createItemInfo);

        });
    }

    @Override
    @Transactional
    @PreAuthorize("hasAuthority('U_EVALUATION_ITEM')")
    public EvaluationItemDTO.Info update(Long id, EvaluationItemDTO.Update dto) {
        EvaluationItem evaluationItem = repository.findById(id).orElseThrow(() -> new EvaluationHandleException(EvaluationHandleException.ErrorType.NotFound));
        mapper.update(evaluationItem, dto);
        try {
            EvaluationItem save = repository.save(evaluationItem);
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
}
