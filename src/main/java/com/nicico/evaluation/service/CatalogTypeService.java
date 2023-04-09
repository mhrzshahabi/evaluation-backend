package com.nicico.evaluation.service;

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

import java.util.List;
import java.util.Optional;

import static com.nicico.evaluation.exception.CoreException.*;

@Service
@RequiredArgsConstructor
public class CatalogTypeService implements ICatalogTypeService {

    private final ModelMapper modelMapper;
    private final CatalogTypeRepository catalogTypeRepository;
    private final CatalogTypeBeanMapper catalogTypeBeanMapper;
    private final ApplicationException<ServiceException> applicationException;

    @Override
    @Transactional(readOnly = true)
    public CatalogTypeDTO.Info getById(Long id) {
        Optional<CatalogType> optionalCatalogType = catalogTypeRepository.findById(id);
        return catalogTypeBeanMapper.entityToDtoInfo(optionalCatalogType.orElseThrow(() -> applicationException.createApplicationException(NOT_FOUND, HttpStatus.NOT_FOUND)));
    }

    @Override
    @Transactional(readOnly = true)
    public CatalogTypeDTO.Info getByCode(String code) {
        Optional<CatalogType> optionalCatalogType = catalogTypeRepository.findByCode(code);
        return catalogTypeBeanMapper.entityToDtoInfo(optionalCatalogType.orElseThrow(() -> applicationException.createApplicationException(NOT_FOUND, HttpStatus.NOT_FOUND)));
    }

    @Override
    @Transactional(readOnly = true)
    public List<CatalogTypeDTO.Info> list(Pageable pageable) {
        Page<CatalogType> catalogTypes = catalogTypeRepository.findAll(pageable);
        return catalogTypeBeanMapper.entityToDtoInfoList(catalogTypes.getContent());
    }

    @Override
    @Transactional
    public CatalogTypeDTO.Info create(CatalogTypeDTO.Create create) {
        CatalogType catalogType = catalogTypeBeanMapper.dtoCreateToEntity(create);
        try {
            return catalogTypeBeanMapper.entityToDtoInfo(catalogTypeRepository.saveAndFlush(catalogType));
        } catch (Exception e) {
            throw applicationException.createApplicationException(NOT_SAVE, HttpStatus.NOT_ACCEPTABLE);
        }
    }

    @Override
    @Transactional
    public CatalogTypeDTO.Info update(CatalogTypeDTO.Update update) {
        Optional<CatalogType> optional = catalogTypeRepository.findById(update.getId());
        CatalogType currentEntity = optional.orElseThrow(() -> applicationException.createApplicationException(NOT_FOUND, HttpStatus.NOT_FOUND));
        CatalogType entity = new CatalogType();
        modelMapper.map(currentEntity, entity);
        modelMapper.map(update, entity);
        try {
            return catalogTypeBeanMapper.entityToDtoInfo(catalogTypeRepository.save(entity));
        } catch (Exception e) {
            throw applicationException.createApplicationException(NOT_UPDATE, HttpStatus.NOT_ACCEPTABLE);
        }
    }

    @Override
    @Transactional
    public void delete(Long id) {
        catalogTypeRepository.findById(id).orElseThrow(() -> applicationException.createApplicationException(NOT_FOUND, HttpStatus.NOT_FOUND));
        catalogTypeRepository.deleteById(id);
    }
}
