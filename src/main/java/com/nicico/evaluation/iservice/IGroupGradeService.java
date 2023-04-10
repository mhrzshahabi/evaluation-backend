package com.nicico.evaluation.iservice;

import com.nicico.copper.common.domain.criteria.NICICOCriteria;
import com.nicico.copper.common.dto.grid.TotalResponse;
import com.nicico.evaluation.dto.GroupGradeDTO;

public interface IGroupGradeService {

    GroupGradeDTO.Info get(Long id);

    GroupGradeDTO.SpecResponse list(int count, int startIndex);

    TotalResponse<GroupGradeDTO.Info> search(NICICOCriteria request);

    GroupGradeDTO.Info create(GroupGradeDTO.Create dto);

    GroupGradeDTO.Info update(GroupGradeDTO.Update dto);

    void delete(Long id);

}
