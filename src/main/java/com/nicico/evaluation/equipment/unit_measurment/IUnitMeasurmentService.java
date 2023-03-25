package com.nicico.evaluation.equipment.unit_measurment;


import com.nicico.copper.common.dto.search.SearchDTO;
import com.nicico.evaluation.equipment.standard_test_status.StandardTestStatusDTO;
import org.springframework.data.domain.Page;

import java.util.List;

public interface IUnitMeasurmentService {

    UnitMeasurment save(UnitMeasurment standardtest);
    UnitMeasurment update(UnitMeasurment unitMeasurment);
    UnitMeasurment getById(Long unitMeasurmentId);
    List<UnitMeasurment> getAll();
    Page<UnitMeasurment> getAll(int page, int size);
    void  delete(List<Long> ids);


    SearchDTO.SearchRs<StandardTestStatusDTO> searchStandardTest(SearchDTO.SearchRq filter);




}
