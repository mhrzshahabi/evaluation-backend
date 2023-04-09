package com.nicico.evaluation.iservice;


import com.nicico.evaluation.dto.CatalogTypeDTO;

public interface ICatalogTypeService {

    CatalogTypeDTO.Info getById(Long id);
    CatalogTypeDTO.Info getByCode(String code);
    CatalogTypeDTO.Info create(CatalogTypeDTO.Create create);
    CatalogTypeDTO.Info update(CatalogTypeDTO.Update update);
    void delete(Long id);
}
