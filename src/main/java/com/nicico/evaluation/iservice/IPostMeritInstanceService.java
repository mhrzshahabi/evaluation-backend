package com.nicico.evaluation.iservice;

import com.nicico.copper.common.dto.search.SearchDTO;
import com.nicico.evaluation.dto.PostMeritInstanceDTO;
import com.nicico.evaluation.utility.BaseResponse;

import java.util.List;
import java.util.Set;

public interface IPostMeritInstanceService {

    PostMeritInstanceDTO.Info get(Long id);


    PostMeritInstanceDTO.SpecResponse list(int count, int startIndex);

    Set<PostMeritInstanceDTO.Info> createAll(Set<PostMeritInstanceDTO.Create> requests);

    PostMeritInstanceDTO.Info create(PostMeritInstanceDTO.Create dto);

    List<PostMeritInstanceDTO.Info> findAllByPostMeritComponentId(Long postMeritId);

    Set<PostMeritInstanceDTO.Info> create(PostMeritInstanceDTO.CreateAll dto);

    BaseResponse batchCreate(PostMeritInstanceDTO.BatchCreate dto);

    void delete(Long id);

    SearchDTO.SearchRs<PostMeritInstanceDTO.Info> search(SearchDTO.SearchRq request) throws IllegalAccessException, NoSuchFieldException;

}
