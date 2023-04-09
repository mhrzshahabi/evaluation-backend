package com.nicico.evaluation.iservice;


import com.nicico.evaluation.dto.CatalogTypeDTO;

import java.util.List;

public interface ICatalogTypeService {

    CatalogTypeDTO.Info getById(Long id);
    CatalogTypeDTO.Info getByCode(String code);
    List<CatalogTypeDTO.Info> list();
    CatalogTypeDTO.Info create(CatalogTypeDTO.Create create);
    CatalogTypeDTO.Info update(CatalogTypeDTO.Update update);
    void delete(Long id);
}
