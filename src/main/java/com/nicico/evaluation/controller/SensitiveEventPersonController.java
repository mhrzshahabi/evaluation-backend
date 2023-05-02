package com.nicico.evaluation.controller;

import com.nicico.copper.common.dto.search.SearchDTO;
import com.nicico.evaluation.dto.FilterDTO;
import com.nicico.evaluation.dto.SensitiveEventPersonDTO;
import com.nicico.evaluation.exception.EvaluationHandleException;
import com.nicico.evaluation.iservice.ISensitiveEventPersonService;
import com.nicico.evaluation.utility.CriteriaUtil;
import com.nicico.evaluation.utility.ExceptionUtil;
import io.swagger.annotations.Api;
import lombok.RequiredArgsConstructor;
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

@RequiredArgsConstructor
@Api(value = "Sensitive Event Person")
@RestController
@RequestMapping(value = "/api/sensitive-event-person")
public class SensitiveEventPersonController {

    private final ISensitiveEventPersonService service;
    private final ResourceBundleMessageSource messageSource;
    private final ExceptionUtil exceptionUtil;

    /**
     * @param id is the SensitiveEventPerson id
     * @return SensitiveEventPersonDTO.Info is the single SensitiveEventPerson entity
     */
    @GetMapping(value = "/{id}")
    public ResponseEntity<SensitiveEventPersonDTO.Info> get(@PathVariable Long id) {
        return new ResponseEntity<>(service.get(id), HttpStatus.OK);
    }

    /**
     * @param count      is the number of entity to every page
     * @param startIndex is the start Index in current page
     * @return SensitiveEventPersonDTO.SpecResponse that contain list of SensitiveEventPersonDTO and the number of total entity
     */
    @GetMapping(value = "/list")
    public ResponseEntity<SensitiveEventPersonDTO.SpecResponse> list(@RequestParam int count, @RequestParam int startIndex) {
        return new ResponseEntity<>(service.list(count, startIndex), HttpStatus.OK);
    }

    /**
     * @param request is the model of input for create SensitiveEventPerson entity
     * @return SensitiveEventPersonDTO.Info is the saved SensitiveEventPerson entity
     */
    @PostMapping
    public ResponseEntity<SensitiveEventPersonDTO.Info> create(@Valid @RequestBody SensitiveEventPersonDTO.Create request) {
        return new ResponseEntity<>(service.create(request), HttpStatus.CREATED);
    }

    /**
     * @param request is  the model of input for update SensitiveEventPerson entity
     * @return SensitiveEventPersonDTO.Info is the updated SensitiveEventPerson entity
     */
    @PutMapping
    public ResponseEntity<SensitiveEventPersonDTO.Info> update(@Valid @RequestBody SensitiveEventPersonDTO.Update request) {
        return new ResponseEntity<>(service.update(request), HttpStatus.OK);
    }

    /**
     * @param id is the SensitiveEventPerson id for delete
     * @return status code only
     */
    @DeleteMapping(value = {"/{id}"})
    public ResponseEntity<String> delete(@Validated @PathVariable Long id) {
        try {
            service.delete(id);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (DataIntegrityViolationException violationException) {
            final Locale locale = LocaleContextHolder.getLocale();
            String msg = exceptionUtil.getRecordsByParentId(violationException, id);
            throw new EvaluationHandleException(EvaluationHandleException.ErrorType.IntegrityConstraint, null, messageSource.getMessage("exception.integrity.constraint", null, locale) + msg);
        } catch (Exception exception) {
            throw new EvaluationHandleException(EvaluationHandleException.ErrorType.NotDeletable);
        }
    }

    /**
     * @param count      is the number of entity to every page
     * @param startIndex is the start Index in current page
     * @param criteria   is the key value pair for criteria
     * @return TotalResponse<SensitiveEventPersonDTO.Info> is the list of SensitiveEventPersonInfo entity that match the criteria
     */
    @PostMapping(value = "/spec-list")
    public ResponseEntity<SensitiveEventPersonDTO.SpecResponse> search(@RequestParam(value = "startIndex", required = false, defaultValue = "0") Integer startIndex,
                                                                       @RequestParam(value = "count", required = false, defaultValue = "30") Integer count,
                                                                       @RequestBody List<FilterDTO> criteria) throws NoSuchFieldException, IllegalAccessException {
        SearchDTO.SearchRq request = CriteriaUtil.ConvertCriteriaToSearchRequest(criteria, count, startIndex);
        SearchDTO.SearchRs<SensitiveEventPersonDTO.Info> data = service.search(request);
        final SensitiveEventPersonDTO.Response response = new SensitiveEventPersonDTO.Response();
        final SensitiveEventPersonDTO.SpecResponse specRs = new SensitiveEventPersonDTO.SpecResponse();
        response.setData(data.getList())
                .setStartRow(startIndex)
                .setEndRow(startIndex + data.getList().size())
                .setTotalRows(data.getTotalCount().intValue());
        specRs.setResponse(response);
        return new ResponseEntity<>(specRs, HttpStatus.OK);
    }

}
