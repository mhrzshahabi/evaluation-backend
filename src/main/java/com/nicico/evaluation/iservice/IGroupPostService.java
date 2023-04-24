package com.nicico.evaluation.iservice;

import com.nicico.copper.common.dto.search.SearchDTO;
import com.nicico.evaluation.dto.GroupPostDTO;

public interface IGroupPostService {

    GroupPostDTO.SpecResponse list(int count, int startIndex);

    GroupPostDTO.Info get(Long id);

    SearchDTO.SearchRs<GroupPostDTO.Info> search(SearchDTO.SearchRq request) throws IllegalAccessException, NoSuchFieldException;

}

