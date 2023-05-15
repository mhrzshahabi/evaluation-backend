package com.nicico.evaluation.service;

import com.nicico.copper.common.dto.search.SearchDTO;
import com.nicico.evaluation.common.PageableMapper;
import com.nicico.evaluation.dto.PostDTO;
import com.nicico.evaluation.exception.EvaluationHandleException;
import com.nicico.evaluation.iservice.IPostService;
import com.nicico.evaluation.mapper.PostMapper;
import com.nicico.evaluation.model.Post;
import com.nicico.evaluation.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RequiredArgsConstructor
@Service
public class PostService implements IPostService {

    private final PostMapper mapper;
    private final PostRepository repository;
    private final PageableMapper pageableMapper;

    @Override
    @Transactional(readOnly = true)
    @PreAuthorize("hasAuthority('R_POST')")
    public PostDTO.Info get(Long id) {
        Post post = repository.findById(id).orElseThrow(() -> new EvaluationHandleException(EvaluationHandleException.ErrorType.NotFound));
        return mapper.entityToDtoInfo(post);
    }

    @Override
    @Transactional(readOnly = true)
    @PreAuthorize("hasAuthority('R_POST')")
    public PostDTO.SpecResponse list(int count, int startIndex) {
        Pageable pageable = pageableMapper.toPageable(count, startIndex);
        Page<Post> posts = repository.findAll(pageable);
        List<PostDTO.Info> postInfos = mapper.entityToDtoInfoList(posts.getContent());
        PostDTO.Response response = new PostDTO.Response();
        PostDTO.SpecResponse specResponse = new PostDTO.SpecResponse();

        if (postInfos != null) {
            response.setData(postInfos)
                    .setStartRow(startIndex)
                    .setEndRow(startIndex + count)
                    .setTotalRows((int) posts.getTotalElements());
            specResponse.setResponse(response);
        }
        return specResponse;
    }

    @Override
    @Transactional(readOnly = true)
    @PreAuthorize("hasAuthority('R_POST')")
    public SearchDTO.SearchRs<PostDTO.Info> search(SearchDTO.SearchRq request) throws IllegalAccessException, NoSuchFieldException {
        return BaseService.optimizedSearch(repository, mapper::entityToDtoInfo, request);
    }

}
