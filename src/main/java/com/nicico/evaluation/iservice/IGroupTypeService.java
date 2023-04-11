package com.nicico.evaluation.iservice;

import com.nicico.copper.common.dto.search.SearchDTO;
import com.nicico.evaluation.dto.GroupTypeDTO;

public interface IGroupTypeService {

    GroupTypeDTO.Info get(Long id);

    GroupTypeDTO.SpecResponse list(int count, int startIndex);

    GroupTypeDTO.Info create(GroupTypeDTO.Create dto);

    GroupTypeDTO.Info update(GroupTypeDTO.Update dto);

    void delete(Long id);

    SearchDTO.SearchRs<GroupTypeDTO.Info> search(SearchDTO.SearchRq request) throws IllegalAccessException, NoSuchFieldException;

}
