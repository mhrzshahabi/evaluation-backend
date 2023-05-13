package com.nicico.evaluation.controller;

import com.nicico.copper.common.dto.search.SearchDTO;
import com.nicico.evaluation.dto.FilterDTO;
import com.nicico.evaluation.dto.InstanceGroupTypeMeritDTO;
import com.nicico.evaluation.exception.EvaluationHandleException;
import com.nicico.evaluation.iservice.IInstanceGroupTypeMeritService;
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
@Api(value = "Instance Group Type Merit")
@RestController
@RequestMapping(value = "/api/instance-group-type-merit")
public class InstanceGroupTypeMeritController {

    private final IInstanceGroupTypeMeritService service;
    private final ResourceBundleMessageSource messageSource;
    private final ExceptionUtil exceptionUtil;
    /**
     * @param id is the instanceGroupTypeMerit id
     * @return InstanceGroupTypeMeritDTO.Info is the single instanceGroupTypeMerit entity
     */
    @GetMapping(value = "/{id}")
    public ResponseEntity<InstanceGroupTypeMeritDTO.Info> get(@PathVariable Long id) {
        return new ResponseEntity<>(service.get(id), HttpStatus.OK);
    }

    /**
     * @param id is the groupTypeMerit id
     * @return InstanceGroupTypeMeritDTO.InstanceInfo is the list of instances entity
     */
    @GetMapping(value = "/instances/{id}")
    public ResponseEntity<List<InstanceGroupTypeMeritDTO.InstanceInfo>> getAllInstanceByGroupTypeMeritId(@PathVariable Long id) {
        return new ResponseEntity<>(service.getAllInstanceByGroupTypeMeritId(id), HttpStatus.OK);
    }

    /**
     * @param count      is the number of entity to every page
     * @param startIndex is the start Index in current page
     * @return InstanceGroupTypeMeritDTO.SpecResponse that contain list of InstanceGroupTypeMeritDTO and the number of total entity
     */
    @GetMapping(value = "/list")
    public ResponseEntity<InstanceGroupTypeMeritDTO.SpecResponse> list(@RequestParam int count, @RequestParam int startIndex) {
        return new ResponseEntity<>(service.list(count, startIndex), HttpStatus.OK);
    }

    /**
     * @param request is the model of input for create instanceGroupTypeMerit entity
     * @return InstanceGroupTypeMeritDTO.Info is the saved instanceGroupTypeMerit entity
     */
    @PostMapping
    public ResponseEntity<InstanceGroupTypeMeritDTO.Info> create(@Valid @RequestBody InstanceGroupTypeMeritDTO.Create request) {
        return new ResponseEntity<>(service.create(request), HttpStatus.CREATED);
    }

    /**
     * @param request is  the model of input for update instanceGroupTypeMerit entity
     * @return InstanceGroupTypeMeritDTO.Info is the updated instanceGroupTypeMerit entity
     */
    @PutMapping(value = "/{id}")
    public ResponseEntity<InstanceGroupTypeMeritDTO.Info> update(@PathVariable Long id, @Valid @RequestBody InstanceGroupTypeMeritDTO.Update request) {
        return new ResponseEntity<>(service.update(id, request), HttpStatus.OK);
    }

    /**
     * @param id is the instanceGroupTypeMerit id for delete
     * @return status code only
     */
    @DeleteMapping(value = {"/{id}"})
    public ResponseEntity<BaseResponse> delete(@Validated @PathVariable Long id) {
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
     * @param count      is the number of entity to every page
     * @param startIndex is the start Index in current page
     * @param criteria   is the key value pair for criteria
     * @return TotalResponse<InstanceGroupTypeMeritDTO.Info> is the list of groupInfo entity that match the criteria
     */
    @PostMapping(value = "/spec-list")
    public ResponseEntity<InstanceGroupTypeMeritDTO.SpecResponse> search(@RequestParam(value = "startIndex", required = false, defaultValue = "0") Integer startIndex,
                                                                         @RequestParam(value = "count", required = false, defaultValue = "30") Integer count,
                                                                         @RequestBody List<FilterDTO> criteria) throws NoSuchFieldException, IllegalAccessException {
        SearchDTO.SearchRq request = CriteriaUtil.ConvertCriteriaToSearchRequest(criteria, count, startIndex);
        SearchDTO.SearchRs<InstanceGroupTypeMeritDTO.Info> data = service.search(request);
        final InstanceGroupTypeMeritDTO.Response response = new InstanceGroupTypeMeritDTO.Response();
        final InstanceGroupTypeMeritDTO.SpecResponse specRs = new InstanceGroupTypeMeritDTO.SpecResponse();
        response.setData(data.getList())
                .setStartRow(startIndex)
                .setEndRow(startIndex + data.getList().size())
                .setTotalRows(data.getTotalCount().intValue());
        specRs.setResponse(response);
        return new ResponseEntity<>(specRs, HttpStatus.OK);
    }

}
