package com.nicico.evaluation.iservice;


import com.nicico.evaluation.dto.CatalogDTO;

import java.util.List;

public interface ICatalogService {

    CatalogDTO.Info getById(Long id);
    CatalogDTO.Info create(CatalogDTO.Create create);
    CatalogDTO.Info update(Long id, CatalogDTO.Update update);
    void delete(Long id);
    List<CatalogDTO.Info> list(String code);
}
