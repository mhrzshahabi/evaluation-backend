package com.nicico.evaluation.iservice;


import com.nicico.evaluation.dto.CatalogDTO;

import java.util.List;

public interface ICatalogService {

    CatalogDTO.Info getById(Long id);

    List<CatalogDTO.Info> list(String code);

    CatalogDTO.Info create(CatalogDTO.Create create);

    CatalogDTO.Info update(CatalogDTO.Update update);

    void delete(Long id);

}
