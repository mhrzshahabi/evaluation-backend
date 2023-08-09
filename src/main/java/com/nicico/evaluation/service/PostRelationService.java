package com.nicico.evaluation.service;

import com.nicico.copper.common.dto.search.SearchDTO;
import com.nicico.evaluation.common.PageableMapper;
import com.nicico.evaluation.dto.PostRelationDTO;
import com.nicico.evaluation.exception.EvaluationHandleException;
import com.nicico.evaluation.iservice.IPostRelationService;
import com.nicico.evaluation.mapper.PostRelationMapper;
import com.nicico.evaluation.model.PostRelation;
import com.nicico.evaluation.repository.PostRelationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RequiredArgsConstructor
@Service
public class PostRelationService implements IPostRelationService {

    private final PostRelationMapper mapper;
    private final PageableMapper pageableMapper;
    private final PostRelationRepository repository;

    @Override
    @Transactional(readOnly = true)
    @PreAuthorize("hasAuthority('R_POST_RELATION')")
    public PostRelationDTO.Info get(Long id) {
        PostRelation postRelation = repository.findById(id).orElseThrow(() -> new EvaluationHandleException(EvaluationHandleException.ErrorType.NotFound));
        return mapper.entityToDtoInfo(postRelation);
    }

    @Override
    @Transactional(readOnly = true)
    @PreAuthorize("hasAuthority('R_POST_RELATION')")
    public PostRelationDTO.SpecResponse list(int count, int startIndex) {
        Pageable pageable = pageableMapper.toPageable(count, startIndex);
        Page<PostRelation> postRelations = repository.findAll(pageable);
        List<PostRelationDTO.Info> infos = mapper.entityToDtoInfoList(postRelations.getContent());

        PostRelationDTO.Response response = new PostRelationDTO.Response();
        PostRelationDTO.SpecResponse specResponse = new PostRelationDTO.SpecResponse();

        if (infos != null) {
            response.setData(infos)
                    .setStartRow(startIndex)
                    .setEndRow(startIndex + count)
                    .setTotalRows((int) postRelations.getTotalElements());
            specResponse.setResponse(response);
        }
        return specResponse;
    }

    @Override
    @Transactional(readOnly = true)
    @PreAuthorize("hasAuthority('R_POST_RELATION')")
    public SearchDTO.SearchRs<PostRelationDTO.Info> search(SearchDTO.SearchRq request) throws IllegalAccessException, NoSuchFieldException {
        return BaseService.optimizedSearch(repository, mapper::entityToDtoInfo, request);
    }

    @Override
    @Transactional(readOnly = true)
    public SearchDTO.SearchRs<PostRelationDTO.Info> searchForEvaluationPeriod(List<String> postCodes) {
        SearchDTO.SearchRs<PostRelationDTO.Info> searchRs = new SearchDTO.SearchRs<>();
        List<PostRelation> postRelations = repository.findAllByPostCodeIn(postCodes);
        List<PostRelationDTO.Info> infos = mapper.entityToDtoInfoList(postRelations);
        searchRs.setList(infos);
        return searchRs;
    }

}
