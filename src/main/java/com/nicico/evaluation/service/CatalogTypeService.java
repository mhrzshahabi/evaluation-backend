package com.nicico.evaluation.service;

import com.nicico.copper.common.dto.search.SearchDTO;
import com.nicico.evaluation.common.PageableMapper;
import com.nicico.evaluation.dto.CatalogTypeDTO;
import com.nicico.evaluation.exception.ApplicationException;
import com.nicico.evaluation.exception.ServiceException;
import com.nicico.evaluation.iservice.ICatalogTypeService;
import com.nicico.evaluation.mapper.CatalogTypeBeanMapper;
import com.nicico.evaluation.model.CatalogType;
import com.nicico.evaluation.repository.CatalogTypeRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Optional;

import static com.nicico.evaluation.exception.CoreException.*;

@Service
@RequiredArgsConstructor
public class CatalogTypeService implements ICatalogTypeService {

    private final ModelMapper modelMapper;
    private final CatalogTypeBeanMapper mapper;
    private final PageableMapper pageableMapper;
    private final CatalogTypeRepository repository;
    private final ApplicationException<ServiceException> applicationException;

    @Override
    @Transactional(readOnly = true)
    public CatalogTypeDTO.Info getById(Long id) {
        Optional<CatalogType> optionalCatalogType = repository.findById(id);
        return mapper.entityToDtoInfo(optionalCatalogType.orElseThrow(() -> applicationException.createApplicationException(NOT_FOUND, HttpStatus.NOT_FOUND)));
    }

    @Override
    @Transactional(readOnly = true)
    public CatalogTypeDTO.Info getByCode(String code) {
        Optional<CatalogType> optionalCatalogType = repository.findByCode(code);
        return mapper.entityToDtoInfo(optionalCatalogType.orElseThrow(() -> applicationException.createApplicationException(NOT_FOUND, HttpStatus.NOT_FOUND)));
    }

    @Override
    @Transactional(readOnly = true)
    public CatalogTypeDTO.SpecResponse list(@RequestParam int count, @RequestParam int startIndex) {
        Pageable pageable = pageableMapper.toPageable(count, startIndex);
        Page<CatalogType> catalogTypes = repository.findAll(pageable);
        List<CatalogTypeDTO.Info> catalogTypeInfos = mapper.entityToDtoInfoList(catalogTypes.getContent());

        CatalogTypeDTO.Response response = new CatalogTypeDTO.Response();
        CatalogTypeDTO.SpecResponse specResponse = new CatalogTypeDTO.SpecResponse();

        if (catalogTypeInfos != null) {
            response.setData(catalogTypeInfos)
                    .setStartRow(startIndex)
                    .setEndRow(startIndex + count)
                    .setTotalRows((int) catalogTypes.getTotalElements());
            specResponse.setResponse(response);
        }
        return specResponse;
    }

    @Override
    @Transactional
    public CatalogTypeDTO.Info create(CatalogTypeDTO.Create create) {
        CatalogType catalogType = mapper.dtoCreateToEntity(create);
        try {
            return mapper.entityToDtoInfo(repository.saveAndFlush(catalogType));
        } catch (Exception e) {
            throw applicationException.createApplicationException(NOT_SAVE, HttpStatus.NOT_ACCEPTABLE);
        }
    }

    @Override
    @Transactional
    public CatalogTypeDTO.Info update(CatalogTypeDTO.Update update) {
        Optional<CatalogType> optional = repository.findById(update.getId());
        CatalogType currentEntity = optional.orElseThrow(() -> applicationException.createApplicationException(NOT_FOUND, HttpStatus.NOT_FOUND));
        CatalogType entity = new CatalogType();
        modelMapper.map(currentEntity, entity);
        modelMapper.map(update, entity);
        try {
            return mapper.entityToDtoInfo(repository.save(entity));
        } catch (Exception e) {
            throw applicationException.createApplicationException(NOT_UPDATE, HttpStatus.NOT_ACCEPTABLE);
        }
    }

    @Override
    @Transactional
    public void delete(Long id) {
        repository.findById(id).orElseThrow(() -> applicationException.createApplicationException(NOT_FOUND, HttpStatus.NOT_FOUND));
        repository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public SearchDTO.SearchRs<CatalogTypeDTO.Info> search(SearchDTO.SearchRq request) throws IllegalAccessException, NoSuchFieldException {
        return BaseService.optimizedSearch(repository, mapper::entityToDtoInfo, request);

    }
}
