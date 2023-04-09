package com.nicico.evaluation.iservice;

import com.nicico.copper.common.domain.criteria.NICICOCriteria;
import com.nicico.copper.common.dto.grid.TotalResponse;
import com.nicico.evaluation.dto.GroupGradeDTO;

import java.util.List;

public interface IGroupGradeService {

    GroupGradeDTO.Info get(Long id);

    List<GroupGradeDTO.Info> list();

    TotalResponse<GroupGradeDTO.Info> search(NICICOCriteria request);

    List<GroupGradeDTO.Info> createAll(List<GroupGradeDTO.Create> requests);

    GroupGradeDTO.Info create(GroupGradeDTO.Create dto);

    List<GroupGradeDTO.Info> createGroupGrade(GroupGradeDTO.CreateAll dto);

    GroupGradeDTO.Info update(GroupGradeDTO.Update dto);

    void delete(Long id);
}
