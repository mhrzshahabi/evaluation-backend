package com.nicico.evaluation.service;

import com.nicico.copper.common.dto.search.EOperator;
import com.nicico.copper.common.dto.search.SearchDTO;
import com.nicico.evaluation.common.PageableMapper;
import com.nicico.evaluation.dto.FilterDTO;
import com.nicico.evaluation.dto.InstanceDTO;
import com.nicico.evaluation.dto.PostMeritInstanceDTO;
import com.nicico.evaluation.exception.EvaluationHandleException;
import com.nicico.evaluation.iservice.IInstanceService;
import com.nicico.evaluation.iservice.IPostMeritInstanceService;
import com.nicico.evaluation.mapper.InstanceMapper;
import com.nicico.evaluation.model.Instance;
import com.nicico.evaluation.repository.InstanceRepository;
import com.nicico.evaluation.utility.BaseResponse;
import com.nicico.evaluation.utility.ExcelGenerator;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Service
public class InstanceService implements IInstanceService {

    private final InstanceMapper mapper;
    private final InstanceRepository repository;
    private final PageableMapper pageableMapper;
    private IPostMeritInstanceService postMeritInstanceService;
    private final ResourceBundleMessageSource messageSource;

    @Autowired
    public void setPostMeritInstanceService(@Lazy IPostMeritInstanceService postMeritInstanceService) {
        this.postMeritInstanceService = postMeritInstanceService;
    }

    @Override
    public ExcelGenerator.ExcelDownload downloadExcel(List<FilterDTO> criteria) throws NoSuchFieldException, IllegalAccessException {
        byte[] body = BaseService.exportExcel(repository, mapper::entityToDtoExcel, criteria, null, "گزارش لیست مصداق ها");
        return new ExcelGenerator.ExcelDownload(body);
    }

    @Override
    @Transactional(readOnly = true)
    @PreAuthorize("hasAuthority('R_INSTANCE')")
    public InstanceDTO.SpecResponse list(int count, int startIndex) {
        Pageable pageable = pageableMapper.toPageable(count, startIndex);
        Page<Instance> instances = repository.findAll(pageable);
        List<InstanceDTO.Info> instanceInfos = mapper.entityToDtoInfoList(instances.getContent());

        InstanceDTO.Response response = new InstanceDTO.Response();
        InstanceDTO.SpecResponse specResponse = new InstanceDTO.SpecResponse();

        if (instanceInfos != null) {
            response.setData(instanceInfos)
                    .setStartRow(startIndex)
                    .setEndRow(startIndex + count)
                    .setTotalRows((int) instances.getTotalElements());
            specResponse.setResponse(response);
        }
        return specResponse;
    }

    @Override
    @Transactional(readOnly = true)
    @PreAuthorize("hasAuthority('R_INSTANCE')")
    public InstanceDTO.Info get(Long id) {
        Instance instance = repository.findById(id).orElseThrow(() -> new EvaluationHandleException(EvaluationHandleException.ErrorType.NotFound));
        return mapper.entityToDtoInfo(instance);
    }

    @Override
    @Transactional
    @PreAuthorize("hasAuthority('C_INSTANCE')")
    public InstanceDTO.Info create(InstanceDTO.Create dto) {
        Instance instance = mapper.dtoCreateToEntity(dto);
        instance = repository.save(instance);
        return mapper.entityToDtoInfo(instance);
    }

    @Override
    public BaseResponse batchCreate(InstanceDTO.Create dto) {
        BaseResponse response = new BaseResponse();
        try {
            create(dto);
            response.setStatus(HttpStatus.OK.value());
        } catch (Exception exception) {
            response.setStatus(HttpStatus.FORBIDDEN.value());
            if (exception.getMessage().contains("UC_TBL_INSTANCE_CODE"))
                response.setMessage(messageSource.getMessage("exception.duplicated.instance", new Object[]{dto.getCode()}, LocaleContextHolder.getLocale()));
            else
                response.setMessage(exception.getMessage());
        }
        return response;
    }

    @Override
    @Transactional
    @PreAuthorize("hasAuthority('U_INSTANCE')")
    public InstanceDTO.Info update(Long id, InstanceDTO.Update dto) {
        Instance instance = repository.findById(id).orElseThrow(() -> new EvaluationHandleException(EvaluationHandleException.ErrorType.NotFound));
        mapper.update(instance, dto);
        Instance save = repository.save(instance);
        return mapper.entityToDtoInfo(save);
    }

    @Override
    @Transactional
    @PreAuthorize("hasAuthority('D_INSTANCE')")
    public void delete(Long id) {
        Instance instance = repository.findById(id).orElseThrow(() -> new EvaluationHandleException(EvaluationHandleException.ErrorType.NotFound));
        repository.delete(instance);
    }

    @Override
    @Transactional(readOnly = true)
    @PreAuthorize("hasAuthority('R_INSTANCE')")
    public SearchDTO.SearchRs<InstanceDTO.Info> search(SearchDTO.SearchRq request) throws IllegalAccessException, NoSuchFieldException {
        return BaseService.optimizedSearch(repository, mapper::entityToDtoInfo, request);
    }

    @Override
    @Transactional(readOnly = true)
    @PreAuthorize("hasAuthority('R_INSTANCE')")
    public SearchDTO.SearchRs<InstanceDTO.Info> searchByPostMeritId(SearchDTO.SearchRq request, Long postMeritId) throws IllegalAccessException, NoSuchFieldException {

        List<Long> instanceIds = postMeritInstanceService.findAllByPostMeritComponentId(postMeritId).stream()
                .map(PostMeritInstanceDTO.Info::getInstance).map(PostMeritInstanceDTO.InstanceTupleDTO::getId).toList();

        final List<SearchDTO.CriteriaRq> criteriaRqList = new ArrayList<>();
        final SearchDTO.CriteriaRq idCriteriaRq = new SearchDTO.CriteriaRq()
                .setOperator(EOperator.notInSet)
                .setFieldName("id")
                .setValue(instanceIds);

        criteriaRqList.add(idCriteriaRq);
        criteriaRqList.add(request.getCriteria());

        final SearchDTO.CriteriaRq criteriaRq = new SearchDTO.CriteriaRq()
                .setOperator(EOperator.and)
                .setCriteria(criteriaRqList);
        request.setCriteria(criteriaRq);

        return BaseService.optimizedSearch(repository, mapper::entityToDtoInfo, request);
    }


    @Override
    public InstanceDTO.Info getByCode(String code) {
        Instance instance = repository.findFirstByCode(code).orElseThrow(() ->
                new EvaluationHandleException(EvaluationHandleException.ErrorType.NotFound, "Instance",
                        messageSource.getMessage("exception.not.exist.instance", new Object[]{code}, LocaleContextHolder.getLocale())));
        return mapper.entityToDtoInfo(instance);
    }


}
