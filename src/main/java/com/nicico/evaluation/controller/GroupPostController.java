package com.nicico.evaluation.controller;

import com.nicico.copper.common.dto.search.SearchDTO;
import com.nicico.evaluation.dto.FilterDTO;
import com.nicico.evaluation.dto.GroupPostDTO;
import com.nicico.evaluation.iservice.IGroupPostService;
import com.nicico.evaluation.service.ExecutorService;
import com.nicico.evaluation.utility.BaseResponse;
import com.nicico.evaluation.utility.CriteriaUtil;
import io.swagger.annotations.Api;
import lombok.RequiredArgsConstructor;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@Api(value = "Group Post")
@RestController
@RequestMapping(value = "/api/group-post")
public class GroupPostController {

    private final IGroupPostService service;
    private final ExecutorService executorService;
    private final ResourceBundleMessageSource messageSource;

    @GetMapping(value = "/{id}")
    public ResponseEntity<GroupPostDTO.Info> get(@PathVariable Long id) {
        return new ResponseEntity<>(service.get(id), HttpStatus.OK);
    }

    @GetMapping(value = "/list")
    public ResponseEntity<GroupPostDTO.SpecResponse> list(@RequestParam int count, @RequestParam int startIndex) {
        return new ResponseEntity<>(service.list(count, startIndex), HttpStatus.OK);
    }

    /**
     * @param count      is the number of entity to every page
     * @param startIndex is the start Index in current page
     * @param criteria   is the key value pair for criteria
     * @return TotalResponse<GroupPostDTO.Info> is the list of groupPostInfo entity that match the criteria
     */
    @PostMapping(value = "/spec-list")
    public ResponseEntity<GroupPostDTO.SpecResponse> search(@RequestParam(value = "startIndex", required = false, defaultValue = "0") Integer startIndex,
                                                            @RequestParam(value = "count", required = false, defaultValue = "30") Integer count,
                                                            @RequestBody List<FilterDTO> criteria) throws NoSuchFieldException, IllegalAccessException {
        SearchDTO.SearchRq request = CriteriaUtil.ConvertCriteriaToSearchRequest(criteria, count, startIndex);
        SearchDTO.SearchRs<GroupPostDTO.Info> data = service.search(request);
        final GroupPostDTO.Response response = new GroupPostDTO.Response();
        final GroupPostDTO.SpecResponse specRs = new GroupPostDTO.SpecResponse();
        response.setData(data.getList())
                .setStartRow(startIndex)
                .setEndRow(startIndex + data.getList().size())
                .setTotalRows(data.getTotalCount().intValue());
        specRs.setResponse(response);
        return new ResponseEntity<>(specRs, HttpStatus.OK);
    }

    /**
     * @param criteria is the key value pair for criteria
     * @return byte[] is the Excel of GroupPostInfo entity that match the criteria
     */
    @PostMapping(value = "/export-excel")
    public ResponseEntity<BaseResponse> exportExcelAsync(@RequestBody List<FilterDTO> criteria) {
        BaseResponse response = new BaseResponse();
        executorService.runAsync(() -> service.downloadExcel(criteria));
        response.setStatus(HttpStatus.OK.value());
        response.setMessage(messageSource.getMessage("message.successful.async.excel", null, LocaleContextHolder.getLocale()));
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
