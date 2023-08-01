package com.nicico.evaluation.iservice;

import com.nicico.copper.common.dto.search.SearchDTO;
import com.nicico.evaluation.dto.PostDTO;
import com.nicico.evaluation.model.Post;

public interface IPostService {

    PostDTO.Info get(Long id);

    Post getByPostCode(String postCode);

    PostDTO.SpecResponse list(int count, int startIndex);

    SearchDTO.SearchRs<PostDTO.Info> search(SearchDTO.SearchRq request) throws IllegalAccessException, NoSuchFieldException;
}
