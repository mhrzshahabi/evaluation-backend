package com.nicico.evaluation.service;

import com.nicico.copper.common.dto.search.EOperator;
import com.nicico.copper.common.dto.search.SearchDTO;
import com.nicico.evaluation.common.PageableMapper;
import com.nicico.evaluation.dto.GroupTypeByGroupByDTO;
import com.nicico.evaluation.dto.GroupTypeDTO;
import com.nicico.evaluation.exception.EvaluationHandleException;
import com.nicico.evaluation.iservice.IGroupTypeService;
import com.nicico.evaluation.iservice.IKPITypeService;
import com.nicico.evaluation.mapper.GroupTypeMapper;
import com.nicico.evaluation.model.GroupType;
import com.nicico.evaluation.repository.GroupTypeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

import static com.nicico.evaluation.utility.CriteriaUtil.makeCriteria;

@RequiredArgsConstructor
@Service
public class GroupTypeService implements IGroupTypeService {

    private final GroupTypeMapper mapper;
    private final PageableMapper pageableMapper;
    private final GroupTypeRepository repository;
    private final IKPITypeService kpiTypeService;
    private final MessageSource messageSource;

    @Override
    @Transactional(readOnly = true)
    @PreAuthorize("hasAuthority('R_GROUP_TYPE')")
    public GroupTypeDTO.Info get(Long id) {
        GroupType groupType = repository.findById(id).orElseThrow(() -> new EvaluationHandleException(EvaluationHandleException.ErrorType.NotFound));
        return mapper.entityToDtoInfo(groupType);
    }

    @Override
    public GroupTypeDTO.Info getByCode(String code) {
        GroupType groupType = repository.getByCode(code);
        return mapper.entityToDtoInfo(groupType);
    }

    @Override
    public List<GroupTypeDTO.Info> getAllByPeriodId(Long periodId) {
        List<GroupType> groupType = repository.findAllByPeriodId(periodId);
        return mapper.entityToDtoInfoList(groupType);
    }
    
    @Override
    public List<GroupTypeDTO.Info> getAllByPostCodes(List<String> postCodes) {
        List<GroupType> groupType = repository.findAllByPostCodes(postCodes);
        return mapper.entityToDtoInfoList(groupType);
    }

    @Override
    @Transactional(readOnly = true)
    @PreAuthorize("hasAuthority('R_GROUP_TYPE')")
    public List<GroupType> getTypeByAssessPostCode(String assessPostCode, String levelDef) {
        return repository.getTypeByAssessPostCode(assessPostCode, levelDef);
    }


    @Override
    @Transactional(readOnly = true)
    @PreAuthorize("hasAuthority('R_GROUP_TYPE')")
    public GroupTypeDTO.GroupTypeMaxWeight getWeightInfoByGroupId(Long groupId) {
        int kpiSize = kpiTypeService.findAll().size();
        List<GroupType> allByGroupIdAndKpiTypeId = repository.getAllByGroupId(groupId);
        long totalWeightCreated = allByGroupIdAndKpiTypeId.stream().mapToLong(GroupType::getWeight).sum();
        long totalCountOfCreated = allByGroupIdAndKpiTypeId.size();
        long remainCount = kpiSize - totalCountOfCreated;
        GroupTypeDTO.GroupTypeMaxWeight data = new GroupTypeDTO.GroupTypeMaxWeight();
        if (totalWeightCreated != 100 && remainCount != 0)
            data.setMaxWeight(100 - totalWeightCreated - remainCount + 1);
        else if (totalWeightCreated != 100)
            data.setMaxWeight(100 - totalWeightCreated);
        else data.setMaxWeight(0L);
        data.setRemainCount(remainCount);
        return data;
    }

    @Override
    @Transactional(readOnly = true)
    @PreAuthorize("hasAuthority('R_EVALUATION_ITEM')")
    public GroupTypeDTO.SpecResponse list(int count, int startIndex) {
        Pageable pageable = pageableMapper.toPageable(count, startIndex);
        Page<GroupType> groupTypes = repository.findAll(pageable);
        List<GroupTypeDTO.Info> groupTypeInfos = mapper.entityToDtoInfoList(groupTypes.getContent());

        GroupTypeDTO.Response response = new GroupTypeDTO.Response();
        GroupTypeDTO.SpecResponse specResponse = new GroupTypeDTO.SpecResponse();

        if (groupTypeInfos != null) {
            response.setData(groupTypeInfos)
                    .setStartRow(startIndex)
                    .setEndRow(startIndex + count)
                    .setTotalRows((int) groupTypes.getTotalElements());
            specResponse.setResponse(response);
        }
        return specResponse;
    }

    @Override
    @Transactional
    @PreAuthorize("hasAuthority('C_GROUP_TYPE')")
    public GroupTypeDTO.Info create(GroupTypeDTO.Create dto) {
        GroupType groupType = mapper.dtoCreateToEntity(dto);
        GroupType allByGroupIdAndKpiTypeId = getAllByGroupIdAndKpiTypeId(groupType);
        if (Objects.nonNull(allByGroupIdAndKpiTypeId))
            throw new EvaluationHandleException(EvaluationHandleException.ErrorType.DuplicateRecord, null, messageSource.getMessage("exception.duplicate.information", null, LocaleContextHolder.getLocale()));
        try {
            GroupType save = repository.save(groupType);
            return mapper.entityToDtoInfo(save);
        } catch (Exception exception) {
            throw new EvaluationHandleException(EvaluationHandleException.ErrorType.NotSave);
        }
    }

    @Override
    @Transactional
    @PreAuthorize("hasAuthority('R_GROUP_TYPE')")
    public GroupType getAllByGroupIdAndKpiTypeId(GroupType groupType) {
        return repository.getByGroupIdAndKpiTypeId(groupType.getGroupId(), groupType.getKpiTypeId());
    }

    @Override
    @Transactional
    @PreAuthorize("hasAuthority('U_GROUP_TYPE')")
    public GroupTypeDTO.Info update(Long id, GroupTypeDTO.Update dto) {
        GroupType groupType = repository.findById(id).orElseThrow(() -> new EvaluationHandleException(EvaluationHandleException.ErrorType.NotFound));
        mapper.update(groupType, dto);
        try {
            GroupType save = repository.save(groupType);
            return mapper.entityToDtoInfo(save);
        } catch (Exception exception) {
            throw new EvaluationHandleException(EvaluationHandleException.ErrorType.NotEditable);
        }
    }

    @Override
    @Transactional
    @PreAuthorize("hasAuthority('D_GROUP_TYPE')")
    public void delete(Long id) {
        GroupType groupType = repository.findById(id).orElseThrow(() -> new EvaluationHandleException(EvaluationHandleException.ErrorType.NotFound));
        repository.delete(groupType);
    }

    @Override
    @Transactional(readOnly = true)
    @PreAuthorize("hasAuthority('R_GROUP_TYPE')")
    public SearchDTO.SearchRs<GroupTypeDTO.Info> search(SearchDTO.SearchRq request) throws IllegalAccessException, NoSuchFieldException {
        SearchDTO.SearchRs<GroupTypeDTO.Info> infoSearchRs = BaseService.optimizedSearch(repository, mapper::entityToDtoInfo, request);
        List<GroupTypeDTO.Info> info = infoSearchRs.getList().stream().toList();
        int kpiSize = kpiTypeService.findAll().size();
        Map<Long, List<GroupTypeDTO.Info>> groupTypeMap = info.stream().collect(Collectors.groupingBy(GroupTypeDTO::getGroupId));
        List<GroupTypeDTO.Info> data = new ArrayList<>();
        groupTypeMap.forEach((groupId, gType) -> {
            long totalWeight = gType.stream().mapToLong(GroupTypeDTO::getWeight).sum();
            gType.forEach(groupType -> {
                groupType.setTotalWeight(totalWeight);
                groupType.setHasAllKpiType(gType.size() == kpiSize ? Boolean.TRUE : Boolean.FALSE);
                data.add(groupType);
            });
        });
        infoSearchRs.setList(data);
        return infoSearchRs;
    }

    @Override
    @Transactional(readOnly = true)
    @PreAuthorize("hasAuthority('R_GROUP_TYPE')")
    public SearchDTO.SearchRs<GroupTypeByGroupByDTO.Info> searchByGroupBy(SearchDTO.SearchRq request) throws IllegalAccessException, NoSuchFieldException {

        if (Objects.nonNull(request.getCriteria()) && Objects.nonNull(request.getCriteria().getCriteria())) {
            Optional<SearchDTO.CriteriaRq> criteriaRqs = request.getCriteria().getCriteria().stream().filter(q -> q.getFieldName().equals("groupName")).findFirst();
            if (criteriaRqs.isPresent()) {
                SearchDTO.CriteriaRq criteriaRq = makeCriteria("group.title", criteriaRqs.get().getValue(), EOperator.contains, new ArrayList<>());
                request.setCriteria(criteriaRq);
            }
        }
        SearchDTO.SearchRs<GroupTypeDTO.Info> infoSearchRs = BaseService.optimizedSearch(repository, mapper::entityToDtoInfo, request);
        List<GroupTypeDTO.Info> info = infoSearchRs.getList().stream().toList();
        int kpiSize = kpiTypeService.findAll().size();
        Map<Long, List<GroupTypeDTO.Info>> groupTypeMap = info.stream().collect(Collectors.groupingBy(GroupTypeDTO::getGroupId));
        SearchDTO.SearchRs<GroupTypeByGroupByDTO.Info> searchRs = new SearchDTO.SearchRs<>();
        List<GroupTypeByGroupByDTO.Info> groupByInfoList = new ArrayList<>();
        groupTypeMap.forEach((groupId, gType) -> {
            List<GroupType> existGType = repository.getAllByGroupId(groupId);
            List<GroupTypeDTO.Info> gTypeList = mapper.entityToDtoInfoList(existGType);
            groupTypeMap.put(groupId, gTypeList);

            GroupTypeByGroupByDTO.Info groupByInfo = new GroupTypeByGroupByDTO.Info();
            groupByInfo.setGroupName(gType.get(0).getGroup().getTitle());
            List<GroupTypeDTO.Info> data = new ArrayList<>();
            long totalWeight = existGType.stream().mapToLong(GroupType::getWeight).sum();
            gTypeList.forEach(groupType -> {
                groupType.setTotalWeight(totalWeight);
                groupType.setHasAllKpiType(existGType.size() == kpiSize ? Boolean.TRUE : Boolean.FALSE);
                data.add(groupType);
            });
            groupByInfo.setTotalWeight(totalWeight);
            groupByInfo.setDetailInfos(data);
            groupByInfoList.add(groupByInfo);
        });
        searchRs.setList(groupByInfoList);
        searchRs.setTotalCount((long) groupByInfoList.size());
        return searchRs;
    }
}
