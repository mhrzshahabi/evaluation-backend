package com.nicico.evaluation.controller;

import com.nicico.copper.common.dto.search.SearchDTO;
import com.nicico.evaluation.dto.FilterDTO;
import com.nicico.evaluation.dto.SensitiveEventsDTO;
import com.nicico.evaluation.exception.EvaluationHandleException;
import com.nicico.evaluation.iservice.ISensitiveEventsService;
import com.nicico.evaluation.utility.CriteriaUtil;
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
@Api(value = "Sensitive Events")
@RestController
@RequestMapping(value = "/api/sensitive-events")
public class SensitiveEventsController {

    private final ISensitiveEventsService service;
    private final ResourceBundleMessageSource messageSource;

    /**
     * @param id is the SensitiveEvents id
     * @return SensitiveEventsDTO.Info is the single SensitiveEvents entity
     */
    @GetMapping(value = "/{id}")
    public ResponseEntity<SensitiveEventsDTO.Info> get(@PathVariable Long id) {
        return new ResponseEntity<>(service.get(id), HttpStatus.OK);
    }

    /**
     * @param count      is the number of entity to every page
     * @param startIndex is the start Index in current page
     * @return SensitiveEventsDTO.SpecResponse that contain list of SensitiveEventsDTO and the number of total entity
     */
    @GetMapping(value = "/list")
    public ResponseEntity<SensitiveEventsDTO.SpecResponse> list(@RequestParam int count, @RequestParam int startIndex) {
        return new ResponseEntity<>(service.list(count, startIndex), HttpStatus.OK);
    }

    /**
     * @param request is the model of input for create SensitiveEvents entity
     * @return SensitiveEventsDTO.Info is the saved SensitiveEvents entity
     */
    @PostMapping
    public ResponseEntity<SensitiveEventsDTO.Info> create(@Valid @RequestBody SensitiveEventsDTO.Create request) {
        return new ResponseEntity<>(service.create(request), HttpStatus.CREATED);
    }

    /**
     * @param request is  the model of input for update SensitiveEvents entity
     * @return SensitiveEventsDTO.Info is the updated SensitiveEvents entity
     */
    @PutMapping
    public ResponseEntity<SensitiveEventsDTO.Info> update(@Valid @RequestBody SensitiveEventsDTO.Update request) {
        return new ResponseEntity<>(service.update(request), HttpStatus.OK);
    }

    /**
     * @param id is the SensitiveEvents id for delete
     * @return status code only
     */
    @DeleteMapping(value = {"/{id}"})
    public ResponseEntity<String> delete(@Validated @PathVariable Long id) {
        try {
            service.delete(id);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (DataIntegrityViolationException violationException) {
            final Locale locale = LocaleContextHolder.getLocale();
            throw new EvaluationHandleException(EvaluationHandleException.ErrorType.IntegrityConstraint, null, messageSource.getMessage("exception.integrity.constraint", null, locale));
        } catch (Exception exception) {
            throw new EvaluationHandleException(EvaluationHandleException.ErrorType.NotDeletable);
        }
    }

    /**
     * @param count      is the number of entity to every page
     * @param startIndex is the start Index in current page
     * @param criteria   is the key value pair for criteria
     * @return TotalResponse<SensitiveEventsDTO.Info> is the list of SensitiveEventsInfo entity that match the criteria
     */
    @PostMapping(value = "/spec-list")
    public ResponseEntity<SensitiveEventsDTO.SpecResponse> search(@RequestParam(value = "startIndex", required = false, defaultValue = "0") Integer startIndex,
                                                                  @RequestParam(value = "count", required = false, defaultValue = "30") Integer count,
                                                                  @RequestBody List<FilterDTO> criteria) throws NoSuchFieldException, IllegalAccessException {
        SearchDTO.SearchRq request = CriteriaUtil.ConvertCriteriaToSearchRequest(criteria, count, startIndex);
        SearchDTO.SearchRs<SensitiveEventsDTO.Info> data = service.search(request);
        final SensitiveEventsDTO.Response response = new SensitiveEventsDTO.Response();
        final SensitiveEventsDTO.SpecResponse specRs = new SensitiveEventsDTO.SpecResponse();
        response.setData(data.getList())
                .setStartRow(startIndex)
                .setEndRow(startIndex + data.getList().size())
                .setTotalRows(data.getTotalCount().intValue());
        specRs.setResponse(response);
        return new ResponseEntity<>(specRs, HttpStatus.OK);
    }

}
