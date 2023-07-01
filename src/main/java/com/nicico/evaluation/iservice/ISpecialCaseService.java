package com.nicico.evaluation.iservice;

import com.nicico.copper.common.dto.search.SearchDTO;
import com.nicico.evaluation.dto.SpecialCaseDTO;
import com.nicico.evaluation.utility.BaseResponse;

import java.util.List;

public interface ISpecialCaseService {

    SpecialCaseDTO.Info get(Long id);

    List<SpecialCaseDTO.Info> getByAssessNationalCodeAndStatusCode(String nationalCode, String statusCode);

    List<SpecialCaseDTO.Info> getByAssessNationalCodeAndStatusCodeNotIn(String nationalCode, String statusCode, List<Long> id);

    SpecialCaseDTO.SpecResponse list(int count, int startIndex);

    SearchDTO.SearchRs<SpecialCaseDTO.Info> search(SearchDTO.SearchRq request) throws IllegalAccessException, NoSuchFieldException;

    SpecialCaseDTO.Info create(SpecialCaseDTO.Create dto);

    SpecialCaseDTO.Info update(Long id, SpecialCaseDTO.Update dto);

    void delete(Long id);

    BaseResponse changeAllStatus(SpecialCaseDTO.ChangeAllStatusDTO changeAllStatusDTO);

    BaseResponse changeStatus(SpecialCaseDTO.ChangeStatusDTO changeStatusDTO);
}
    