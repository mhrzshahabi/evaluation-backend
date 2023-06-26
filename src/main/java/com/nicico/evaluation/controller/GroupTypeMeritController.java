package com.nicico.evaluation.controller;

import com.nicico.copper.common.dto.search.SearchDTO;
import com.nicico.evaluation.dto.FilterDTO;
import com.nicico.evaluation.dto.GroupTypeMeritDTO;
import com.nicico.evaluation.exception.EvaluationHandleException;
import com.nicico.evaluation.iservice.IGroupTypeMeritService;
import com.nicico.evaluation.utility.BaseResponse;
import com.nicico.evaluation.utility.CriteriaUtil;
import com.nicico.evaluation.utility.ExcelGenerator;
import com.nicico.evaluation.utility.ExceptionUtil;
import io.swagger.annotations.Api;
import lombok.RequiredArgsConstructor;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Locale;

@RequiredArgsConstructor
@Api(value = "Group Type Merit")
@RestController
@RequestMapping(value = "/api/group-type-merit")
public class GroupTypeMeritController {

    private final ResourceBundleMessageSource messageSource;
    private final ExceptionUtil exceptionUtil;
    private final IGroupTypeMeritService service;

    /**
     * @param id is the groupTypeMerit id
     * @return GroupTypeMeritDTO.Info is the single groupTypeMerit entity
     */
    @GetMapping(value = "/{id}")
    public ResponseEntity<GroupTypeMeritDTO.Info> get(@PathVariable Long id) {
        return new ResponseEntity<>(service.get(id), HttpStatus.OK);
    }

    /**
     * @param groupTypeId is the groupType id
     * @return total weight of components by groupTypeId
     */
    @GetMapping(value = "/total-weight/{groupTypeId}")
    public ResponseEntity<Long> getTotalComponentWeightByGroupType(@PathVariable Long groupTypeId) {
        return new ResponseEntity<>(service.getTotalComponentWeightByGroupType(groupTypeId), HttpStatus.OK);
    }

    /**
     * @param count      is the number of entity to every page
     * @param startIndex is the start Index in current page
     * @return GroupTypeMeritDTO.SpecResponse that contain list of GroupTypeMeritDTO and the number of total entity
     */
    @GetMapping(value = "/list")
    public ResponseEntity<GroupTypeMeritDTO.SpecResponse> list(@RequestParam int count, @RequestParam int startIndex) {
        return new ResponseEntity<>(service.list(count, startIndex), HttpStatus.OK);
    }

    /**
     * @param request is the model of input for create groupTypeMerit entity
     * @return GroupTypeMeritDTO.Info is the saved groupTypeMerit entity
     */
    @PostMapping
    public ResponseEntity<GroupTypeMeritDTO.Info> create(@Valid @RequestBody GroupTypeMeritDTO.Create request) {
        return new ResponseEntity<>(service.create(request), HttpStatus.CREATED);
    }

    /**
     * @param request is  the model of input for update groupTypeMerit entity
     * @return GroupTypeMeritDTO.Info is the updated groupTypeMerit entity
     */
    @PutMapping(value = "/{id}")
    public ResponseEntity<GroupTypeMeritDTO.Info> update(@PathVariable Long id, @Valid @RequestBody GroupTypeMeritDTO.Update request) {
        try {
            return new ResponseEntity<>(service.update(id, request), HttpStatus.OK);

        } catch (DataIntegrityViolationException violationException) {
            throw new EvaluationHandleException(EvaluationHandleException.ErrorType.IntegrityConstraint, null,
                    messageSource.getMessage("exception.integrity.constraint.change", null,
                            LocaleContextHolder.getLocale()));
        } catch (Exception exception) {
            throw new EvaluationHandleException(EvaluationHandleException.ErrorType.NotDeletable);
        }
    }

    /**
     * @param id is the groupTypeMerit id for delete
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
     * @return TotalResponse<GroupTypeMeritDTO.Info> is the list of groupInfo entity that match the criteria
     */
    @PostMapping(value = "/spec-list")
    public ResponseEntity<GroupTypeMeritDTO.SpecResponse> search(@RequestParam(value = "startIndex", required = false, defaultValue = "0") Integer startIndex,
                                                                 @RequestParam(value = "count", required = false, defaultValue = "30") Integer count,
                                                                 @RequestBody List<FilterDTO> criteria) throws NoSuchFieldException, IllegalAccessException {
        SearchDTO.SearchRq request = CriteriaUtil.ConvertCriteriaToSearchRequest(criteria, count, startIndex);
        SearchDTO.SearchRs<GroupTypeMeritDTO.Info> data = service.search(request);
        final GroupTypeMeritDTO.Response response = new GroupTypeMeritDTO.Response();
        final GroupTypeMeritDTO.SpecResponse specRs = new GroupTypeMeritDTO.SpecResponse();
        response.setData(data.getList())
                .setStartRow(startIndex)
                .setEndRow(startIndex + data.getList().size())
                .setTotalRows(data.getTotalCount().intValue());
        specRs.setResponse(response);
        return new ResponseEntity<>(specRs, HttpStatus.OK);
    }

    /**
     * @param criteria is the key value pair for criteria
     * @return byte[] is the excel of group-type-meritInfo list that match the criteria
     */
    @PostMapping(value = "/export-excel")
    public ResponseEntity<byte[]> exportExcel(@RequestBody List<FilterDTO> criteria) throws NoSuchFieldException, IllegalAccessException {
        ExcelGenerator.ExcelDownload excelDownload = service.downloadExcel(criteria);
        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(excelDownload.getContentType()))
                .header(HttpHeaders.CONTENT_DISPOSITION, excelDownload.getHeaderValue())
                .body(excelDownload.getContent());
    }

}
