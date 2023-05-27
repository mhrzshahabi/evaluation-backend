package com.nicico.evaluation.controller;

import com.nicico.copper.common.dto.search.EOperator;
import com.nicico.copper.common.dto.search.SearchDTO;
import com.nicico.evaluation.dto.EvaluationPeriodPostDTO;
import com.nicico.evaluation.dto.FilterDTO;
import com.nicico.evaluation.dto.PostRelationDTO;
import com.nicico.evaluation.iservice.IEvaluationPeriodPostService;
import com.nicico.evaluation.iservice.IEvaluationService;
import com.nicico.evaluation.iservice.IPostRelationService;
import com.nicico.evaluation.utility.CriteriaUtil;
import io.swagger.annotations.Api;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Api(value = "Post Relation")
@RestController
@RequestMapping(value = "/api/post-relation")
public class PostRelationController {

    private final IPostRelationService service;
    private final IEvaluationPeriodPostService evaluationPeriodPostService;
    private final IEvaluationService iEvaluationService;

    @GetMapping(value = "/{id}")
    public ResponseEntity<PostRelationDTO.Info> get(@PathVariable Long id) {
        return new ResponseEntity<>(service.get(id), HttpStatus.OK);
    }

    @GetMapping(value = "/list")
    public ResponseEntity<PostRelationDTO.SpecResponse> list(@RequestParam int count, @RequestParam int startIndex) {
        return new ResponseEntity<>(service.list(count, startIndex), HttpStatus.OK);
    }

    @PostMapping(value = "/spec-list")
    public ResponseEntity<PostRelationDTO.SpecResponse> search(@RequestParam(value = "startIndex", required = false, defaultValue = "0") Integer startIndex,
                                                               @RequestParam(value = "count", required = false, defaultValue = "30") Integer count,
                                                               @RequestBody List<FilterDTO> criteria) throws NoSuchFieldException, IllegalAccessException {
        SearchDTO.SearchRq request = CriteriaUtil.ConvertCriteriaToSearchRequest(criteria, count, startIndex);
        SearchDTO.SearchRs<PostRelationDTO.Info> data = service.search(request);
        final PostRelationDTO.Response response = new PostRelationDTO.Response();
        final PostRelationDTO.SpecResponse specRs = new PostRelationDTO.SpecResponse();
        response.setData(data.getList())
                .setStartRow(startIndex)
                .setEndRow(startIndex + data.getList().size())
                .setTotalRows(data.getTotalCount().intValue());
        specRs.setResponse(response);
        return new ResponseEntity<>(specRs, HttpStatus.OK);
    }

    @PostMapping(value = "/spec-list/byEvaluationPeriodId")
    public ResponseEntity<PostRelationDTO.SpecResponse> searchByEvaluationPeriodId(@RequestParam(value = "startIndex", required = false, defaultValue = "0") Integer startIndex,
                                                                                   @RequestParam(value = "count", required = false, defaultValue = "30") Integer count,
                                                                                   @RequestParam(value = "evaluationPeriodId") Long evaluationPeriodId,
                                                                                   @RequestBody List<FilterDTO> criteria) throws NoSuchFieldException, IllegalAccessException {

        List<String> postCodes = evaluationPeriodPostService.getAllByEvaluationPeriodId(evaluationPeriodId).stream().map(EvaluationPeriodPostDTO.PostInfoEvaluationPeriod::getPostCode).toList();

        SearchDTO.SearchRq request = CriteriaUtil.ConvertCriteriaToSearchRequest(criteria, count, startIndex);
        final List<SearchDTO.CriteriaRq> criteriaRqList = new ArrayList<>();
        final SearchDTO.CriteriaRq postCodeCriteriaRq = new SearchDTO.CriteriaRq()
                .setOperator(EOperator.inSet)
                .setFieldName("postCode")
                .setValue(postCodes);

        criteriaRqList.add(postCodeCriteriaRq);
        criteriaRqList.add(request.getCriteria());

        final SearchDTO.CriteriaRq criteriaRq = new SearchDTO.CriteriaRq()
                .setOperator(EOperator.and)
                .setCriteria(criteriaRqList);
        request.setCriteria(criteriaRq);

        SearchDTO.SearchRs<PostRelationDTO.Info> data = service.search(request);
        final PostRelationDTO.Response response = new PostRelationDTO.Response();
        final PostRelationDTO.SpecResponse specRs = new PostRelationDTO.SpecResponse();
        response.setData(data.getList())
                .setStartRow(startIndex)
                .setEndRow(startIndex + data.getList().size())
                .setTotalRows(data.getTotalCount().intValue());
        specRs.setResponse(response);
        return new ResponseEntity<>(specRs, HttpStatus.OK);
    }

    @PostMapping(value = "/spec-list/not-used/byEvaluationPeriodId")
    public ResponseEntity<PostRelationDTO.SpecResponse> searchByEvaluationPeriodIdAndNotInEvaluation(@RequestParam(value = "startIndex", required = false, defaultValue = "0") Integer startIndex,
                                                                                   @RequestParam(value = "count", required = false, defaultValue = "30") Integer count,
                                                                                   @RequestParam(value = "evaluationPeriodId") Long evaluationPeriodId,
                                                                                   @RequestBody List<FilterDTO> criteria) throws NoSuchFieldException, IllegalAccessException {

        List<String> postCodes = evaluationPeriodPostService.getAllByEvaluationPeriodId(evaluationPeriodId).stream().map(EvaluationPeriodPostDTO.PostInfoEvaluationPeriod::getPostCode).toList();
        List<String> usedPost=iEvaluationService.getUsedPostInEvaluation(evaluationPeriodId);
     if (!usedPost.isEmpty())
        postCodes.removeAll(usedPost);
        SearchDTO.SearchRq request = CriteriaUtil.ConvertCriteriaToSearchRequest(criteria, count, startIndex);
        final List<SearchDTO.CriteriaRq> criteriaRqList = new ArrayList<>();
        final SearchDTO.CriteriaRq postCodeCriteriaRq = new SearchDTO.CriteriaRq()
                .setOperator(EOperator.inSet)
                .setFieldName("postCode")
                .setValue(postCodes);

        criteriaRqList.add(postCodeCriteriaRq);
        criteriaRqList.add(request.getCriteria());

        final SearchDTO.CriteriaRq criteriaRq = new SearchDTO.CriteriaRq()
                .setOperator(EOperator.and)
                .setCriteria(criteriaRqList);
        request.setCriteria(criteriaRq);

        SearchDTO.SearchRs<PostRelationDTO.Info> data = service.search(request);
        final PostRelationDTO.Response response = new PostRelationDTO.Response();
        final PostRelationDTO.SpecResponse specRs = new PostRelationDTO.SpecResponse();
        response.setData(data.getList())
                .setStartRow(startIndex)
                .setEndRow(startIndex + data.getList().size())
                .setTotalRows(data.getTotalCount().intValue());
        specRs.setResponse(response);
        return new ResponseEntity<>(specRs, HttpStatus.OK);
    }
}
