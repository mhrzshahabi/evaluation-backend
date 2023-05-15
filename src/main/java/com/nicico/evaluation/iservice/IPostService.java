package com.nicico.evaluation.iservice;

import com.nicico.copper.common.dto.search.SearchDTO;
import com.nicico.evaluation.dto.PostDTO;

public interface IPostService {

    PostDTO.Info get(Long id);

    PostDTO.SpecResponse list(int count, int startIndex);

    SearchDTO.SearchRs<PostDTO.Info> search(SearchDTO.SearchRq request) throws IllegalAccessException, NoSuchFieldException;

}
