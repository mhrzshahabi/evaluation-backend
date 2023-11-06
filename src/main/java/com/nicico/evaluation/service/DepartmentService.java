package com.nicico.evaluation.service;

import com.nicico.evaluation.common.PageableMapper;
import com.nicico.evaluation.dto.DepartmentDTO;
import com.nicico.evaluation.iservice.IDepartmentService;
import com.nicico.evaluation.repository.DepartmentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RequiredArgsConstructor
@Service
public class DepartmentService implements IDepartmentService {

    private final PageableMapper pageableMapper;
    private final DepartmentRepository repository;

    @Override
    @Transactional(readOnly = true)
    public DepartmentDTO.SpecResponse mojtameList(int startIndex, int count) {
        final Pageable pageable = pageableMapper.toPageable(count, startIndex);
        DepartmentDTO.Response response = new DepartmentDTO.Response();
        DepartmentDTO.SpecResponse specResponse = new DepartmentDTO.SpecResponse();
        List<String> data = repository.findMojtameList(pageable.getPageNumber() * pageable.getPageSize(), pageable.getPageSize());
        Integer total = repository.findAllMojtameCount();

        if (data != null) {
            response.setData(data)
                    .setStartRow(startIndex)
                    .setEndRow(startIndex + count)
                    .setTotalRows(total);
            specResponse.setResponse(response);
        }
        return specResponse;
    }

    @Override
    @Transactional(readOnly = true)
    public DepartmentDTO.SpecResponse moavenatList(int startIndex, int count) {
        final Pageable pageable = pageableMapper.toPageable(count, startIndex);
        DepartmentDTO.Response response = new DepartmentDTO.Response();
        DepartmentDTO.SpecResponse specResponse = new DepartmentDTO.SpecResponse();
        List<String> data = repository.findMoavenatList(pageable.getPageNumber() * pageable.getPageSize(), pageable.getPageSize());
        Integer total = repository.findAllMoavenatCount();

        if (data != null) {
            response.setData(data)
                    .setStartRow(startIndex)
                    .setEndRow(startIndex + count)
                    .setTotalRows(total);
            specResponse.setResponse(response);
        }
        return specResponse;
    }

    @Override
    @Transactional(readOnly = true)
    public DepartmentDTO.SpecResponse omorList(int startIndex, int count) {
        final Pageable pageable = pageableMapper.toPageable(count, startIndex);
        DepartmentDTO.Response response = new DepartmentDTO.Response();
        DepartmentDTO.SpecResponse specResponse = new DepartmentDTO.SpecResponse();
        List<String> data = repository.findOmorList(pageable.getPageNumber() * pageable.getPageSize(), pageable.getPageSize());
        Integer total = repository.findAllOmorCount();

        if (data != null) {
            response.setData(data)
                    .setStartRow(startIndex)
                    .setEndRow(startIndex + data.size())
                    .setTotalRows(total);
            specResponse.setResponse(response);
        }
        return specResponse;
    }

    @Override
    @Transactional(readOnly = true)
    public DepartmentDTO.SpecResponse ghesmatList(int startIndex, int count) {
        final Pageable pageable = pageableMapper.toPageable(count, startIndex);
        DepartmentDTO.Response response = new DepartmentDTO.Response();
        DepartmentDTO.SpecResponse specResponse = new DepartmentDTO.SpecResponse();
        List<String> data = repository.findGhesmatList(pageable.getPageNumber() * pageable.getPageSize(), pageable.getPageSize());
        Integer total = repository.findAllGhesmatCount();

        if (data != null) {
            response.setData(data)
                    .setStartRow(startIndex)
                    .setEndRow(startIndex + count)
                    .setTotalRows(total);
            specResponse.setResponse(response);
        }
        return specResponse;
    }
}
