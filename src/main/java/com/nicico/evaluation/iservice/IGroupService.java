package com.nicico.evaluation.iservice;

import com.nicico.copper.common.dto.search.SearchDTO;
import com.nicico.evaluation.dto.GroupDTO;
import com.nicico.evaluation.dto.GroupGradeDTO;

import java.util.List;

public interface IGroupService {

    GroupDTO.SpecResponse list(int count, int startIndex);

    GroupDTO.Info get(Long id);

    List<GroupGradeDTO.Info> create(GroupDTO.Create dto);

    GroupDTO.Info update(GroupDTO.Update dto);

    void delete(Long id);

    SearchDTO.SearchRs<GroupDTO.Info> search(SearchDTO.SearchRq request) throws IllegalAccessException, NoSuchFieldException;

}

