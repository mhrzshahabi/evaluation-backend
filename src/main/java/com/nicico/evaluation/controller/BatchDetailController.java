package com.nicico.evaluation.controller;

import com.nicico.copper.common.dto.search.SearchDTO;
import com.nicico.evaluation.dto.BatchDetailDTO;
import com.nicico.evaluation.dto.FilterDTO;
import com.nicico.evaluation.exception.EvaluationHandleException;
import com.nicico.evaluation.iservice.IBatchDetailService;
import com.nicico.evaluation.utility.BaseResponse;
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
@Api(value = "Batch Detail")
@RestController
@RequestMapping(value = "/api/batch-detail")
public class BatchDetailController {

    private final IBatchDetailService service;
    private final ResourceBundleMessageSource messageSource;
    private final ExceptionUtil exceptionUtil;
    /**
     * @param id is the batchDetail id
     * @return BatchDetailDTO.Info is the single batchDetail entity
     */
    @GetMapping(value = "/{id}")
    public ResponseEntity<BatchDetailDTO.Info> get(@PathVariable Long id) {
        return new ResponseEntity<>(service.get(id), HttpStatus.OK);
    }

    /**
     * @param count      is the number of entity to every page
     * @param startIndex is the start Index in current page
     * @return BatchDetailDTO.SpecResponse that contain list of BatchDetailDTO and the number of total entity
     */
    @GetMapping(value = "/list")
    public ResponseEntity<BatchDetailDTO.SpecResponse> list(@RequestParam int count, @RequestParam int startIndex) {
        return new ResponseEntity<>(service.list(count, startIndex), HttpStatus.OK);
    }

    /**
     * @param request is the model of input for create batchDetail entity
     * @return BatchDetailDTO.Info is the saved batchDetail entity
     */
    @PostMapping
    public ResponseEntity<BaseResponse> create(@Valid @RequestBody BatchDetailDTO.CreateList request) {
        return new ResponseEntity<>(service.create(request), HttpStatus.CREATED);
    }

    /**
     * @param request is  the model of input for update batchDetail entity
     * @return BatchDetailDTO.Info is the updated batchDetail entity
     */
    @PutMapping
    public ResponseEntity<BatchDetailDTO.Info> update(@Valid @RequestBody BatchDetailDTO.Update request) {
        return new ResponseEntity<>(service.update(request), HttpStatus.OK);
    }

    /**
     * @param id is the batchDetail id for delete
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
     * @return TotalResponse<BatchDetailDTO.Info> is the list of batchDetail entity that match the criteria
     */
    @PostMapping(value = "/spec-list")
    public ResponseEntity<BatchDetailDTO.SpecResponse> search(@RequestParam(value = "startIndex", required = false, defaultValue = "0") Integer startIndex,
                                                          @RequestParam(value = "count", required = false, defaultValue = "30") Integer count,
                                                          @RequestBody List<FilterDTO> criteria) throws NoSuchFieldException, IllegalAccessException {
        SearchDTO.SearchRq request = CriteriaUtil.ConvertCriteriaToSearchRequest(criteria, count, startIndex);
        SearchDTO.SearchRs<BatchDetailDTO.Info> data = service.search(request);
        final BatchDetailDTO.Response response = new BatchDetailDTO.Response();
        final BatchDetailDTO.SpecResponse specRs = new BatchDetailDTO.SpecResponse();
        response.setData(data.getList())
                .setStartRow(startIndex)
                .setEndRow(startIndex + data.getList().size())
                .setTotalRows(data.getTotalCount().intValue());
        specRs.setResponse(response);
        return new ResponseEntity<>(specRs, HttpStatus.OK);
    }

}
