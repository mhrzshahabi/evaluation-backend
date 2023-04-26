package com.nicico.evaluation.service;

import com.nicico.copper.common.dto.search.SearchDTO;
import com.nicico.evaluation.common.PageableMapper;
import com.nicico.evaluation.dto.PersonDTO;
import com.nicico.evaluation.exception.EvaluationHandleException;
import com.nicico.evaluation.iservice.IPersonService;
import com.nicico.evaluation.mapper.PersonMapper;
import com.nicico.evaluation.model.Person;
import com.nicico.evaluation.repository.PersonRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RequiredArgsConstructor
@Service
public class PersonService implements IPersonService {

    private final PersonMapper mapper;
    private final PersonRepository repository;
    private final PageableMapper pageableMapper;

    @Override
    @Transactional(readOnly = true)
    @PreAuthorize("hasAuthority('R_PERSON')")
    public PersonDTO.Info get(Long id) {
        Person person = repository.findById(id).orElseThrow(() -> new EvaluationHandleException(EvaluationHandleException.ErrorType.NotFound));
        return mapper.entityToDtoInfo(person);
    }

    @Override
    @Transactional(readOnly = true)
    @PreAuthorize("hasAuthority('R_PERSON')")
    public PersonDTO.SpecResponse list(int count, int startIndex) {
        Pageable pageable = pageableMapper.toPageable(count, startIndex);
        Page<Person> persons = repository.findAll(pageable);
        List<PersonDTO.Info> personInfos = mapper.entityToDtoInfoList(persons.getContent());
        PersonDTO.Response response = new PersonDTO.Response();
        PersonDTO.SpecResponse specResponse = new PersonDTO.SpecResponse();

        if (personInfos != null) {
            response.setData(personInfos)
                    .setStartRow(startIndex)
                    .setEndRow(startIndex + count)
                    .setTotalRows((int) persons.getTotalElements());
            specResponse.setResponse(response);
        }
        return specResponse;
    }

    @Override
    @Transactional(readOnly = true)
    @PreAuthorize("hasAuthority('R_PERSON')")
    public SearchDTO.SearchRs<PersonDTO.Info> search(SearchDTO.SearchRq request) throws IllegalAccessException, NoSuchFieldException {
        return BaseService.optimizedSearch(repository, mapper::entityToDtoInfo, request);
    }

}
