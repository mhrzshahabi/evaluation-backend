package com.nicico.evaluation.service;

import com.nicico.copper.common.domain.criteria.NICICOCriteria;
import com.nicico.copper.common.dto.grid.TotalResponse;
import com.nicico.evaluation.DTO.KPITypeDTO;

import java.util.List;

public interface KPITypeService {

    KPITypeDTO.Info get(Long id) throws Exception;

    List<KPITypeDTO.Info> list();

    TotalResponse<KPITypeDTO.Info> search(NICICOCriteria request);

    KPITypeDTO.Info create(KPITypeDTO.Create dto);

    KPITypeDTO.Info update(KPITypeDTO.Update dto);

    void delete(KPITypeDTO.Delete dto);
}
