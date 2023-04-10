package com.nicico.evaluation.iservice;

import com.nicico.copper.common.domain.criteria.NICICOCriteria;
import com.nicico.copper.common.dto.grid.TotalResponse;
import com.nicico.copper.common.dto.search.SearchDTO;
import com.nicico.evaluation.dto.GroupGradeDTO;

import java.util.List;

public interface IGroupGradeService {

    GroupGradeDTO.Info get(Long id);

    GroupGradeDTO.SpecResponse list(int count, int startIndex);

    TotalResponse<GroupGradeDTO.Info> search(NICICOCriteria request);

    List<GroupGradeDTO.Info> createAll(List<GroupGradeDTO.Create> requests);

    GroupGradeDTO.Info create(GroupGradeDTO.Create dto);

    List<GroupGradeDTO.Info> createGroupGrade(GroupGradeDTO.CreateAll dto);

    List<GroupGradeDTO.Info> update(Long id, GroupGradeDTO.CreateAll dto);

    void deleteAll(List<Long> ids);

    void delete(Long id);

    SearchDTO.SearchRs<GroupGradeDTO.Info> search(SearchDTO.SearchRq request) throws IllegalAccessException, NoSuchFieldException;


}