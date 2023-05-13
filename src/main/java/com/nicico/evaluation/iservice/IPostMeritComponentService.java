package com.nicico.evaluation.iservice;

import com.nicico.copper.common.dto.search.SearchDTO;
import com.nicico.evaluation.dto.PostMeritComponentDTO;
import com.nicico.evaluation.utility.BaseResponse;

import java.util.List;

public interface IPostMeritComponentService {

    PostMeritComponentDTO.Info get(Long id);

    List<PostMeritComponentDTO.Info> getByPostCode(String groupPostCode);

    PostMeritComponentDTO.SpecResponse list(int count, int startIndex);

    PostMeritComponentDTO.Info create(PostMeritComponentDTO.Create dto);

    BaseResponse batchCreate(PostMeritComponentDTO.BatchCreate dto);

    PostMeritComponentDTO.Info update(Long id, PostMeritComponentDTO.Update dto);

    void delete(Long id);

    SearchDTO.SearchRs<PostMeritComponentDTO.Info> search(SearchDTO.SearchRq request) throws IllegalAccessException, NoSuchFieldException;

}
