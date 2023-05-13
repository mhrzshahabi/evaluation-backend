package com.nicico.evaluation.controller;

import com.nicico.copper.common.dto.search.SearchDTO;
import com.nicico.evaluation.dto.CatalogDTO;
import com.nicico.evaluation.dto.FilterDTO;
import com.nicico.evaluation.exception.EvaluationHandleException;
import com.nicico.evaluation.iservice.ICatalogService;
import com.nicico.evaluation.utility.BaseResponse;
import com.nicico.evaluation.utility.CriteriaUtil;
import com.nicico.evaluation.utility.EvaluationConstant;
import com.nicico.evaluation.utility.ExceptionUtil;
import lombok.AllArgsConstructor;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Locale;

@RestController
@RequestMapping("/api/catalog")
@Validated
@AllArgsConstructor
public class CatalogController {

    private final ICatalogService service;
    private final ExceptionUtil exceptionUtil;
    private final ResourceBundleMessageSource messageSource;

    /**
     * @param id is the Catalog id
     * @return CatalogDTO.Info is the single kPIType entity
     */
    @GetMapping(value = "/{id}")
    public ResponseEntity<CatalogDTO.Info> get(@PathVariable Long id) {
        return new ResponseEntity<>(service.get(id), HttpStatus.OK);
    }

    /**
     * @param count      is the number of entity to every page
     * @param startIndex is the start Index in current page
     * @return CatalogDTO.SpecResponse that contain list of CatalogDTO and the number of total entity
     */
    @GetMapping(value = "/list")
    public ResponseEntity<CatalogDTO.SpecResponse> list(@RequestParam int count, @RequestParam int startIndex) {
        return new ResponseEntity<>(service.list(count, startIndex), HttpStatus.OK);
    }

    /**
     * @param create is the model of input for create catalog entity
     * @return CatalogDTO.Info is the saved catalog entity
     */
    @PostMapping
    public ResponseEntity<CatalogDTO.Info> create(@RequestBody CatalogDTO.Create create) {
        return new ResponseEntity<>(service.create(create), HttpStatus.OK);
    }

    /**
     * @param request is the model of input for update catalog entity
     * @return CatalogDTO.Info is the updated catalog entity
     */
    @PutMapping(value = "/{id}")
    public ResponseEntity<CatalogDTO.Info> update(@PathVariable Long id, @Valid @RequestBody CatalogDTO.Update request) {
        return new ResponseEntity<>(service.update(id, request), HttpStatus.OK);
    }

    /**
     * @param id is the catalog id for delete
     * @return status code only
     */
    @DeleteMapping(value = "/{id}")
    public ResponseEntity<BaseResponse> delete(@PathVariable Long id) {
        final Locale locale = LocaleContextHolder.getLocale();
        try {
            service.delete(id);
            BaseResponse response = new BaseResponse();
            response.setMessage(messageSource.getMessage("message.successful.operation", null, locale));
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (DataIntegrityViolationException violationException) {
            String msg = exceptionUtil.getRecordsByParentId(violationException, id);
            throw new EvaluationHandleException(EvaluationHandleException.ErrorType.IntegrityConstraint, null, messageSource.getMessage("exception.integrity.constraint", null, locale) + msg);
        } catch (Exception exception) {
            throw new EvaluationHandleException(EvaluationHandleException.ErrorType.NotDeletable);
        }
    }

    /**
     * @return list of CatalogDTO.SpecResponse with catalogType code equals LEVEL_DEF
     */
    @GetMapping(value = "/level-def-list")
    public ResponseEntity<CatalogDTO.SpecResponse> levelDefList() {
        return new ResponseEntity<>(service.levelDefList(EvaluationConstant.LEVEL_DEF), HttpStatus.OK);
    }

    /**
     * @param count      is the number of entity to every page
     * @param startIndex is the start Index in current page
     * @param criteria is the key value pair for criteria
     * @return TotalResponse<CatalogDTO.Info> is the list of catalogInfo entity that match the criteria
     */
    @PostMapping(value = "/spec-list")
    public ResponseEntity<CatalogDTO.SpecResponse> search(@RequestParam(value = "startIndex", required = false, defaultValue = "0") Integer startIndex,
                                                        @RequestParam(value = "count", required = false, defaultValue = "30") Integer count,
                                                        @RequestBody List<FilterDTO> criteria) throws NoSuchFieldException, IllegalAccessException {
        SearchDTO.SearchRq request = CriteriaUtil.ConvertCriteriaToSearchRequest(criteria, count, startIndex);
        SearchDTO.SearchRs<CatalogDTO.Info> data = service.search(request);
        final CatalogDTO.Response response = new CatalogDTO.Response();
        final CatalogDTO.SpecResponse specRs = new CatalogDTO.SpecResponse();
        response.setData(data.getList())
                .setStartRow(startIndex)
                .setEndRow(startIndex + data.getList().size())
                .setTotalRows(data.getTotalCount().intValue());
        specRs.setResponse(response);
        return new ResponseEntity<>(specRs, HttpStatus.OK);
    }
}
