package com.nicico.evaluation.service;

import com.nicico.copper.common.dto.search.EOperator;
import com.nicico.copper.common.dto.search.SearchDTO;
import com.nicico.evaluation.common.PageableMapper;
import com.nicico.evaluation.dto.CatalogDTO;
import com.nicico.evaluation.dto.CatalogTypeDTO;
import com.nicico.evaluation.dto.SensitiveEventsDTO;
import com.nicico.evaluation.exception.EvaluationHandleException;
import com.nicico.evaluation.mapper.SensitiveEventsMapper;
import com.nicico.evaluation.model.Catalog;
import com.nicico.evaluation.model.CatalogType;
import com.nicico.evaluation.model.SensitiveEvents;
import com.nicico.evaluation.repository.SensitiveEventsRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.*;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class SensitiveEventsServiceTest {

    @Mock
    private SensitiveEventsMapper mockMapper;
    @Mock
    private PageableMapper mockPageableMapper;
    @Mock
    private SensitiveEventsRepository mockRepository;

    private SensitiveEventsService sensitiveEventsServiceUnderTest;

    @BeforeEach
    void setUp() {
        sensitiveEventsServiceUnderTest = new SensitiveEventsService(mockMapper, mockPageableMapper, mockRepository);
    }

    @Test
    void testGet() {
        // Setup
        // Configure SensitiveEventsRepository.findById(...).
        final Optional<SensitiveEvents> sensitiveEvents = Optional.of(
                new SensitiveEvents(0L, "title", new GregorianCalendar(2020, Calendar.JANUARY, 1).getTime(),
                        new GregorianCalendar(2020, Calendar.JANUARY, 1).getTime(), 0L,
                        new Catalog(0L, "title", "code", "description",
                                new CatalogType(0L, "title", "code", "description", List.of()), 0L), 0L,
                        new Catalog(0L, "title", "code", "description",
                                new CatalogType(0L, "title", "code", "description", List.of()), 0L), 0L,
                        new Catalog(0L, "title", "code", "description",
                                new CatalogType(0L, "title", "code", "description", List.of()), 0L), 0L,
                        "description"));
        when(mockRepository.findById(0L)).thenReturn(sensitiveEvents);

        // Configure SensitiveEventsMapper.entityToDtoInfo(...).
        final SensitiveEventsDTO.Info info = new SensitiveEventsDTO.Info();
        info.setId(0L);
        final CatalogDTO.Info statusCatalog = new CatalogDTO.Info();
        statusCatalog.setId(0L);
        statusCatalog.setDescription("description");
        statusCatalog.setCatalogTypeId(0L);
        final CatalogTypeDTO catalogType = new CatalogTypeDTO();
        catalogType.setTitle("title");
        catalogType.setCode("code");
        statusCatalog.setCatalogType(catalogType);
        info.setStatusCatalog(statusCatalog);
        final CatalogDTO.Info eventPolicyCatalog = new CatalogDTO.Info();
        eventPolicyCatalog.setId(0L);
        eventPolicyCatalog.setDescription("description");
        eventPolicyCatalog.setCatalogTypeId(0L);
        final CatalogTypeDTO catalogType1 = new CatalogTypeDTO();
        catalogType1.setTitle("title");
        catalogType1.setCode("code");
        eventPolicyCatalog.setCatalogType(catalogType1);
        info.setEventPolicyCatalog(eventPolicyCatalog);
        when(mockMapper.entityToDtoInfo(
                new SensitiveEvents(0L, "title", new GregorianCalendar(2020, Calendar.JANUARY, 1).getTime(),
                        new GregorianCalendar(2020, Calendar.JANUARY, 1).getTime(), 0L,
                        new Catalog(0L, "title", "code", "description",
                                new CatalogType(0L, "title", "code", "description", List.of()), 0L), 0L,
                        new Catalog(0L, "title", "code", "description",
                                new CatalogType(0L, "title", "code", "description", List.of()), 0L), 0L,
                        new Catalog(0L, "title", "code", "description",
                                new CatalogType(0L, "title", "code", "description", List.of()), 0L), 0L,
                        "description"))).thenReturn(info);

        // Run the test
        final SensitiveEventsDTO.Info result = sensitiveEventsServiceUnderTest.get(0L);

        // Verify the results
    }

    @Test
    void testGet_SensitiveEventsRepositoryReturnsAbsent() {
        // Setup
        when(mockRepository.findById(0L)).thenReturn(Optional.empty());

        // Run the test
        assertThatThrownBy(() -> sensitiveEventsServiceUnderTest.get(0L)).isInstanceOf(EvaluationHandleException.class);
    }

    @Test
    void testList() {
        // Setup
        when(mockPageableMapper.toPageable(0, 0)).thenReturn(PageRequest.of(0, 1));

        // Configure SensitiveEventsRepository.findAll(...).
        final Page<SensitiveEvents> sensitiveEvents = new PageImpl<>(
                List.of(new SensitiveEvents(0L, "title", new GregorianCalendar(2020, Calendar.JANUARY, 1).getTime(),
                        new GregorianCalendar(2020, Calendar.JANUARY, 1).getTime(), 0L,
                        new Catalog(0L, "title", "code", "description",
                                new CatalogType(0L, "title", "code", "description", List.of()), 0L), 0L,
                        new Catalog(0L, "title", "code", "description",
                                new CatalogType(0L, "title", "code", "description", List.of()), 0L), 0L,
                        new Catalog(0L, "title", "code", "description",
                                new CatalogType(0L, "title", "code", "description", List.of()), 0L), 0L,
                        "description")));
        when(mockRepository.findAll(any(Pageable.class))).thenReturn(sensitiveEvents);

        // Configure SensitiveEventsMapper.entityToDtoInfoList(...).
        final SensitiveEventsDTO.Info info = new SensitiveEventsDTO.Info();
        info.setId(0L);
        final CatalogDTO.Info statusCatalog = new CatalogDTO.Info();
        statusCatalog.setId(0L);
        statusCatalog.setDescription("description");
        statusCatalog.setCatalogTypeId(0L);
        final CatalogTypeDTO catalogType = new CatalogTypeDTO();
        catalogType.setTitle("title");
        catalogType.setCode("code");
        statusCatalog.setCatalogType(catalogType);
        info.setStatusCatalog(statusCatalog);
        final CatalogDTO.Info eventPolicyCatalog = new CatalogDTO.Info();
        eventPolicyCatalog.setId(0L);
        eventPolicyCatalog.setDescription("description");
        eventPolicyCatalog.setCatalogTypeId(0L);
        final CatalogTypeDTO catalogType1 = new CatalogTypeDTO();
        catalogType1.setTitle("title");
        catalogType1.setCode("code");
        eventPolicyCatalog.setCatalogType(catalogType1);
        info.setEventPolicyCatalog(eventPolicyCatalog);
        final List<SensitiveEventsDTO.Info> infos = List.of(info);
        when(mockMapper.entityToDtoInfoList(
                List.of(new SensitiveEvents(0L, "title", new GregorianCalendar(2020, Calendar.JANUARY, 1).getTime(),
                        new GregorianCalendar(2020, Calendar.JANUARY, 1).getTime(), 0L,
                        new Catalog(0L, "title", "code", "description",
                                new CatalogType(0L, "title", "code", "description", List.of()), 0L), 0L,
                        new Catalog(0L, "title", "code", "description",
                                new CatalogType(0L, "title", "code", "description", List.of()), 0L), 0L,
                        new Catalog(0L, "title", "code", "description",
                                new CatalogType(0L, "title", "code", "description", List.of()), 0L), 0L,
                        "description")))).thenReturn(infos);

        // Run the test
        final SensitiveEventsDTO.SpecResponse result = sensitiveEventsServiceUnderTest.list(0, 0);

        // Verify the results
    }

    @Test
    void testList_SensitiveEventsMapperReturnsNull() {
        // Setup
        when(mockPageableMapper.toPageable(0, 0)).thenReturn(PageRequest.of(0, 1));

        // Configure SensitiveEventsRepository.findAll(...).
        final Page<SensitiveEvents> sensitiveEvents = new PageImpl<>(
                List.of(new SensitiveEvents(0L, "title", new GregorianCalendar(2020, Calendar.JANUARY, 1).getTime(),
                        new GregorianCalendar(2020, Calendar.JANUARY, 1).getTime(), 0L,
                        new Catalog(0L, "title", "code", "description",
                                new CatalogType(0L, "title", "code", "description", List.of()), 0L), 0L,
                        new Catalog(0L, "title", "code", "description",
                                new CatalogType(0L, "title", "code", "description", List.of()), 0L), 0L,
                        new Catalog(0L, "title", "code", "description",
                                new CatalogType(0L, "title", "code", "description", List.of()), 0L), 0L,
                        "description")));
        when(mockRepository.findAll(any(Pageable.class))).thenReturn(sensitiveEvents);

        when(mockMapper.entityToDtoInfoList(
                List.of(new SensitiveEvents(0L, "title", new GregorianCalendar(2020, Calendar.JANUARY, 1).getTime(),
                        new GregorianCalendar(2020, Calendar.JANUARY, 1).getTime(), 0L,
                        new Catalog(0L, "title", "code", "description",
                                new CatalogType(0L, "title", "code", "description", List.of()), 0L), 0L,
                        new Catalog(0L, "title", "code", "description",
                                new CatalogType(0L, "title", "code", "description", List.of()), 0L), 0L,
                        new Catalog(0L, "title", "code", "description",
                                new CatalogType(0L, "title", "code", "description", List.of()), 0L), 0L,
                        "description")))).thenReturn(null);

        // Run the test
        final SensitiveEventsDTO.SpecResponse result = sensitiveEventsServiceUnderTest.list(0, 0);

        // Verify the results
    }

    @Test
    void testList_SensitiveEventsMapperReturnsNoItems() {
        // Setup
        when(mockPageableMapper.toPageable(0, 0)).thenReturn(PageRequest.of(0, 1));

        // Configure SensitiveEventsRepository.findAll(...).
        final Page<SensitiveEvents> sensitiveEvents = new PageImpl<>(
                List.of(new SensitiveEvents(0L, "title", new GregorianCalendar(2020, Calendar.JANUARY, 1).getTime(),
                        new GregorianCalendar(2020, Calendar.JANUARY, 1).getTime(), 0L,
                        new Catalog(0L, "title", "code", "description",
                                new CatalogType(0L, "title", "code", "description", List.of()), 0L), 0L,
                        new Catalog(0L, "title", "code", "description",
                                new CatalogType(0L, "title", "code", "description", List.of()), 0L), 0L,
                        new Catalog(0L, "title", "code", "description",
                                new CatalogType(0L, "title", "code", "description", List.of()), 0L), 0L,
                        "description")));
        when(mockRepository.findAll(any(Pageable.class))).thenReturn(sensitiveEvents);

        when(mockMapper.entityToDtoInfoList(
                List.of(new SensitiveEvents(0L, "title", new GregorianCalendar(2020, Calendar.JANUARY, 1).getTime(),
                        new GregorianCalendar(2020, Calendar.JANUARY, 1).getTime(), 0L,
                        new Catalog(0L, "title", "code", "description",
                                new CatalogType(0L, "title", "code", "description", List.of()), 0L), 0L,
                        new Catalog(0L, "title", "code", "description",
                                new CatalogType(0L, "title", "code", "description", List.of()), 0L), 0L,
                        new Catalog(0L, "title", "code", "description",
                                new CatalogType(0L, "title", "code", "description", List.of()), 0L), 0L,
                        "description")))).thenReturn(Collections.emptyList());

        // Run the test
        final SensitiveEventsDTO.SpecResponse result = sensitiveEventsServiceUnderTest.list(0, 0);

        // Verify the results
    }

    @Test
    void testCreate() {
        // Setup
        final SensitiveEventsDTO.Create dto = new SensitiveEventsDTO.Create();

        // Configure SensitiveEventsMapper.dtoCreateToEntity(...).
        final SensitiveEvents sensitiveEvents = new SensitiveEvents(0L, "title",
                new GregorianCalendar(2020, Calendar.JANUARY, 1).getTime(),
                new GregorianCalendar(2020, Calendar.JANUARY, 1).getTime(), 0L,
                new Catalog(0L, "title", "code", "description",
                        new CatalogType(0L, "title", "code", "description", List.of()), 0L), 0L,
                new Catalog(0L, "title", "code", "description",
                        new CatalogType(0L, "title", "code", "description", List.of()), 0L), 0L,
                new Catalog(0L, "title", "code", "description",
                        new CatalogType(0L, "title", "code", "description", List.of()), 0L), 0L, "description");
        when(mockMapper.dtoCreateToEntity(any(SensitiveEventsDTO.Create.class))).thenReturn(sensitiveEvents);

        // Configure SensitiveEventsRepository.save(...).
        final SensitiveEvents sensitiveEvents1 = new SensitiveEvents(0L, "title",
                new GregorianCalendar(2020, Calendar.JANUARY, 1).getTime(),
                new GregorianCalendar(2020, Calendar.JANUARY, 1).getTime(), 0L,
                new Catalog(0L, "title", "code", "description",
                        new CatalogType(0L, "title", "code", "description", List.of()), 0L), 0L,
                new Catalog(0L, "title", "code", "description",
                        new CatalogType(0L, "title", "code", "description", List.of()), 0L), 0L,
                new Catalog(0L, "title", "code", "description",
                        new CatalogType(0L, "title", "code", "description", List.of()), 0L), 0L, "description");
        when(mockRepository.save(
                new SensitiveEvents(0L, "title", new GregorianCalendar(2020, Calendar.JANUARY, 1).getTime(),
                        new GregorianCalendar(2020, Calendar.JANUARY, 1).getTime(), 0L,
                        new Catalog(0L, "title", "code", "description",
                                new CatalogType(0L, "title", "code", "description", List.of()), 0L), 0L,
                        new Catalog(0L, "title", "code", "description",
                                new CatalogType(0L, "title", "code", "description", List.of()), 0L), 0L,
                        new Catalog(0L, "title", "code", "description",
                                new CatalogType(0L, "title", "code", "description", List.of()), 0L), 0L,
                        "description"))).thenReturn(sensitiveEvents1);

        // Configure SensitiveEventsMapper.entityToDtoInfo(...).
        final SensitiveEventsDTO.Info info = new SensitiveEventsDTO.Info();
        info.setId(0L);
        final CatalogDTO.Info statusCatalog = new CatalogDTO.Info();
        statusCatalog.setId(0L);
        statusCatalog.setDescription("description");
        statusCatalog.setCatalogTypeId(0L);
        final CatalogTypeDTO catalogType = new CatalogTypeDTO();
        catalogType.setTitle("title");
        catalogType.setCode("code");
        statusCatalog.setCatalogType(catalogType);
        info.setStatusCatalog(statusCatalog);
        final CatalogDTO.Info eventPolicyCatalog = new CatalogDTO.Info();
        eventPolicyCatalog.setId(0L);
        eventPolicyCatalog.setDescription("description");
        eventPolicyCatalog.setCatalogTypeId(0L);
        final CatalogTypeDTO catalogType1 = new CatalogTypeDTO();
        catalogType1.setTitle("title");
        catalogType1.setCode("code");
        eventPolicyCatalog.setCatalogType(catalogType1);
        info.setEventPolicyCatalog(eventPolicyCatalog);
        when(mockMapper.entityToDtoInfo(
                new SensitiveEvents(0L, "title", new GregorianCalendar(2020, Calendar.JANUARY, 1).getTime(),
                        new GregorianCalendar(2020, Calendar.JANUARY, 1).getTime(), 0L,
                        new Catalog(0L, "title", "code", "description",
                                new CatalogType(0L, "title", "code", "description", List.of()), 0L), 0L,
                        new Catalog(0L, "title", "code", "description",
                                new CatalogType(0L, "title", "code", "description", List.of()), 0L), 0L,
                        new Catalog(0L, "title", "code", "description",
                                new CatalogType(0L, "title", "code", "description", List.of()), 0L), 0L,
                        "description"))).thenReturn(info);

        // Run the test
        final SensitiveEventsDTO.Info result = sensitiveEventsServiceUnderTest.create(dto);

        // Verify the results
    }

    @Test
    void testUpdate() {
        // Setup
        final SensitiveEventsDTO.Update dto = new SensitiveEventsDTO.Update();
        dto.setId(0L);

        // Configure SensitiveEventsRepository.findById(...).
        final Optional<SensitiveEvents> sensitiveEvents = Optional.of(
                new SensitiveEvents(0L, "title", new GregorianCalendar(2020, Calendar.JANUARY, 1).getTime(),
                        new GregorianCalendar(2020, Calendar.JANUARY, 1).getTime(), 0L,
                        new Catalog(0L, "title", "code", "description",
                                new CatalogType(0L, "title", "code", "description", List.of()), 0L), 0L,
                        new Catalog(0L, "title", "code", "description",
                                new CatalogType(0L, "title", "code", "description", List.of()), 0L), 0L,
                        new Catalog(0L, "title", "code", "description",
                                new CatalogType(0L, "title", "code", "description", List.of()), 0L), 0L,
                        "description"));
        when(mockRepository.findById(0L)).thenReturn(sensitiveEvents);

        // Configure SensitiveEventsRepository.save(...).
        final SensitiveEvents sensitiveEvents1 = new SensitiveEvents(0L, "title",
                new GregorianCalendar(2020, Calendar.JANUARY, 1).getTime(),
                new GregorianCalendar(2020, Calendar.JANUARY, 1).getTime(), 0L,
                new Catalog(0L, "title", "code", "description",
                        new CatalogType(0L, "title", "code", "description", List.of()), 0L), 0L,
                new Catalog(0L, "title", "code", "description",
                        new CatalogType(0L, "title", "code", "description", List.of()), 0L), 0L,
                new Catalog(0L, "title", "code", "description",
                        new CatalogType(0L, "title", "code", "description", List.of()), 0L), 0L, "description");
        when(mockRepository.save(
                new SensitiveEvents(0L, "title", new GregorianCalendar(2020, Calendar.JANUARY, 1).getTime(),
                        new GregorianCalendar(2020, Calendar.JANUARY, 1).getTime(), 0L,
                        new Catalog(0L, "title", "code", "description",
                                new CatalogType(0L, "title", "code", "description", List.of()), 0L), 0L,
                        new Catalog(0L, "title", "code", "description",
                                new CatalogType(0L, "title", "code", "description", List.of()), 0L), 0L,
                        new Catalog(0L, "title", "code", "description",
                                new CatalogType(0L, "title", "code", "description", List.of()), 0L), 0L,
                        "description"))).thenReturn(sensitiveEvents1);

        // Configure SensitiveEventsMapper.entityToDtoInfo(...).
        final SensitiveEventsDTO.Info info = new SensitiveEventsDTO.Info();
        info.setId(0L);
        final CatalogDTO.Info statusCatalog = new CatalogDTO.Info();
        statusCatalog.setId(0L);
        statusCatalog.setDescription("description");
        statusCatalog.setCatalogTypeId(0L);
        final CatalogTypeDTO catalogType = new CatalogTypeDTO();
        catalogType.setTitle("title");
        catalogType.setCode("code");
        statusCatalog.setCatalogType(catalogType);
        info.setStatusCatalog(statusCatalog);
        final CatalogDTO.Info eventPolicyCatalog = new CatalogDTO.Info();
        eventPolicyCatalog.setId(0L);
        eventPolicyCatalog.setDescription("description");
        eventPolicyCatalog.setCatalogTypeId(0L);
        final CatalogTypeDTO catalogType1 = new CatalogTypeDTO();
        catalogType1.setTitle("title");
        catalogType1.setCode("code");
        eventPolicyCatalog.setCatalogType(catalogType1);
        info.setEventPolicyCatalog(eventPolicyCatalog);
        when(mockMapper.entityToDtoInfo(
                new SensitiveEvents(0L, "title", new GregorianCalendar(2020, Calendar.JANUARY, 1).getTime(),
                        new GregorianCalendar(2020, Calendar.JANUARY, 1).getTime(), 0L,
                        new Catalog(0L, "title", "code", "description",
                                new CatalogType(0L, "title", "code", "description", List.of()), 0L), 0L,
                        new Catalog(0L, "title", "code", "description",
                                new CatalogType(0L, "title", "code", "description", List.of()), 0L), 0L,
                        new Catalog(0L, "title", "code", "description",
                                new CatalogType(0L, "title", "code", "description", List.of()), 0L), 0L,
                        "description"))).thenReturn(info);

        // Run the test
        final SensitiveEventsDTO.Info result = sensitiveEventsServiceUnderTest.update(dto);

        // Verify the results
        verify(mockMapper).update(
                eq(new SensitiveEvents(0L, "title", new GregorianCalendar(2020, Calendar.JANUARY, 1).getTime(),
                        new GregorianCalendar(2020, Calendar.JANUARY, 1).getTime(), 0L,
                        new Catalog(0L, "title", "code", "description",
                                new CatalogType(0L, "title", "code", "description", List.of()), 0L), 0L,
                        new Catalog(0L, "title", "code", "description",
                                new CatalogType(0L, "title", "code", "description", List.of()), 0L), 0L,
                        new Catalog(0L, "title", "code", "description",
                                new CatalogType(0L, "title", "code", "description", List.of()), 0L), 0L,
                        "description")), any(SensitiveEventsDTO.Update.class));
    }

    @Test
    void testUpdate_SensitiveEventsRepositoryFindByIdReturnsAbsent() {
        // Setup
        final SensitiveEventsDTO.Update dto = new SensitiveEventsDTO.Update();
        dto.setId(0L);

        when(mockRepository.findById(0L)).thenReturn(Optional.empty());

        // Run the test
        assertThatThrownBy(() -> sensitiveEventsServiceUnderTest.update(dto))
                .isInstanceOf(EvaluationHandleException.class);
    }

    @Test
    void testDelete() {
        // Setup
        // Configure SensitiveEventsRepository.findById(...).
        final Optional<SensitiveEvents> sensitiveEvents = Optional.of(
                new SensitiveEvents(0L, "title", new GregorianCalendar(2020, Calendar.JANUARY, 1).getTime(),
                        new GregorianCalendar(2020, Calendar.JANUARY, 1).getTime(), 0L,
                        new Catalog(0L, "title", "code", "description",
                                new CatalogType(0L, "title", "code", "description", List.of()), 0L), 0L,
                        new Catalog(0L, "title", "code", "description",
                                new CatalogType(0L, "title", "code", "description", List.of()), 0L), 0L,
                        new Catalog(0L, "title", "code", "description",
                                new CatalogType(0L, "title", "code", "description", List.of()), 0L), 0L,
                        "description"));
        when(mockRepository.findById(0L)).thenReturn(sensitiveEvents);

        // Run the test
        sensitiveEventsServiceUnderTest.delete(0L);

        // Verify the results
        verify(mockRepository).delete(
                new SensitiveEvents(0L, "title", new GregorianCalendar(2020, Calendar.JANUARY, 1).getTime(),
                        new GregorianCalendar(2020, Calendar.JANUARY, 1).getTime(), 0L,
                        new Catalog(0L, "title", "code", "description",
                                new CatalogType(0L, "title", "code", "description", List.of()), 0L), 0L,
                        new Catalog(0L, "title", "code", "description",
                                new CatalogType(0L, "title", "code", "description", List.of()), 0L), 0L,
                        new Catalog(0L, "title", "code", "description",
                                new CatalogType(0L, "title", "code", "description", List.of()), 0L), 0L,
                        "description"));
    }

    @Test
    void testDelete_SensitiveEventsRepositoryFindByIdReturnsAbsent() {
        // Setup
        when(mockRepository.findById(0L)).thenReturn(Optional.empty());

        // Run the test
        assertThatThrownBy(() -> sensitiveEventsServiceUnderTest.delete(0L))
                .isInstanceOf(EvaluationHandleException.class);
    }

}
