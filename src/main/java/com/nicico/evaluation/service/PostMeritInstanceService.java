package com.nicico.evaluation.service;

import com.nicico.copper.common.dto.search.SearchDTO;
import com.nicico.evaluation.common.PageableMapper;
import com.nicico.evaluation.dto.PostMeritComponentDTO;
import com.nicico.evaluation.dto.PostMeritInstanceDTO;
import com.nicico.evaluation.exception.EvaluationHandleException;
import com.nicico.evaluation.iservice.IInstanceService;
import com.nicico.evaluation.iservice.IPostMeritComponentService;
import com.nicico.evaluation.iservice.IPostMeritInstanceService;
import com.nicico.evaluation.mapper.PostMeritInstanceMapper;
import com.nicico.evaluation.model.PostMeritInstance;
import com.nicico.evaluation.repository.PostMeritInstanceRepository;
import com.nicico.evaluation.utility.BaseResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class PostMeritInstanceService implements IPostMeritInstanceService {

    private final PageableMapper pageableMapper;
    private final PostMeritInstanceMapper mapper;
    private final IInstanceService instanceService;
    private final PostMeritInstanceRepository repository;
    private final IPostMeritComponentService postMeritComponentService;

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
    public Set<PostMeritInstanceDTO.Info> createAll(Set<PostMeritInstanceDTO.Create> requests) {
        return requests.stream().map(this::create).collect(Collectors.toSet());
    }

    @Override
    @Transactional
    @PreAuthorize("hasAuthority('C_POST_MERIT_INSTANCE')")
    public PostMeritInstanceDTO.Info create(PostMeritInstanceDTO.Create dto) {
        if (Objects.nonNull(dto.getId())) return null;
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
    @PreAuthorize("hasAuthority('R_POST_MERIT_INSTANCE')")
    public List<PostMeritInstanceDTO.Info> findAllByPostMeritComponentId(Long postMeritId) {

        Set<PostMeritInstance> allByPostMeritComponentId = repository.findAllByPostMeritComponentId(postMeritId);
        List<PostMeritInstance> postMeritInstances = allByPostMeritComponentId.stream().toList();
        return mapper.entityToDtoInfoList(postMeritInstances);
    }

    @Override
    @Transactional
    @PreAuthorize("hasAuthority('C_POST_MERIT_INSTANCE')")
    public Set<PostMeritInstanceDTO.Info> create(PostMeritInstanceDTO.CreateAll dto) {
        Set<PostMeritInstanceDTO.Create> createDtoList = new HashSet<>();
        dto.getInstanceIds().forEach(instanceId -> {
            PostMeritInstanceDTO.Create createDto = new PostMeritInstanceDTO.Create();
            createDto.setPostMeritComponentId(dto.getPostMeritComponentId());
            createDto.setInstanceId(instanceId);
            createDtoList.add(createDto);
        });
        Set<PostMeritInstance> allByPostMeritComponentId = repository.findAllByPostMeritComponentId(dto.getPostMeritComponentId());
        Set<PostMeritInstanceDTO.Create> createList = new HashSet<>(mapper.entityToCreateDtoList(allByPostMeritComponentId));
        createList.addAll(createDtoList);
        return this.createAll(createList);
    }

    @Override
    @PreAuthorize("hasAuthority('C_POST_MERIT_INSTANCE')")
    public BaseResponse batchCreate(PostMeritInstanceDTO.BatchCreate dto) {
        BaseResponse response = new BaseResponse();
        try {
            Long instanceId = instanceService.getByCode(dto.getInstanceCode()).getId();
            PostMeritComponentDTO.BatchCreate batchCreate = mapper.dtoBatchCreateToDtoComponentBatchCreate(dto);
            BaseResponse postMeritComponentResponse = postMeritComponentService.batchCreate(batchCreate);
            if (postMeritComponentResponse.getStatus() == HttpStatus.OK.value()) {
                PostMeritInstanceDTO.Create create = new PostMeritInstanceDTO.Create();
                create.setPostMeritComponentId(Long.valueOf(postMeritComponentResponse.getMessage()));
                create.setInstanceId(instanceId);
                create(create);
                response.setStatus(HttpStatus.OK.value());
            } else {
                response.setStatus(HttpStatus.FORBIDDEN.value());
                response.setMessage(postMeritComponentResponse.getMessage());
            }
        } catch (Exception exception) {
            response.setStatus(HttpStatus.FORBIDDEN.value());
            response.setMessage(exception.getMessage());
        }
        return response;
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
