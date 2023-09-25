package com.nicico.evaluation.iservice;

import com.nicico.copper.common.dto.search.SearchDTO;
import com.nicico.evaluation.dto.PersonDTO;

import java.util.List;

public interface IPersonService {

    PersonDTO.Info get(Long id);

    PersonDTO.Info getByNationalCode(String nationalCode);

    List<PersonDTO.Info> getAllByNationalCode(List<String> nationalCodes);

    PersonDTO.Info getByPostCode(String postCode);

    PersonDTO.SpecResponse list(int count, int startIndex);

    SearchDTO.SearchRs<PersonDTO.Info> search(SearchDTO.SearchRq request) throws IllegalAccessException, NoSuchFieldException;

}
