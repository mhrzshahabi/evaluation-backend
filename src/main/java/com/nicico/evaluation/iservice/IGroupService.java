package com.nicico.evaluation.iservice;

import com.nicico.copper.common.domain.criteria.NICICOCriteria;
import com.nicico.copper.common.dto.grid.TotalResponse;
import com.nicico.copper.common.dto.search.SearchDTO;
import com.nicico.evaluation.dto.GroupDTO;

public interface IGroupService {

    GroupDTO.SpecResponse list(int count, int startIndex);

    GroupDTO.Info get(Long id);

    TotalResponse<GroupDTO.Info> search(NICICOCriteria request);

    GroupDTO.Info create(GroupDTO.Create dto);

    GroupDTO.Info update(GroupDTO.Update dto);

    void delete(Long id);

    SearchDTO.SearchRs<GroupDTO.Info> search(SearchDTO.SearchRq request) throws IllegalAccessException, NoSuchFieldException;


}
