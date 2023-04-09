package com.nicico.evaluation.iservice;

import com.nicico.copper.common.domain.criteria.NICICOCriteria;
import com.nicico.copper.common.dto.grid.TotalResponse;
import com.nicico.evaluation.dto.GroupGradeDTO;

import java.util.List;

public interface IGroupGradeService {

    GroupGradeDTO.Info get(Long id) throws Exception;

    List<GroupGradeDTO.Info> list();

    TotalResponse<GroupGradeDTO.Info> search(NICICOCriteria request);

    GroupGradeDTO.Info create(GroupGradeDTO.Create dto);

    GroupGradeDTO.Info update(GroupGradeDTO.Update dto);

    void delete(Long id);
}
