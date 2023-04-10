package com.nicico.evaluation.service;

import com.nicico.evaluation.dto.CatalogDTO;
import com.nicico.evaluation.exception.ApplicationException;
import com.nicico.evaluation.exception.ServiceException;
import com.nicico.evaluation.iservice.ICatalogService;
import com.nicico.evaluation.mapper.CatalogBeanMapper;
import com.nicico.evaluation.model.Catalog;
import com.nicico.evaluation.repository.CatalogRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

import static com.nicico.evaluation.exception.CoreException.*;

@Service
@RequiredArgsConstructor
public class CatalogService implements ICatalogService {

    private final ModelMapper modelMapper;
    private final CatalogBeanMapper mapper;
    private final CatalogRepository repository;
    private final ApplicationException<ServiceException> applicationException;

    @Transactional(readOnly = true)
    @Override
    public CatalogDTO.Info getById(Long id) {
        Optional<Catalog> optionalCatalog = repository.findById(id);
        return mapper.entityToDtoInfo(optionalCatalog.orElseThrow(() -> applicationException.createApplicationException(NOT_FOUND, HttpStatus.NOT_FOUND)));
    }

    @Transactional
    @Override
    public CatalogDTO.Info create(CatalogDTO.Create create) {
        Catalog catalog = mapper.dtoCreateToEntity(create);
        try {
            return mapper.entityToDtoInfo(repository.saveAndFlush(catalog));
        } catch (Exception exception) {
            throw applicationException.createApplicationException(NOT_SAVE, HttpStatus.NOT_ACCEPTABLE);
        }
    }

    @Transactional
    @Override
    public CatalogDTO.Info update(CatalogDTO.Update update) {
        Optional<Catalog> optional = repository.findById(update.getId());
        Catalog currentEntity = optional.orElseThrow(() -> applicationException.createApplicationException(NOT_FOUND, HttpStatus.NOT_FOUND));
        Catalog entity = new Catalog();
        modelMapper.map(currentEntity, entity);
        modelMapper.map(update, entity);
        try {
            return mapper.entityToDtoInfo(repository.save(entity));
        } catch (Exception e) {
            throw applicationException.createApplicationException(NOT_UPDATE, HttpStatus.NOT_ACCEPTABLE);
        }
    }

    @Transactional
    @Override
    public void delete(Long id) {
        repository.findById(id).orElseThrow(() -> applicationException.createApplicationException(NOT_FOUND, HttpStatus.NOT_FOUND));
        repository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<CatalogDTO.Info> list(String code) {
        List<Catalog> allByCatalogTypeCode = repository.findAllByCatalogTypeCode(code);
        return mapper.entityToDtoInfoList(allByCatalogTypeCode);
    }
}
