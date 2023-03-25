package com.nicico.evaluation.equipment.standard_test_status;


import com.nicico.copper.common.dto.search.SearchDTO;
import org.springframework.data.domain.Page;

import java.util.List;

public interface IStandardTestStatusService {

    StandardTestStatus save(StandardTestStatus standardtest);
    StandardTestStatus update(StandardTestStatus standardtest);
    StandardTestStatus getById(Long standardtestId);
    List<StandardTestStatus> getAll();
    Page<StandardTestStatus> getAll(int page, int size);
    void  delete(List<Long> ids);


    SearchDTO.SearchRs<StandardTestStatusDTO> searchStandardTest(SearchDTO.SearchRq filter);




}
