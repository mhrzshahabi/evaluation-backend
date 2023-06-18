package com.nicico.evaluation.iservice;

import com.nicico.copper.common.dto.search.SearchDTO;
import com.nicico.evaluation.dto.PersonnelDTO;

public interface IPersonnelService {

    PersonnelDTO.Info get(Long id);

    PersonnelDTO.Info getByNationalCode(String nationalCode);

    PersonnelDTO.SpecResponse list(int count, int startIndex);

    SearchDTO.SearchRs<PersonnelDTO.Info> search(SearchDTO.SearchRq request) throws IllegalAccessException, NoSuchFieldException;

}
