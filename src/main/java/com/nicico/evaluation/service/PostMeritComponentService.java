package com.nicico.evaluation.service;

import com.nicico.copper.common.dto.search.SearchDTO;
import com.nicico.evaluation.common.PageableMapper;
import com.nicico.evaluation.dto.PostMeritComponentDTO;
import com.nicico.evaluation.exception.EvaluationHandleException;
import com.nicico.evaluation.iservice.IPostMeritComponentService;
import com.nicico.evaluation.mapper.PostMeritComponentMapper;
import com.nicico.evaluation.model.PostMeritComponent;
import com.nicico.evaluation.repository.PostMeritComponentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RequiredArgsConstructor
@Service
public class PostMeritComponentService implements IPostMeritComponentService {

    private final PostMeritComponentMapper mapper;
    private final PageableMapper pageableMapper;
    private final PostMeritComponentRepository repository;

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
    public PostMeritComponentDTO.SpecResponse list(int count, int startIndex) {
        Pageable pageable = pageableMapper.toPageable(count, startIndex);
        Page<PostMeritComponent> postMeritComponents = repository.findAll(pageable);
        List<PostMeritComponentDTO.Info> meritComponentInfos = mapper.entityToDtoInfoList(postMeritComponents.getContent());

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
        PostMeritComponent postMeritComponent = mapper.dtoCreateToEntity(dto);
        try {
            PostMeritComponent save = repository.save(postMeritComponent);
            return mapper.entityToDtoInfo(save);
        } catch (Exception exception) {
            throw new EvaluationHandleException(EvaluationHandleException.ErrorType.NotSave);
        }
    }

    @Override
    @Transactional
    @PreAuthorize("hasAuthority('U_POST_MERIT_COMPONENT')")
    public PostMeritComponentDTO.Info update(PostMeritComponentDTO.Update dto) {
        PostMeritComponent postMeritComponent = repository.findById(dto.getId()).orElseThrow(() -> new EvaluationHandleException(EvaluationHandleException.ErrorType.NotFound));
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
    public SearchDTO.SearchRs<PostMeritComponentDTO.Info> search(SearchDTO.SearchRq request) throws IllegalAccessException, NoSuchFieldException {
        return BaseService.optimizedSearch(repository, mapper::entityToDtoInfo, request);
    }

}