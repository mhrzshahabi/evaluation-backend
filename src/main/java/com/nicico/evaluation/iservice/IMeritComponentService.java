package com.nicico.evaluation.iservice;

import com.nicico.copper.common.domain.criteria.NICICOCriteria;
import com.nicico.copper.common.dto.grid.TotalResponse;
import com.nicico.evaluation.dto.MeritComponentDTO;

import java.util.List;

public interface IMeritComponentService {

    MeritComponentDTO.Info get(Long id) throws Exception;

    List<MeritComponentDTO.Info> list();

    TotalResponse<MeritComponentDTO.Info> search(NICICOCriteria request);

    MeritComponentDTO.Info create(MeritComponentDTO.Create dto);

    MeritComponentDTO.Info update(MeritComponentDTO.Update dto);

    void delete(Long id);
}
