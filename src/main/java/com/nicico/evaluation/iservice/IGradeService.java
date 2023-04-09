package com.nicico.evaluation.iservice;

import com.nicico.copper.common.domain.criteria.NICICOCriteria;
import com.nicico.copper.common.dto.grid.TotalResponse;
import com.nicico.evaluation.dto.GradeDTO;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface IGradeService {

    GradeDTO.Info get(Long id) throws Exception;

    @Transactional(readOnly = true)
//    @PreAuthorize("hasAuthority('R_GRADE')")
    GradeDTO.Info getByCode(String code) throws Exception;

    List<GradeDTO.Info> list();

    TotalResponse<GradeDTO.Info> search(NICICOCriteria request);

}
