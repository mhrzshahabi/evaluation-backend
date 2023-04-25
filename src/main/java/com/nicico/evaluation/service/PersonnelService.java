package com.nicico.evaluation.service;

import com.nicico.copper.common.dto.search.SearchDTO;
import com.nicico.evaluation.common.PageableMapper;
import com.nicico.evaluation.dto.PersonnelDTO;
import com.nicico.evaluation.exception.EvaluationHandleException;
import com.nicico.evaluation.iservice.IPersonnelService;
import com.nicico.evaluation.mapper.PersonnelMapper;
import com.nicico.evaluation.model.Personnel;
import com.nicico.evaluation.repository.PersonnelRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RequiredArgsConstructor
@Service
public class PersonnelService implements IPersonnelService {

    private final PersonnelMapper mapper;
    private final PersonnelRepository repository;
    private final PageableMapper pageableMapper;

    @Override
    @Transactional(readOnly = true)
    @PreAuthorize("hasAuthority('R_PERSONNEL')")
    public PersonnelDTO.Info get(Long id) {
        Personnel personnel = repository.findById(id).orElseThrow(() -> new EvaluationHandleException(EvaluationHandleException.ErrorType.NotFound));
        return mapper.entityToDtoInfo(personnel);
    }

    @Override
    @Transactional(readOnly = true)
    @PreAuthorize("hasAuthority('R_PERSONNEL')")
    public PersonnelDTO.SpecResponse list(int count, int startIndex) {
        Pageable pageable = pageableMapper.toPageable(count, startIndex);
        Page<Personnel> personals = repository.findAll(pageable);
        List<PersonnelDTO.Info> personnelInfos = mapper.entityToDtoInfoList(personals.getContent());
        PersonnelDTO.Response response = new PersonnelDTO.Response();
        PersonnelDTO.SpecResponse specResponse = new PersonnelDTO.SpecResponse();

        if (personnelInfos != null) {
            response.setData(personnelInfos)
                    .setStartRow(startIndex)
                    .setEndRow(startIndex + count)
                    .setTotalRows((int) personals.getTotalElements());
            specResponse.setResponse(response);
        }
        return specResponse;
    }

    @Override
    @Transactional(readOnly = true)
    @PreAuthorize("hasAuthority('R_PERSONNEL')")
    public SearchDTO.SearchRs<PersonnelDTO.Info> search(SearchDTO.SearchRq request) throws IllegalAccessException, NoSuchFieldException {
        return BaseService.optimizedSearch(repository, mapper::entityToDtoInfo, request);
    }

}
