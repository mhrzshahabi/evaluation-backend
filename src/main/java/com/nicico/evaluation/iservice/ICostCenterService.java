package com.nicico.evaluation.iservice;

import com.nicico.copper.common.dto.search.SearchDTO;
import com.nicico.evaluation.dto.CostCenterDTO;

public interface ICostCenterService {

    SearchDTO.SearchRs<CostCenterDTO.Info> search(SearchDTO.SearchRq request) throws IllegalAccessException, NoSuchFieldException;
}
