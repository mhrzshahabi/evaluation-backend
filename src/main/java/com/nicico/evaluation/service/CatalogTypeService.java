package com.nicico.evaluation.service;

import com.nicico.evaluation.dto.CatalogTypeDTO;
import com.nicico.evaluation.exception.ApplicationException;
import com.nicico.evaluation.exception.NotFoundException;
import com.nicico.evaluation.exception.ServiceException;
import com.nicico.evaluation.iservice.ICatalogTypeService;
import com.nicico.evaluation.mapper.CatalogTypeBeanMapper;
import com.nicico.evaluation.model.CatalogType;
import com.nicico.evaluation.repository.CatalogTypeRepository;
import lombok.RequiredArgsConstructor;
import org.hibernate.exception.ConstraintViolationException;
import org.modelmapper.ModelMapper;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.expression.EvaluationException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static com.nicico.evaluation.exception.CoreException.NOT_FOUND;

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
        return catalogTypeBeanMapper.catalogTypeToInfo(optionalCatalogType.orElseThrow(() -> applicationException.createApplicationException(NOT_FOUND, HttpStatus.NOT_FOUND)));
    }

    @Override
    @Transactional(readOnly = true)
    public CatalogTypeDTO.Info getByCode(String code) {
        Optional<CatalogType> optionalCatalogType = catalogTypeRepository.findByCode(code);
        return catalogTypeBeanMapper.catalogTypeToInfo(optionalCatalogType.orElse(null));
    }

    @Override
    @Transactional
    public CatalogTypeDTO.Info create(CatalogTypeDTO.Create create) {
        CatalogType catalogType = catalogTypeBeanMapper.createToCatalogType(create);
        try {
            return catalogTypeBeanMapper.catalogTypeToInfo(catalogTypeRepository.saveAndFlush(catalogType));
        } catch (NotFoundException notFoundException) {
            throw new NotFoundException("not-message");
        }
    }

    @Override
    @Transactional
    public CatalogTypeDTO.Info update(Long id, CatalogTypeDTO.Update update) {
        Optional<CatalogType> optional = catalogTypeRepository.findById(id);
        CatalogType currentEntity = optional.orElseThrow(() -> new EvaluationException(""));
        CatalogType entity = new CatalogType();
        modelMapper.map(currentEntity, entity);
        modelMapper.map(update, entity);
        try {
            return catalogTypeBeanMapper.catalogTypeToInfo(catalogTypeRepository.save(entity));
        } catch (ConstraintViolationException | DataIntegrityViolationException e) {
            return null;
        }
    }

    @Override
    @Transactional
    public void delete(Long id) {
        Optional<CatalogType> optionalCatalogType = catalogTypeRepository.findById(id);
        optionalCatalogType.orElseThrow(() -> null);
        catalogTypeRepository.deleteById(id);
    }
}
