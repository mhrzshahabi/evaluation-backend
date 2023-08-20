package com.nicico.evaluation.service;

import com.nicico.copper.common.dto.search.SearchDTO;
import com.nicico.evaluation.common.PageableMapper;
import com.nicico.evaluation.dto.EvaluationItemDTO;
import com.nicico.evaluation.dto.PostMeritComponentDTO;
import com.nicico.evaluation.exception.EvaluationHandleException;
import com.nicico.evaluation.iservice.ICatalogService;
import com.nicico.evaluation.iservice.IGroupPostService;
import com.nicico.evaluation.iservice.IMeritComponentService;
import com.nicico.evaluation.iservice.IPostMeritComponentService;
import com.nicico.evaluation.mapper.PostMeritComponentMapper;
import com.nicico.evaluation.model.PostMeritComponent;
import com.nicico.evaluation.repository.PostMeritComponentRepository;
import com.nicico.evaluation.utility.BaseResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Locale;

@RequiredArgsConstructor
@Service
public class PostMeritComponentService implements IPostMeritComponentService {

    private final PageableMapper pageableMapper;
    private final ICatalogService catalogService;
    private final PostMeritComponentMapper mapper;
    private final IGroupPostService groupPostService;
    private final PostMeritComponentRepository repository;
    private final ResourceBundleMessageSource messageSource;
    private final IMeritComponentService meritComponentService;

    @Override
    @Transactional(readOnly = true)
    @PreAuthorize("hasAuthority('R_POST_MERIT_COMPONENT')")
    public PostMeritComponentDTO.Info get(Long id) {
        PostMeritComponent postMeritComponent = repository.findById(id).orElseThrow(() -> new EvaluationHandleException(EvaluationHandleException.ErrorType.NotFound));
        return mapper.entityToDtoInfo(postMeritComponent);
    }

    @Override
    @Transactional(readOnly = true)
    @PreAuthorize("hasAuthority('R_POST_MERIT_COMPONENT')")
    public List<PostMeritComponentDTO.Info> getAllByPeriodIdIn(Long evaluationPeriodId) {
        List<PostMeritComponent> allByPeriodId = repository.findAllByPeriodIdIn(evaluationPeriodId);
        return mapper.entityToDtoInfoList(allByPeriodId);
    }

    @Override
    @Transactional(readOnly = true)
    @PreAuthorize("hasAuthority('R_POST_MERIT_COMPONENT')")
    public Long getTotalWeight(String groupPostCode) {
        Long revokedMeritId = catalogService.getByCode("Revoked-Merit").getId();
        List<PostMeritComponent> byGroupPostCode = repository.findAllByGroupPostCodeAndStatusCatalogId(groupPostCode, revokedMeritId);
        return byGroupPostCode.stream().map(PostMeritComponent::getWeight).reduce(0L, Long::sum);
    }

    @Override
    @Transactional(readOnly = true)
    @PreAuthorize("hasAuthority('R_POST_MERIT_COMPONENT')")
    public List<EvaluationItemDTO.MeritTupleDTO> getByGroupPostCode(String groupPostCode) {
        List<PostMeritComponent> byGroupPostCode = repository.findAllByGroupPostCode(groupPostCode);
        return mapper.postMeritDtoToEvaluationItemInfoList(byGroupPostCode);
    }

    @Transactional(readOnly = true)
    @PreAuthorize("hasAuthority('R_POST_MERIT_COMPONENT')")
    public List<EvaluationItemDTO.MeritTupleDTO> getByPostCodeAndMeritStatus(String postCode, Long statusCatalogId) {
        List<PostMeritComponent> byPostCode = repository.findAllByPostCode(postCode,statusCatalogId);
        return mapper.postMeritDtoToEvaluationItemInfoList(byPostCode);
    }

    @Override
    @Transactional(readOnly = true)
    @PreAuthorize("hasAuthority('R_POST_MERIT_COMPONENT')")
    public PostMeritComponentDTO.SpecResponse list(int count, int startIndex) {
        Pageable pageable = pageableMapper.toPageable(count, startIndex);
        Page<PostMeritComponent> postMeritComponents = repository.findAll(pageable);
        List<PostMeritComponentDTO.LastActiveMeritInfo> meritComponentInfos = mapper.entityToDtoLastActiveMeritInfoList(postMeritComponents.getContent());

        PostMeritComponentDTO.Response response = new PostMeritComponentDTO.Response();
        PostMeritComponentDTO.SpecResponse specResponse = new PostMeritComponentDTO.SpecResponse();

        if (meritComponentInfos != null) {
            response.setData(meritComponentInfos)
                    .setStartRow(startIndex)
                    .setEndRow(startIndex + count)
                    .setTotalRows((int) postMeritComponents.getTotalElements());
            specResponse.setResponse(response);
        }
        return specResponse;
    }

    @Override
    @Transactional
    @PreAuthorize("hasAuthority('C_POST_MERIT_COMPONENT')")
    public PostMeritComponentDTO.Info create(PostMeritComponentDTO.Create dto) {
        List<Long> meritComponentIds = repository.findAllByGroupPostCode(dto.getGroupPostCode()).stream().map(PostMeritComponent::getMeritComponentId).toList();
        if (meritComponentIds.contains(dto.getMeritComponentId())) {
            final Locale locale = LocaleContextHolder.getLocale();
            throw new EvaluationHandleException(EvaluationHandleException.ErrorType.DuplicateRecord, null, messageSource.getMessage("exception.merit.component.already.added", null, locale));
        } else {
            PostMeritComponent postMeritComponent = mapper.dtoCreateToEntity(dto);
            try {
                PostMeritComponent save = repository.save(postMeritComponent);
                return mapper.entityToDtoInfo(save);
            } catch (Exception exception) {
                throw new EvaluationHandleException(EvaluationHandleException.ErrorType.NotSave);
            }
        }
    }

    @Override
    @PreAuthorize("hasAuthority('C_POST_MERIT_COMPONENT')")
    public BaseResponse batchCreate(PostMeritComponentDTO.BatchCreate dto) {
        BaseResponse response = new BaseResponse();
        try {
            groupPostService.getByCode(dto.getGroupPostCode());
            Long meritComponentId = meritComponentService.getByCode(dto.getMeritComponentCode()).getId();
            PostMeritComponent postMeritComponent = mapper.dtoBatchCreateToEntity(dto);
            postMeritComponent.setMeritComponentId(meritComponentId);
            PostMeritComponent save = repository.save(postMeritComponent);
            PostMeritComponentDTO.Info info = mapper.entityToDtoInfo(save);
            response.setMessage(info.getId().toString());
            response.setStatus(HttpStatus.OK.value());
        } catch (Exception exception) {
            response.setStatus(HttpStatus.FORBIDDEN.value());
            response.setMessage(exception.getMessage());
        }
        return response;
    }

    @Override
    @Transactional
    @PreAuthorize("hasAuthority('U_POST_MERIT_COMPONENT')")
    public PostMeritComponentDTO.Info update(Long id, PostMeritComponentDTO.Update dto) {
        PostMeritComponent postMeritComponent = repository.findById(id).orElseThrow(() -> new EvaluationHandleException(EvaluationHandleException.ErrorType.NotFound));
        mapper.update(postMeritComponent, dto);
        try {
            PostMeritComponent save = repository.save(postMeritComponent);
            return mapper.entityToDtoInfo(save);
        } catch (Exception exception) {
            throw new EvaluationHandleException(EvaluationHandleException.ErrorType.NotEditable);
        }
    }

    @Override
    @Transactional
    @PreAuthorize("hasAuthority('D_POST_MERIT_COMPONENT')")
    public void delete(Long id) {
        PostMeritComponent postMeritComponent = repository.findById(id).orElseThrow(() -> new EvaluationHandleException(EvaluationHandleException.ErrorType.NotFound));
        repository.delete(postMeritComponent);
    }

    @Override
    @Transactional(readOnly = true)
    @PreAuthorize("hasAuthority('R_POST_MERIT_COMPONENT')")
    public SearchDTO.SearchRs<PostMeritComponentDTO.LastActiveMeritInfo> search(SearchDTO.SearchRq request) throws IllegalAccessException, NoSuchFieldException {
        return BaseService.optimizedSearch(repository, mapper::entityToDtoLastActiveMeritInfo, request);
    }

}
