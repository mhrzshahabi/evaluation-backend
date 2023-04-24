package com.nicico.evaluation.iservice;

import com.nicico.copper.common.dto.search.SearchDTO;
import com.nicico.evaluation.dto.PostMeritComponentDTO;

public interface IPostMeritComponentService {

    PostMeritComponentDTO.Info get(Long id);

    PostMeritComponentDTO.SpecResponse list(int count, int startIndex);

    PostMeritComponentDTO.Info create(PostMeritComponentDTO.Create dto);

    PostMeritComponentDTO.Info update(PostMeritComponentDTO.Update dto);

    void delete(Long id);

    SearchDTO.SearchRs<PostMeritComponentDTO.Info> search(SearchDTO.SearchRq request) throws IllegalAccessException, NoSuchFieldException;

}
