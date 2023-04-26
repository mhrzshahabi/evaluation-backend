package com.nicico.evaluation.iservice;

import com.nicico.copper.common.dto.search.SearchDTO;
import com.nicico.evaluation.dto.PostMeritInstanceDTO;

import java.util.List;

public interface IPostMeritInstanceService {

    PostMeritInstanceDTO.Info get(Long id);

    PostMeritInstanceDTO.SpecResponse list(int count, int startIndex);

    List<PostMeritInstanceDTO.Info> createAll(List<PostMeritInstanceDTO.Create> requests);

    PostMeritInstanceDTO.Info create(PostMeritInstanceDTO.Create dto);

    List<PostMeritInstanceDTO.Info> create(PostMeritInstanceDTO.CreateAll dto);

    void delete(Long id);

    SearchDTO.SearchRs<PostMeritInstanceDTO.Info> search(SearchDTO.SearchRq request) throws IllegalAccessException, NoSuchFieldException;

}