package com.nicico.evaluation.iservice;

import com.nicico.copper.common.dto.search.SearchDTO;
import com.nicico.evaluation.dto.CatalogTypeDTO;
import org.springframework.web.bind.annotation.RequestParam;

public interface ICatalogTypeService {

    CatalogTypeDTO.Info get(Long id);

    CatalogTypeDTO.Info getByCode(String code);

    CatalogTypeDTO.SpecResponse list(@RequestParam int count, @RequestParam int startIndex);

    CatalogTypeDTO.Info create(CatalogTypeDTO.Create create);

    CatalogTypeDTO.Info update(Long id, CatalogTypeDTO.Update update);

    void delete(Long id);

    SearchDTO.SearchRs<CatalogTypeDTO.Info> search(SearchDTO.SearchRq request) throws IllegalAccessException, NoSuchFieldException;

}
