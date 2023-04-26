package com.nicico.evaluation.service;

import com.nicico.evaluation.common.PageableMapper;
import com.nicico.evaluation.dto.*;
import com.nicico.evaluation.exception.EvaluationHandleException;
import com.nicico.evaluation.mapper.PostMeritComponentMapper;
import com.nicico.evaluation.model.MeritComponent;
import com.nicico.evaluation.model.PostMeritComponent;
import com.nicico.evaluation.repository.PostMeritComponentRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PostMeritComponentServiceTest {

    @Mock
    private PostMeritComponentMapper mockMapper;
    @Mock
    private PageableMapper mockPageableMapper;
    @Mock
    private PostMeritComponentRepository mockRepository;

    private PostMeritComponentService postMeritComponentServiceUnderTest;

    @BeforeEach
    void setUp() {
        postMeritComponentServiceUnderTest = new PostMeritComponentService(mockMapper, mockPageableMapper,
                mockRepository);
    }

    @Test
    void testGet() {
        // Setup
        // Configure PostMeritComponentRepository.findById(...).
        final Optional<PostMeritComponent> postMeritComponent = Optional.of(
                new PostMeritComponent(0L, "groupPostCode", new MeritComponent(0L, "title", "code",null), 0L, 0L, 0L));
        when(mockRepository.findById(0L)).thenReturn(postMeritComponent);

        // Configure PostMeritComponentMapper.entityToDtoInfo(...).
        final PostMeritComponentDTO.Info info = new PostMeritComponentDTO.Info();
        info.setId(0L);
        final MeritComponentDTO.Info meritComponent = new MeritComponentDTO.Info();
        meritComponent.setId(0L);
        meritComponent.setHasEvaluation(false);
        final KPITypeDTO.Info kpiTypeDTO = new KPITypeDTO.Info();
        kpiTypeDTO.setId(0L);
        final CatalogDTO.Info levelDefCatalog = new CatalogDTO.Info();
        levelDefCatalog.setId(0L);
        levelDefCatalog.setDescription("description");
        levelDefCatalog.setCatalogTypeId(0L);
        final CatalogTypeDTO catalogType = new CatalogTypeDTO();
        catalogType.setTitle("title");
        catalogType.setCode("code");
        levelDefCatalog.setCatalogType(catalogType);
        kpiTypeDTO.setLevelDefCatalog(levelDefCatalog);
//        meritComponent.setKpiType(List.of(kpiTypeDTO));
        info.setMeritComponent(meritComponent);
        when(mockMapper.entityToDtoInfo(
                new PostMeritComponent(0L, "groupPostCode", new MeritComponent(0L, "title", "code",null), 0L, 0L,
                        0L))).thenReturn(info);

        // Run the test
        final PostMeritComponentDTO.Info result = postMeritComponentServiceUnderTest.get(0L);

        // Verify the results
    }

    @Test
    void testGet_PostMeritComponentRepositoryReturnsAbsent() {
        // Setup
        when(mockRepository.findById(0L)).thenReturn(Optional.empty());

        // Run the test
        assertThatThrownBy(() -> postMeritComponentServiceUnderTest.get(0L))
                .isInstanceOf(EvaluationHandleException.class);
    }

    @Test
    void testList() {
        // Setup
        when(mockPageableMapper.toPageable(0, 0)).thenReturn(PageRequest.of(0, 1));

        // Configure PostMeritComponentRepository.findAll(...).
        final Page<PostMeritComponent> postMeritComponents = new PageImpl<>(
                List.of(new PostMeritComponent(0L, "groupPostCode", new MeritComponent(0L, "title", "code",null), 0L, 0L,
                        0L)));
        when(mockRepository.findAll(any(Pageable.class))).thenReturn(postMeritComponents);

        // Configure PostMeritComponentMapper.entityToDtoInfoList(...).
        final PostMeritComponentDTO.Info info = new PostMeritComponentDTO.Info();
        info.setId(0L);
        final MeritComponentDTO.Info meritComponent = new MeritComponentDTO.Info();
        meritComponent.setId(0L);
        meritComponent.setHasEvaluation(false);
        final KPITypeDTO.Info kpiTypeDTO = new KPITypeDTO.Info();
        kpiTypeDTO.setId(0L);
        final CatalogDTO.Info levelDefCatalog = new CatalogDTO.Info();
        levelDefCatalog.setId(0L);
        levelDefCatalog.setDescription("description");
        levelDefCatalog.setCatalogTypeId(0L);
        final CatalogTypeDTO catalogType = new CatalogTypeDTO();
        catalogType.setTitle("title");
        catalogType.setCode("code");
        levelDefCatalog.setCatalogType(catalogType);
        kpiTypeDTO.setLevelDefCatalog(levelDefCatalog);
//        meritComponent.setKpiType(List.of(kpiTypeDTO));
        info.setMeritComponent(meritComponent);
        final List<PostMeritComponentDTO.Info> infos = List.of(info);
        when(mockMapper.entityToDtoInfoList(
                List.of(new PostMeritComponent(0L, "groupPostCode", new MeritComponent(0L, "title", "code",null), 0L, 0L,
                        0L)))).thenReturn(infos);

        // Run the test
        final PostMeritComponentDTO.SpecResponse result = postMeritComponentServiceUnderTest.list(0, 0);

        // Verify the results
    }

    @Test
    void testList_PostMeritComponentMapperReturnsNull() {
        // Setup
        when(mockPageableMapper.toPageable(0, 0)).thenReturn(PageRequest.of(0, 1));

        // Configure PostMeritComponentRepository.findAll(...).
        final Page<PostMeritComponent> postMeritComponents = new PageImpl<>(
                List.of(new PostMeritComponent(0L, "groupPostCode", new MeritComponent(0L, "title", "code",null), 0L, 0L,
                        0L)));
        when(mockRepository.findAll(any(Pageable.class))).thenReturn(postMeritComponents);

        when(mockMapper.entityToDtoInfoList(
                List.of(new PostMeritComponent(0L, "groupPostCode", new MeritComponent(0L, "title", "code",null), 0L, 0L,
                        0L)))).thenReturn(null);

        // Run the test
        final PostMeritComponentDTO.SpecResponse result = postMeritComponentServiceUnderTest.list(0, 0);

        // Verify the results
    }

    @Test
    void testList_PostMeritComponentMapperReturnsNoItems() {
        // Setup
        when(mockPageableMapper.toPageable(0, 0)).thenReturn(PageRequest.of(0, 1));

        // Configure PostMeritComponentRepository.findAll(...).
        final Page<PostMeritComponent> postMeritComponents = new PageImpl<>(
                List.of(new PostMeritComponent(0L, "groupPostCode", new MeritComponent(0L, "title", "code",null), 0L, 0L,
                        0L)));
        when(mockRepository.findAll(any(Pageable.class))).thenReturn(postMeritComponents);

        when(mockMapper.entityToDtoInfoList(
                List.of(new PostMeritComponent(0L, "groupPostCode", new MeritComponent(0L, "title", "code",null), 0L, 0L,
                        0L)))).thenReturn(Collections.emptyList());

        // Run the test
        final PostMeritComponentDTO.SpecResponse result = postMeritComponentServiceUnderTest.list(0, 0);

        // Verify the results
    }

    @Test
    void testCreate() {
        // Setup
        final PostMeritComponentDTO.Create dto = new PostMeritComponentDTO.Create();

        // Configure PostMeritComponentMapper.dtoCreateToEntity(...).
        final PostMeritComponent postMeritComponent = new PostMeritComponent(0L, "groupPostCode",
                new MeritComponent(0L, "title", "code",null), 0L, 0L, 0L);
        when(mockMapper.dtoCreateToEntity(any(PostMeritComponentDTO.Create.class))).thenReturn(postMeritComponent);

        // Configure PostMeritComponentRepository.save(...).
        final PostMeritComponent postMeritComponent1 = new PostMeritComponent(0L, "groupPostCode",
                new MeritComponent(0L, "title", "code",null), 0L, 0L, 0L);
        when(mockRepository.save(
                new PostMeritComponent(0L, "groupPostCode", new MeritComponent(0L, "title", "code",null), 0L, 0L,
                        0L))).thenReturn(postMeritComponent1);

        // Configure PostMeritComponentMapper.entityToDtoInfo(...).
        final PostMeritComponentDTO.Info info = new PostMeritComponentDTO.Info();
        info.setId(0L);
        final MeritComponentDTO.Info meritComponent = new MeritComponentDTO.Info();
        meritComponent.setId(0L);
        meritComponent.setHasEvaluation(false);
        final KPITypeDTO.Info kpiTypeDTO = new KPITypeDTO.Info();
        kpiTypeDTO.setId(0L);
        final CatalogDTO.Info levelDefCatalog = new CatalogDTO.Info();
        levelDefCatalog.setId(0L);
        levelDefCatalog.setDescription("description");
        levelDefCatalog.setCatalogTypeId(0L);
        final CatalogTypeDTO catalogType = new CatalogTypeDTO();
        catalogType.setTitle("title");
        catalogType.setCode("code");
        levelDefCatalog.setCatalogType(catalogType);
        kpiTypeDTO.setLevelDefCatalog(levelDefCatalog);
//        meritComponent.setKpiType(List.of(kpiTypeDTO));
        info.setMeritComponent(meritComponent);
        when(mockMapper.entityToDtoInfo(
                new PostMeritComponent(0L, "groupPostCode", new MeritComponent(0L, "title", "code",null), 0L, 0L,
                        0L))).thenReturn(info);

        // Run the test
        final PostMeritComponentDTO.Info result = postMeritComponentServiceUnderTest.create(dto);

        // Verify the results
    }

    @Test
    void testUpdate() {
        // Setup
        final PostMeritComponentDTO.Update dto = new PostMeritComponentDTO.Update();
        dto.setId(0L);

        // Configure PostMeritComponentRepository.findById(...).
        final Optional<PostMeritComponent> postMeritComponent = Optional.of(
                new PostMeritComponent(0L, "groupPostCode", new MeritComponent(0L, "title", "code",null), 0L, 0L, 0L));
        when(mockRepository.findById(0L)).thenReturn(postMeritComponent);

        // Configure PostMeritComponentRepository.save(...).
        final PostMeritComponent postMeritComponent1 = new PostMeritComponent(0L, "groupPostCode",
                new MeritComponent(0L, "title", "code",null), 0L, 0L, 0L);
        when(mockRepository.save(
                new PostMeritComponent(0L, "groupPostCode", new MeritComponent(0L, "title", "code",null), 0L, 0L,
                        0L))).thenReturn(postMeritComponent1);

        // Configure PostMeritComponentMapper.entityToDtoInfo(...).
        final PostMeritComponentDTO.Info info = new PostMeritComponentDTO.Info();
        info.setId(0L);
        final MeritComponentDTO.Info meritComponent = new MeritComponentDTO.Info();
        meritComponent.setId(0L);
        meritComponent.setHasEvaluation(false);
        final KPITypeDTO.Info kpiTypeDTO = new KPITypeDTO.Info();
        kpiTypeDTO.setId(0L);
        final CatalogDTO.Info levelDefCatalog = new CatalogDTO.Info();
        levelDefCatalog.setId(0L);
        levelDefCatalog.setDescription("description");
        levelDefCatalog.setCatalogTypeId(0L);
        final CatalogTypeDTO catalogType = new CatalogTypeDTO();
        catalogType.setTitle("title");
        catalogType.setCode("code");
        levelDefCatalog.setCatalogType(catalogType);
        kpiTypeDTO.setLevelDefCatalog(levelDefCatalog);
//        meritComponent.setKpiType(List.of(kpiTypeDTO));
        info.setMeritComponent(meritComponent);
        when(mockMapper.entityToDtoInfo(
                new PostMeritComponent(0L, "groupPostCode", new MeritComponent(0L, "title", "code",null), 0L, 0L,
                        0L))).thenReturn(info);

        // Run the test
        final PostMeritComponentDTO.Info result = postMeritComponentServiceUnderTest.update(dto);

        // Verify the results
        verify(mockMapper).update(
                eq(new PostMeritComponent(0L, "groupPostCode", new MeritComponent(0L, "title", "code",null), 0L, 0L, 0L)),
                any(PostMeritComponentDTO.Update.class));
    }

    @Test
    void testUpdate_PostMeritComponentRepositoryFindByIdReturnsAbsent() {
        // Setup
        final PostMeritComponentDTO.Update dto = new PostMeritComponentDTO.Update();
        dto.setId(0L);

        when(mockRepository.findById(0L)).thenReturn(Optional.empty());

        // Run the test
        assertThatThrownBy(() -> postMeritComponentServiceUnderTest.update(dto))
                .isInstanceOf(EvaluationHandleException.class);
    }

    @Test
    void testDelete() {
        // Setup
        // Configure PostMeritComponentRepository.findById(...).
        final Optional<PostMeritComponent> postMeritComponent = Optional.of(
                new PostMeritComponent(0L, "groupPostCode", new MeritComponent(0L, "title", "code",null), 0L, 0L, 0L));
        when(mockRepository.findById(0L)).thenReturn(postMeritComponent);

        // Run the test
        postMeritComponentServiceUnderTest.delete(0L);

        // Verify the results
        verify(mockRepository).delete(
                new PostMeritComponent(0L, "groupPostCode", new MeritComponent(0L, "title", "code",null), 0L, 0L, 0L));
    }

    @Test
    void testDelete_PostMeritComponentRepositoryFindByIdReturnsAbsent() {
        // Setup
        when(mockRepository.findById(0L)).thenReturn(Optional.empty());

        // Run the test
        assertThatThrownBy(() -> postMeritComponentServiceUnderTest.delete(0L))
                .isInstanceOf(EvaluationHandleException.class);
    }

}
