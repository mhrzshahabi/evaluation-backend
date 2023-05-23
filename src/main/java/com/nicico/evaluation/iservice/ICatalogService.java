package com.nicico.evaluation.iservice;

import com.nicico.copper.common.dto.search.SearchDTO;
import com.nicico.evaluation.dto.CatalogDTO;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

public interface ICatalogService {

    CatalogDTO.Info get(Long id);

    CatalogDTO.SpecResponse list(@RequestParam int count, @RequestParam int startIndex);

    CatalogDTO.Info create(CatalogDTO.Create create);

    CatalogDTO.Info update(Long id, CatalogDTO.Update update);

    void delete(Long id);

    CatalogDTO.SpecResponse catalogListByCatalogTypeCode(String code);

    List<CatalogDTO.Info> catalogByCatalogTypeCode(String code);

    SearchDTO.SearchRs<CatalogDTO.Info> search(SearchDTO.SearchRq request) throws IllegalAccessException, NoSuchFieldException;

    CatalogDTO.PureInfo getByCode(String code);

}
