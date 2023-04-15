package com.nicico.evaluation.iservice;


import com.nicico.copper.common.dto.search.SearchDTO;
import com.nicico.evaluation.dto.CatalogDTO;
import org.springframework.web.bind.annotation.RequestParam;

public interface ICatalogService {

    CatalogDTO.Info get(Long id);

    CatalogDTO.SpecResponse list(@RequestParam int count, @RequestParam int startIndex);

    CatalogDTO.Info create(CatalogDTO.Create create);

    CatalogDTO.Info update(CatalogDTO.Update update);

    void delete(Long id);

    CatalogDTO.SpecResponse levelDefList(String code);

    SearchDTO.SearchRs<CatalogDTO.Info> search(SearchDTO.SearchRq request) throws IllegalAccessException, NoSuchFieldException;

}
