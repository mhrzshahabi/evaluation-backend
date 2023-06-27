package com.nicico.evaluation.iservice;

import com.nicico.copper.common.dto.search.SearchDTO;
import com.nicico.evaluation.dto.PostRelationDTO;

import java.util.List;

public interface IPostRelationService {

    PostRelationDTO.Info get(Long id);

    PostRelationDTO.SpecResponse list(int count, int startIndex);

    SearchDTO.SearchRs<PostRelationDTO.Info> search(SearchDTO.SearchRq request) throws IllegalAccessException, NoSuchFieldException;
}
