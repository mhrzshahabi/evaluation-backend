package com.nicico.evaluation.iservice;


import com.nicico.evaluation.dto.CatalogTypeDTO;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ICatalogTypeService {

    CatalogTypeDTO.Info getById(Long id);
    CatalogTypeDTO.Info getByCode(String code);
    List<CatalogTypeDTO.Info> list(Pageable pageable);
    CatalogTypeDTO.Info create(CatalogTypeDTO.Create create);
    CatalogTypeDTO.Info update(CatalogTypeDTO.Update update);
    void delete(Long id);
}
