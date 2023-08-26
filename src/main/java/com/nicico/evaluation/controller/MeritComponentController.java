package com.nicico.evaluation.controller;

import com.nicico.copper.common.dto.search.SearchDTO;
import com.nicico.evaluation.dto.FilterDTO;
import com.nicico.evaluation.dto.MeritComponentDTO;
import com.nicico.evaluation.exception.EvaluationHandleException;
import com.nicico.evaluation.iservice.IMeritComponentService;
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
@Api(value = "MeritComponent")
@RestController
@RequestMapping(value = "/api/merit-Component")
public class MeritComponentController {

    private final ExceptionUtil exceptionUtil;
    private final IMeritComponentService service;
    private final ResourceBundleMessageSource messageSource;

    /**
     * @param id is the merit component id
     * @return MeritComponentDTO.Info is the single meritComponent entity
     */
    @GetMapping(value = "/{id}")
    public ResponseEntity<MeritComponentDTO.Info> get(@PathVariable Long id) {
        return new ResponseEntity<>(service.get(id), HttpStatus.OK);
    }

    /**
     * @param id is the merit component id
     * @return MeritComponentDTO.Info is the single meritComponent entity
     */
    @GetMapping(value = "/last-active/{id}")
    public ResponseEntity<MeritComponentDTO.Info> findLastActiveByMeritComponentId(@PathVariable Long id) {
        return new ResponseEntity<>(service.findLastActiveByMeritComponentId(id), HttpStatus.OK);
    }

    /**
     * @param count      is the number of entity to every page
     * @param startIndex is the start Index in current page
     * @return MeritComponentDTO.SpecResponse that contain list of MeritComponentDTO and the number of total entity
     */
    @GetMapping(value = "/list")
    public ResponseEntity<MeritComponentDTO.SpecResponse> list(@RequestParam int count, @RequestParam int startIndex) {
        return new ResponseEntity<>(service.list(count, startIndex), HttpStatus.OK);
    }

    /**
     * @param request is the model of input for create meritComponent entity
     * @return MeritComponentDTO.Info is the saved meritComponent entity
     */
    @PostMapping
    public ResponseEntity<MeritComponentDTO.Info> create(@Valid @RequestBody MeritComponentDTO.Create request) {
        try {
            return new ResponseEntity<>(service.create(request), HttpStatus.CREATED);
        } catch (Exception exception) {
            throw new EvaluationHandleException(EvaluationHandleException.ErrorType.NotSave);
        }
    }

    /**
     * @param request is  the model of input for update meritComponent entity
     * @return MeritComponentDTO.Info is the updated meritComponent entity
     */
    @PutMapping(value = "/{id}")
    public ResponseEntity<MeritComponentDTO.Info> update(@PathVariable Long id, @Valid @RequestBody MeritComponentDTO.Update request) {
        return new ResponseEntity<>(service.update(id, request), HttpStatus.OK);
    }

    /**
     * @param request is  the model of input for change status MeritComponent entity
     * @return MeritComponentDTO.Info is the updated MeritComponentDTO entity
     */
    @PutMapping(value = "/change-status/{id}")
    public ResponseEntity<MeritComponentDTO.Info> changeStatus(@PathVariable Long id, @Valid @RequestBody MeritComponentDTO.ChangeStatus request) {
        return new ResponseEntity<>(service.changeStatus(id, request), HttpStatus.OK);
    }

    /**
     * @param id is the meritComponent id for delete
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
     * @return TotalResponse<MeritComponentDTO.Info> is the list of groupInfo entity that match the criteria
     */
    @PostMapping(value = "/spec-list")
    public ResponseEntity<MeritComponentDTO.SpecResponse> search(@RequestParam(value = "startIndex", required = false, defaultValue = "0") Integer startIndex,
                                                                 @RequestParam(value = "count", required = false, defaultValue = "30") Integer count,
                                                                 @RequestBody List<FilterDTO> criteria) throws NoSuchFieldException, IllegalAccessException {
        SearchDTO.SearchRq request = CriteriaUtil.ConvertCriteriaToSearchRequest(criteria, count, startIndex);
        request.setSortBy("-code");
        SearchDTO.SearchRs<MeritComponentDTO.Info> data = service.search(request);
        final MeritComponentDTO.Response response = new MeritComponentDTO.Response();
        final MeritComponentDTO.SpecResponse specRs = new MeritComponentDTO.SpecResponse();
        response.setData(data.getList())
                .setStartRow(startIndex)
                .setEndRow(startIndex + data.getList().size())
                .setTotalRows(data.getTotalCount().intValue());
        specRs.setResponse(response);
        return new ResponseEntity<>(specRs, HttpStatus.OK);
    }

    /**
     * @param criteria is the key value pair for criteria
     * @return byte[] is the Excel of MeritComponentInfo entity that match the criteria
     */
    @PostMapping(value = "/export-excel")
    public ResponseEntity<byte[]> exportExcel(@RequestBody List<FilterDTO> criteria) throws NoSuchFieldException, IllegalAccessException {
        ExcelGenerator.ExcelDownload excelDownload = service.downloadExcel(criteria);
        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(excelDownload.getContentType()))
                .header(HttpHeaders.CONTENT_DISPOSITION, excelDownload.getHeaderValue())
                .body(excelDownload.getContent());
    }

    @GetMapping(value = "/update-merit-to-audit")
    public ResponseEntity<Void> updateMeritToAudit() {
        service.updateMeritToAudit();
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
