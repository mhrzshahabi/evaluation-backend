package com.nicico.evaluation.service;

import com.nicico.copper.common.dto.search.SearchDTO;
import com.nicico.evaluation.common.PageableMapper;
import com.nicico.evaluation.dto.PostMeritInstanceDTO;
import com.nicico.evaluation.exception.EvaluationHandleException;
import com.nicico.evaluation.iservice.IPostMeritInstanceService;
import com.nicico.evaluation.mapper.PostMeritInstanceMapper;
import com.nicico.evaluation.model.PostMeritInstance;
import com.nicico.evaluation.repository.PostMeritInstanceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Service
public class PostMeritInstanceService implements IPostMeritInstanceService {

    private final PostMeritInstanceMapper mapper;
    private final PageableMapper pageableMapper;
    private final PostMeritInstanceRepository repository;

    @Override
    @Transactional(readOnly = true)
    @PreAuthorize("hasAuthority('R_POST_MERIT_INSTANCE')")
    public PostMeritInstanceDTO.Info get(Long id) {
        PostMeritInstance postMeritInstance = repository.findById(id).orElseThrow(() -> new EvaluationHandleException(EvaluationHandleException.ErrorType.NotFound));
        return mapper.entityToDtoInfo(postMeritInstance);
    }

    @Override
    @Transactional(readOnly = true)
    @PreAuthorize("hasAuthority('R_POST_MERIT_INSTANCE')")
    public List<PostMeritInstanceDTO.Info> getAllByPostMeritComponentId(Long id) {
        List<PostMeritInstance> allByPostMeritComponentId = repository.findAllByPostMeritComponentId(id);
        return mapper.entityToDtoInfoList(allByPostMeritComponentId);
    }

    @Override
    @Transactional(readOnly = true)
    @PreAuthorize("hasAuthority('R_POST_MERIT_INSTANCE')")
    public PostMeritInstanceDTO.SpecResponse list(int count, int startIndex) {
        Pageable pageable = pageableMapper.toPageable(count, startIndex);
        Page<PostMeritInstance> postMeritInstance = repository.findAll(pageable);
        List<PostMeritInstanceDTO.Info> postMeritInstanceInfos = mapper.entityToDtoInfoList(postMeritInstance.getContent());

        PostMeritInstanceDTO.Response response = new PostMeritInstanceDTO.Response();
        PostMeritInstanceDTO.SpecResponse specResponse = new PostMeritInstanceDTO.SpecResponse();

        if (postMeritInstanceInfos != null) {
            response.setData(postMeritInstanceInfos)
                    .setStartRow(startIndex)
                    .setEndRow(startIndex + count)
                    .setTotalRows((int) postMeritInstance.getTotalElements());
            specResponse.setResponse(response);
        }
        return specResponse;
    }

    @Override
    @Transactional
    @PreAuthorize("hasAuthority('C_POST_MERIT_INSTANCE')")
    public List<PostMeritInstanceDTO.Info> createAll(List<PostMeritInstanceDTO.Create> requests) {
        return requests.stream().map(this::create).toList();
    }

    @Override
    @Transactional
    @PreAuthorize("hasAuthority('C_POST_MERIT_INSTANCE')")
    public PostMeritInstanceDTO.Info create(PostMeritInstanceDTO.Create dto) {
        PostMeritInstance postMeritInstance = mapper.dtoCreateToEntity(dto);
        try {
            PostMeritInstance save = repository.save(postMeritInstance);
            return mapper.entityToDtoInfo(save);
        } catch (Exception exception) {
            throw new EvaluationHandleException(EvaluationHandleException.ErrorType.NotSave);
        }
    }

    @Override
    @Transactional
    @PreAuthorize("hasAuthority('C_POST_MERIT_INSTANCE')")
    public List<PostMeritInstanceDTO.Info> create(PostMeritInstanceDTO.CreateAll dto) {
        List<PostMeritInstanceDTO.Create> createDtoList = new ArrayList<>();
        dto.getInstanceIds().forEach(instanceId -> {
            PostMeritInstanceDTO.Create createDto = new PostMeritInstanceDTO.Create();
            createDto.setPostMeritComponentId(dto.getPostMeritComponentId());
            createDto.setInstanceId(instanceId);
            createDtoList.add(createDto);
        });
        return this.createAll(createDtoList);
    }

    @Override
    @Transactional
    @PreAuthorize("hasAuthority('D_POST_MERIT_INSTANCE')")
    public void delete(Long id) {
        PostMeritInstance postMeritInstance = repository.findById(id).orElseThrow(() -> new EvaluationHandleException(EvaluationHandleException.ErrorType.NotFound));
        repository.delete(postMeritInstance);
    }

    @Override
    @Transactional(readOnly = true)
    @PreAuthorize("hasAuthority('R_POST_MERIT_INSTANCE')")
    public SearchDTO.SearchRs<PostMeritInstanceDTO.Info> search(SearchDTO.SearchRq request) throws IllegalAccessException, NoSuchFieldException {
        return BaseService.optimizedSearch(repository, mapper::entityToDtoInfo, request);
    }

}
