package com.nicico.evaluation.service;

import com.nicico.evaluation.dto.CatalogDTO;
import com.nicico.evaluation.iservice.ICatalogService;
import com.nicico.evaluation.mapper.CatalogBeanMapper;
import com.nicico.evaluation.model.Catalog;
import com.nicico.evaluation.repository.CatalogRepository;
import lombok.RequiredArgsConstructor;
import org.hibernate.exception.ConstraintViolationException;
import org.modelmapper.ModelMapper;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.expression.EvaluationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CatalogService implements ICatalogService {

    private final ModelMapper modelMapper;
    private final CatalogRepository catalogRepository;
    private final CatalogBeanMapper catalogBeanMapper;

    @Transactional(readOnly = true)
    @Override
    public CatalogDTO.Info getById(Long id) {
        Optional<Catalog> optionalCatalog = catalogRepository.findById(id);
        return catalogBeanMapper.catalogToInfo(optionalCatalog.orElse(null));
    }

    @Transactional
    @Override
    public CatalogDTO.Info create(CatalogDTO.Create create) {
        Catalog catalog = catalogBeanMapper.createToCatalog(create);
        try {
            return catalogBeanMapper.catalogToInfo(catalogRepository.saveAndFlush(catalog));
        } catch (Exception exception) {
            return null;
        }
    }

    @Transactional
    @Override
    public CatalogDTO.Info update(Long id, CatalogDTO.Update update) {
        Optional<Catalog> optional = catalogRepository.findById(id);
        Catalog currentEntity = optional.orElseThrow(() -> new EvaluationException(""));
        Catalog entity = new Catalog();
        modelMapper.map(currentEntity, entity);
        modelMapper.map(update, entity);
        try {
            return catalogBeanMapper.catalogToInfo(catalogRepository.save(entity));
        } catch (ConstraintViolationException | DataIntegrityViolationException e) {
            return null;
        }
    }

    @Transactional
    @Override
    public void delete(Long id) {
        Optional<Catalog> optionalCatalog = catalogRepository.findById(id);
        optionalCatalog.orElseThrow(() -> null);
        catalogRepository.deleteById(id);
    }
}
