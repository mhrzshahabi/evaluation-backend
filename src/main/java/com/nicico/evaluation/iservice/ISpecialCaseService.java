package com.nicico.evaluation.iservice;

import com.nicico.copper.common.dto.search.SearchDTO;
import com.nicico.evaluation.dto.SpecialCaseDTO;
import com.nicico.evaluation.utility.BaseResponse;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface ISpecialCaseService {

    SpecialCaseDTO.Info get(Long id);

    SpecialCaseDTO.Info getByAssessNationalCodeAndAssessPostCode(String nationalCode, String postCode);

    List<SpecialCaseDTO.Info> getByAssessNationalCodeAndStatusCode(String nationalCode, String statusCode);

    SpecialCaseDTO.SpecResponse list(int count, int startIndex);

    SearchDTO.SearchRs<SpecialCaseDTO.Info> search(SearchDTO.SearchRq request) throws IllegalAccessException, NoSuchFieldException;

    SpecialCaseDTO.Info create(SpecialCaseDTO.Create dto);

    SpecialCaseDTO.Info update(Long id, SpecialCaseDTO.Update dto);

    void delete(Long id);

    BaseResponse changeStatus(SpecialCaseDTO.ChangeStatusDTO changeStatusDTO);
}
    