
package com.nicico.evaluation.controller;

import com.nicico.copper.common.dto.search.SearchDTO;
import com.nicico.evaluation.dto.FilterDTO;
import com.nicico.evaluation.exception.EvaluationHandleException;
import com.nicico.evaluation.dto.SpecialCaseDTO;
import com.nicico.evaluation.iservice.ISpecialCaseService;
import com.nicico.evaluation.utility.CriteriaUtil;
import io.swagger.annotations.Api;
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
@RequestMapping("/api/special-case")
@Api("SpecialCase Api")
@Validated
@AllArgsConstructor
public class SpecialCaseController {

    private final ISpecialCaseService specialCaseService;
    private final ResourceBundleMessageSource messageSource;

    /**
     * @param id is the specialCase id
     * @return SpecialCaseDTO.Info  is the single specialCase entity
     */
    @GetMapping(value = "/{id}")
    public ResponseEntity<SpecialCaseDTO.Info> get(@PathVariable Long id) {
        return new ResponseEntity<>(specialCaseService.get(id), HttpStatus.OK);
    }

    /**
     * @param count      is the number of entity to every page
     * @param startIndex is the start Index in current page
     * @return SpecialCaseDTO.SpecResponse that contain list of SpecialCaseInfo and the number of total entity
     */
    @GetMapping(value = "/list")
    public ResponseEntity<SpecialCaseDTO.SpecResponse> list(@RequestParam int count, @RequestParam int startIndex) {
        return new ResponseEntity<>(specialCaseService.list(count, startIndex), HttpStatus.OK);
    }

    /**
     * @param request is the model of input for create specialCase entity
     * @return SpecialCaseDTOInfo is the saved specialCase entity
     */
    @PostMapping
    public ResponseEntity<SpecialCaseDTO.Info> create(@Valid @RequestBody SpecialCaseDTO.Create request) {
        return new ResponseEntity<>(specialCaseService.create(request), HttpStatus.CREATED);
    }

    /**
     * @param request is  the model of input for update specialCase entity
     * @return SpecialCaseDTOInfo is the updated specialCase entity
     */
    @PutMapping
    public ResponseEntity<SpecialCaseDTO.Info> update(@Valid @RequestBody SpecialCaseDTO.Update request) {
        return new ResponseEntity<>(specialCaseService.update(request), HttpStatus.OK);
    }

    /**
     * @param id is the specialCase id for delete
     * @return status code only
     */
    @DeleteMapping(value = "/{id}")
    public ResponseEntity<String> delete(@Validated @PathVariable Long id) {
        try {
            specialCaseService.delete(id);
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
     * @return TotalResponse<SpecialCaseDTO.Info> is the list of SpecialCaseInfo entity that match the criteria
     */
    @PostMapping(value = "/spec-list")
    public ResponseEntity<SpecialCaseDTO.SpecResponse> search(@RequestParam(value = "startIndex", required = false, defaultValue = "0") Integer startIndex,
                                                              @RequestParam(value = "count", required = false, defaultValue = "30") Integer count,
                                                              @RequestBody List<FilterDTO> criteria) throws NoSuchFieldException, IllegalAccessException {
        SearchDTO.SearchRq request = CriteriaUtil.ConvertCriteriaToSearchRequest(criteria, count, startIndex);
        SearchDTO.SearchRs<SpecialCaseDTO.Info> data = specialCaseService.search(request);
        final SpecialCaseDTO.Response response = new SpecialCaseDTO.Response();
        final SpecialCaseDTO.SpecResponse specRs = new SpecialCaseDTO.SpecResponse();
        response.setData(data.getList())
                .setStartRow(startIndex)
                .setEndRow(startIndex + data.getList().size())
                .setTotalRows(data.getTotalCount().intValue());
        specRs.setResponse(response);
        return new ResponseEntity<>(specRs, HttpStatus.OK);
    }

}

    