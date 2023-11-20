package com.nicico.evaluation.service;

import com.nicico.copper.common.dto.search.SearchDTO;
import com.nicico.evaluation.dto.EvaluationDTO;
import com.nicico.evaluation.dto.FilterDTO;
import com.nicico.evaluation.iservice.IEvaluationCostCenterReportViewService;
import com.nicico.evaluation.mapper.EvaluationCostCenterReportViewMapper;
import com.nicico.evaluation.repository.EvaluationCostCenterReportViewRepository;
import com.nicico.evaluation.utility.ExcelGenerator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RequiredArgsConstructor
@Slf4j
@Service
public class EvaluationCostCenterReportViewService implements IEvaluationCostCenterReportViewService {

    private final EvaluationCostCenterReportViewMapper mapper;
    private final EvaluationCostCenterReportViewRepository repository;

    @Override
    @Transactional(readOnly = true)
    public SearchDTO.SearchRs<EvaluationDTO.CostCenterInfo> search(SearchDTO.SearchRq request) throws IllegalAccessException, NoSuchFieldException {
        return BaseService.optimizedSearch(repository, mapper::entityToDtoInfo, request);
    }

    @Override
    @Transactional(readOnly = true)
    public ExcelGenerator.ExcelDownload downloadExcel(List<FilterDTO> criteria) throws NoSuchFieldException, IllegalAccessException {
        byte[] body = BaseService.exportExcel(repository, mapper::entityToDtoExcel, criteria, null, "گزارش ارزیابی سازمان");
        return new ExcelGenerator.ExcelDownload(body);
    }
}
