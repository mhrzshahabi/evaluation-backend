package com.nicico.evaluation.controller;

import com.nicico.copper.common.dto.search.SearchDTO;
import com.nicico.evaluation.dto.CatalogTypeDTO;
import com.nicico.evaluation.dto.FilterDTO;
import com.nicico.evaluation.exception.EvaluationHandleException;
import com.nicico.evaluation.iservice.ICatalogTypeService;
import com.nicico.evaluation.utility.CriteriaUtil;
import com.nicico.evaluation.utility.ExceptionUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Locale;


@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping(value = "/api/catalog-type")
public class CatalogTypeController {

    private final ICatalogTypeService service;
    private final ResourceBundleMessageSource messageSource;
    private final ExceptionUtil exceptionUtil;
    /**
     * @param id is the CatalogType id
     * @return CatalogTypeDTO.Info is the single kPIType entity
     */
    @GetMapping(value = "/{id}")
    public ResponseEntity<CatalogTypeDTO.Info> get(@PathVariable Long id) {
        return new ResponseEntity<>(service.get(id), HttpStatus.OK);
    }

    /**
     * @param count      is the number of entity to every page
     * @param startIndex is the start Index in current page
     * @return CatalogTypeDTO.SpecResponse that contain list of CatalogTypeDTO and the number of total entity
     */
    @GetMapping(value = "/list")
    public ResponseEntity<CatalogTypeDTO.SpecResponse> list(@RequestParam int count, @RequestParam int startIndex) {
        return new ResponseEntity<>(service.list(count, startIndex), HttpStatus.OK);
    }

    /**
     * @param create is the model of input for create catalogType entity
     * @return CatalogTypeDTO.Info is the saved catalogType entity
     */
    @PostMapping
    public ResponseEntity<CatalogTypeDTO.Info> create(@RequestBody CatalogTypeDTO.Create create) {
        return new ResponseEntity<>(service.create(create), HttpStatus.OK);
    }

    /**
     * @param request is the model of input for update catalogType entity
     * @return CatalogTypeDTO.Info is the updated catalogType entity
     */
    @PutMapping(value = "/{id}")
    public ResponseEntity<CatalogTypeDTO.Info> update(@PathVariable Long id, @Valid @RequestBody CatalogTypeDTO.Update request) {
        return new ResponseEntity<>(service.update(id, request), HttpStatus.OK);
    }

    /**
     * @param id is the catalogType id for delete
     * @return status code only
     */
    @DeleteMapping(value = "/{id}")
    public ResponseEntity<String> delete(@PathVariable Long id) {
        final Locale locale = LocaleContextHolder.getLocale();
        try {
            service.delete(id);
            return new ResponseEntity<>(messageSource.getMessage("message.successful.operation", null, locale), HttpStatus.OK);
        } catch (DataIntegrityViolationException violationException) {
            String msg = exceptionUtil.getRecordsByParentId(violationException, id);
            throw new EvaluationHandleException(EvaluationHandleException.ErrorType.IntegrityConstraint, null, messageSource.getMessage("exception.integrity.constraint", null, locale) + msg);
        } catch (Exception exception) {
            throw new EvaluationHandleException(EvaluationHandleException.ErrorType.NotDeletable);
        }
    }

    /**
     * @param count      is the number of entity to every page
     * @param startIndex is the start Index in current page
     * @param criteria is the key value pair for criteria
     * @return TotalResponse<CatalogTypeDTO.Info> is the list of catalogTypeInfo entity that match the criteria
     */
    @PostMapping(value = "/spec-list")
    public ResponseEntity<CatalogTypeDTO.SpecResponse> search(@RequestParam(value = "startIndex", required = false, defaultValue = "0") Integer startIndex,
                                                              @RequestParam(value = "count", required = false, defaultValue = "30") Integer count,
                                                              @RequestBody List<FilterDTO> criteria) throws NoSuchFieldException, IllegalAccessException {
        SearchDTO.SearchRq request = CriteriaUtil.ConvertCriteriaToSearchRequest(criteria, count, startIndex);
        SearchDTO.SearchRs<CatalogTypeDTO.Info> data = service.search(request);
        final CatalogTypeDTO.Response response = new CatalogTypeDTO.Response();
        final CatalogTypeDTO.SpecResponse specRs = new CatalogTypeDTO.SpecResponse();
        response.setData(data.getList())
                .setStartRow(startIndex)
                .setEndRow(startIndex + data.getList().size())
                .setTotalRows(data.getTotalCount().intValue());
        specRs.setResponse(response);
        return new ResponseEntity<>(specRs, HttpStatus.OK);
    }
}
