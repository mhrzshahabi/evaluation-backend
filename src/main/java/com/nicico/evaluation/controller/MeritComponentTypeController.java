package com.nicico.evaluation.controller;

import com.nicico.copper.common.dto.search.SearchDTO;
import com.nicico.evaluation.dto.FilterDTO;
import com.nicico.evaluation.dto.MeritComponentTypeDTO;
import com.nicico.evaluation.exception.EvaluationHandleException;
import com.nicico.evaluation.iservice.IMeritComponentTypeService;
import com.nicico.evaluation.utility.CriteriaUtil;
import io.swagger.annotations.Api;
import lombok.RequiredArgsConstructor;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Locale;

@RequiredArgsConstructor
@Api(value = "MeritComponent")
@RestController
@RequestMapping(value = "/api/merit-Component-type")
public class MeritComponentTypeController {

    private final IMeritComponentTypeService service;

    /**
     * @param id is the meritComponentType id
     * @return MeritComponentTypeDTO.Info is the single meritComponentType entity
     */
    @GetMapping(value = "/{id}")
    public ResponseEntity<MeritComponentTypeDTO.Info> get(@PathVariable Long id) {
        return new ResponseEntity<>(service.get(id), HttpStatus.OK);
    }

    /**
     * @param count      is the number of entity to every page
     * @param startIndex is the start Index in current page
     * @return MeritComponentTypeDTO.SpecResponse that contain list of MeritComponentTypeDTO and the number of total entity
     */
    @GetMapping(value = "/list")
    public ResponseEntity<MeritComponentTypeDTO.SpecResponse> list(@RequestParam int count, @RequestParam int startIndex) {
        return new ResponseEntity<>(service.list(count, startIndex), HttpStatus.OK);
    }

    /**
     * @param request is the model of input for create meritComponentType entity
     * @return MeritComponentTypeDTO.Info is the saved meritComponentType entity
     */
    @PostMapping
    public ResponseEntity<MeritComponentTypeDTO.Info> create(@Valid @RequestBody MeritComponentTypeDTO.Create request) {
        return new ResponseEntity<>(service.create(request), HttpStatus.CREATED);
    }

    /**
     * @param request is  the model of input for update meritComponentType entity
     * @return MeritComponentTypeDTO.Info is the updated meritComponentType entity
     */
    @PutMapping
    public ResponseEntity<MeritComponentTypeDTO.Info> update(@Valid @RequestBody MeritComponentTypeDTO.Update request) {
        return new ResponseEntity<>(service.update(request), HttpStatus.OK);
    }

    /**
     * @param id is the meritComponentType id for delete
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
     * @return TotalResponse<MeritComponentTypeDTO.Info> is the list of groupInfo entity that match the criteria
     */
    @PostMapping(value = "/spec-list")
    public ResponseEntity<MeritComponentTypeDTO.SpecResponse> search(@RequestParam(value = "startIndex", required = false, defaultValue = "0") Integer startIndex,
                                                                     @RequestParam(value = "count", required = false, defaultValue = "30") Integer count,
                                                                     @RequestBody List<FilterDTO> criteria) throws NoSuchFieldException, IllegalAccessException {
        SearchDTO.SearchRq request = CriteriaUtil.ConvertCriteriaToSearchRequest(criteria, count, startIndex);
        SearchDTO.SearchRs<MeritComponentTypeDTO.Info> data = service.search(request);
        final MeritComponentTypeDTO.Response response = new MeritComponentTypeDTO.Response();
        final MeritComponentTypeDTO.SpecResponse specRs = new MeritComponentTypeDTO.SpecResponse();
        response.setData(data.getList())
                .setStartRow(startIndex)
                .setEndRow(startIndex + data.getList().size())
                .setTotalRows(data.getTotalCount().intValue());
        specRs.setResponse(response);
        return new ResponseEntity<>(specRs, HttpStatus.OK);
    }

}
