package com.nicico.evaluation.controller;

import com.nicico.copper.common.dto.search.SearchDTO;
import com.nicico.evaluation.dto.AttachmentDTO;
import com.nicico.evaluation.dto.FilterDTO;
import com.nicico.evaluation.exception.EvaluationHandleException;
import com.nicico.evaluation.iservice.IAttachmentService;
import com.nicico.evaluation.utility.CriteriaUtil;
import com.nicico.evaluation.utility.ExceptionUtil;
import io.swagger.annotations.Api;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
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
@Api(value = "Attachment")
@RestController
@RequestMapping(value = "/api/attachment")
public class AttachmentController {


    private String fmsUrl;
    @Value("${ui.landing.fmsGroupId}")
    private String fmsGroupId;
    private final IAttachmentService service;
    private final ResourceBundleMessageSource messageSource;
    private final ExceptionUtil exceptionUtil;

    /**
     * @param count      is the number of entity to every page
     * @param startIndex is the start Index in current page
     * @return AttachmentDTO.SpecResponse that contain list of attachmentInfoDto and the number of total entity
     */
    @GetMapping(value = "/list")
    public ResponseEntity<AttachmentDTO.SpecResponse> list(@RequestParam int count, @RequestParam int startIndex) {
        return new ResponseEntity<>(service.list(count, startIndex), HttpStatus.OK);
    }

    /**
     * @param id is the attachment id
     * @return AttachmentDTO.Info  is the single attachment entity
     */
    @GetMapping(value = "/{id}")
    public ResponseEntity<AttachmentDTO.Info> get(@PathVariable Long id) {
        try {
            return new ResponseEntity<>(service.get(id), HttpStatus.OK);
        } catch (Exception e) {
            throw new EvaluationHandleException(EvaluationHandleException.ErrorType.NotFound);
        }
    }

    /**
     * @param request is the model of input for create attachment entity
     * @return AttachmentDTOInfo is the saved attachment entity
     */
    @PostMapping
    public ResponseEntity<AttachmentDTO.Info> create(@Valid @RequestBody AttachmentDTO.Create request) {
        return new ResponseEntity<>(service.create(request), HttpStatus.CREATED);
    }

    /**
     * @param request is  the model of input for update attachment entity
     * @return AttachmentDTOInfo is the updated attachment entity
     */
    @PutMapping
    public ResponseEntity<AttachmentDTO.Info> update(@Valid @RequestBody AttachmentDTO.Update request) {
        return new ResponseEntity<>(service.update(request), HttpStatus.OK);
    }

    /**
     * @param id is the attachment id for delete
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
     * @return TotalResponse<AttachmentDTO.Info> is the list of attachmentInfo entity that match the criteria
     */
    @PostMapping(value = "/spec-list")
    public ResponseEntity<AttachmentDTO.SpecResponse> search(@RequestParam(value = "startIndex", required = false, defaultValue = "0") Integer startIndex,
                                                             @RequestParam(value = "count", required = false, defaultValue = "30") Integer count,
                                                             @RequestBody List<FilterDTO> criteria) throws NoSuchFieldException, IllegalAccessException {
        SearchDTO.SearchRq request = CriteriaUtil.ConvertCriteriaToSearchRequest(criteria, count, startIndex);
        SearchDTO.SearchRs<AttachmentDTO.Info> data = service.search(request);
        final AttachmentDTO.Response response = new AttachmentDTO.Response();
        final AttachmentDTO.SpecResponse specRs = new AttachmentDTO.SpecResponse();
        response.setData(data.getList())
                .setStartRow(startIndex)
                .setEndRow(startIndex + data.getList().size())
                .setTotalRows(data.getTotalCount().intValue());
        specRs.setResponse(response);
        return new ResponseEntity<>(specRs, HttpStatus.OK);
    }

    /**
     * @return fms url and fms groupId
     */
    @GetMapping(value = "/getFmsInfo")
    public ResponseEntity<AttachmentDTO.FmsInfo> getFmsInfo() {
        AttachmentDTO.FmsInfo fmsInfo = new AttachmentDTO.FmsInfo();
        fmsInfo.setFmsUrl(fmsUrl).setFmsGroupId(fmsGroupId);
        return new ResponseEntity<>(fmsInfo, HttpStatus.OK);
    }

}
