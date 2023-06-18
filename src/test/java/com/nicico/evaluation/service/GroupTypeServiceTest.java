package com.nicico.evaluation.service;

import com.nicico.copper.common.dto.search.EOperator;
import com.nicico.copper.common.dto.search.SearchDTO;
import com.nicico.evaluation.common.PageableMapper;
import com.nicico.evaluation.dto.*;
import com.nicico.evaluation.exception.EvaluationHandleException;
import com.nicico.evaluation.iservice.IKPITypeService;
import com.nicico.evaluation.mapper.GroupTypeMapper;
import com.nicico.evaluation.model.GroupType;
import com.nicico.evaluation.model.GroupTypeMerit;
import com.nicico.evaluation.model.InstanceGroupTypeMerit;
import com.nicico.evaluation.repository.GroupTypeRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.MessageSource;
import org.springframework.context.NoSuchMessageException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class GroupTypeServiceTest {

    @Mock
    private GroupTypeMapper mockMapper;
    @Mock
    private PageableMapper mockPageableMapper;
    @Mock
    private GroupTypeRepository mockRepository;
    @Mock
    private IKPITypeService mockKpiTypeService;
    @Mock
    private MessageSource mockMessageSource;

    private GroupTypeService groupTypeServiceUnderTest;

    @BeforeEach
    void setUp() {
        groupTypeServiceUnderTest = new GroupTypeService(mockMapper, mockPageableMapper, mockRepository,
                mockKpiTypeService, mockMessageSource);
    }

//    @Test
//    void testGet() {
//        // Setup
//        // Configure GroupTypeRepository.findById(...).
//        final GroupType groupType1 = new GroupType();
//        groupType1.setId(0L);
//        groupType1.setCode("code");
//        groupType1.setKpiTypeId(0L);
//        groupType1.setGroupId(0L);
//        groupType1.setWeight(0L);
//        final GroupTypeMerit groupTypeMerit = new GroupTypeMerit();
//        groupTypeMerit.setId(0L);
//        groupTypeMerit.setMeritComponentId(0L);
//        groupTypeMerit.setGroupTypeId(0L);
//        groupTypeMerit.setWeight(0L);
//        final InstanceGroupTypeMerit instanceGroupTypeMerit = new InstanceGroupTypeMerit();
//        instanceGroupTypeMerit.setId(0L);
//        instanceGroupTypeMerit.setGroupTypeMeritId(0L);
//        instanceGroupTypeMerit.setInstanceId(0L);
//        groupTypeMerit.setInstanceGroupTypeMerits(List.of(instanceGroupTypeMerit));
//        groupTypeMerit.setHasInstance(false);
//        groupType1.setGroupTypeMeritList(List.of(groupTypeMerit));
//        final Optional<GroupType> groupType = Optional.of(groupType1);
//        when(mockRepository.findById(0L)).thenReturn(groupType);
//
//        // Configure GroupTypeMapper.entityToDtoInfo(...).
//        final GroupTypeDTO.Info info = new GroupTypeDTO.Info();
//        info.setWeight(0L);
//        info.setGroupId(0L);
//        info.setId(0L);
//        final KPITypeDTO.Info kpiType = new KPITypeDTO.Info();
//        kpiType.setId(0L);
//        final CatalogDTO.Info levelDefCatalog = new CatalogDTO.Info();
//        levelDefCatalog.setId(0L);
//        levelDefCatalog.setDescription("description");
//        levelDefCatalog.setCatalogTypeId(0L);
//        final CatalogTypeDTO catalogType = new CatalogTypeDTO();
//        catalogType.setTitle("title");
//        catalogType.setCode("code");
//        levelDefCatalog.setCatalogType(catalogType);
//        kpiType.setLevelDefCatalog(levelDefCatalog);
//        info.setKpiType(kpiType);
//        final GroupDTO.Info group = new GroupDTO.Info();
//        info.setGroup(group);
//        info.setTotalWeight(0L);
//        info.setHasAllKpiType(false);
//        when(mockMapper.entityToDtoInfo(new GroupType())).thenReturn(info);
//
//        // Run the test
//        final GroupTypeDTO.Info result = groupTypeServiceUnderTest.get(0L);
//
//        // Verify the results
//    }

    @Test
    void testGet_GroupTypeRepositoryReturnsAbsent() {
        // Setup
        when(mockRepository.findById(0L)).thenReturn(Optional.empty());

        // Run the test
        assertThatThrownBy(() -> groupTypeServiceUnderTest.get(0L)).isInstanceOf(EvaluationHandleException.class);
    }

    @Test
    void testGetTypeByAssessPostCode() {
        // Setup
        final GroupType groupType = new GroupType();
        groupType.setId(0L);
        groupType.setCode("code");
        groupType.setKpiTypeId(0L);
        groupType.setGroupId(0L);
        groupType.setWeight(0L);
        final GroupTypeMerit groupTypeMerit = new GroupTypeMerit();
        groupTypeMerit.setId(0L);
        groupTypeMerit.setMeritComponentId(0L);
        groupTypeMerit.setGroupTypeId(0L);
        groupTypeMerit.setWeight(0L);
        final InstanceGroupTypeMerit instanceGroupTypeMerit = new InstanceGroupTypeMerit();
        instanceGroupTypeMerit.setId(0L);
        instanceGroupTypeMerit.setGroupTypeMeritId(0L);
        instanceGroupTypeMerit.setInstanceId(0L);
        groupTypeMerit.setInstanceGroupTypeMerits(List.of(instanceGroupTypeMerit));
        groupTypeMerit.setHasInstance(false);
        groupType.setGroupTypeMeritList(List.of(groupTypeMerit));
        final List<GroupType> expectedResult = List.of(groupType);

        // Configure GroupTypeRepository.getTypeByAssessPostCode(...).
        final GroupType groupType1 = new GroupType();
        groupType1.setId(0L);
        groupType1.setCode("code");
        groupType1.setKpiTypeId(0L);
        groupType1.setGroupId(0L);
        groupType1.setWeight(0L);
        final GroupTypeMerit groupTypeMerit1 = new GroupTypeMerit();
        groupTypeMerit1.setId(0L);
        groupTypeMerit1.setMeritComponentId(0L);
        groupTypeMerit1.setGroupTypeId(0L);
        groupTypeMerit1.setWeight(0L);
        final InstanceGroupTypeMerit instanceGroupTypeMerit1 = new InstanceGroupTypeMerit();
        instanceGroupTypeMerit1.setId(0L);
        instanceGroupTypeMerit1.setGroupTypeMeritId(0L);
        instanceGroupTypeMerit1.setInstanceId(0L);
        groupTypeMerit1.setInstanceGroupTypeMerits(List.of(instanceGroupTypeMerit1));
        groupTypeMerit1.setHasInstance(false);
        groupType1.setGroupTypeMeritList(List.of(groupTypeMerit1));
        final List<GroupType> groupTypes = List.of(groupType1);
        when(mockRepository.getTypeByAssessPostCode("assessPostCode", "levelDef")).thenReturn(groupTypes);

        // Run the test
        final List<GroupType> result = groupTypeServiceUnderTest.getTypeByAssessPostCode("assessPostCode", "levelDef");

        // Verify the results
        assertThat(result).isEqualTo(expectedResult);
    }

    @Test
    void testGetTypeByAssessPostCode_GroupTypeRepositoryReturnsNoItems() {
        // Setup
        when(mockRepository.getTypeByAssessPostCode("assessPostCode", "levelDef")).thenReturn(Collections.emptyList());

        // Run the test
        final List<GroupType> result = groupTypeServiceUnderTest.getTypeByAssessPostCode("assessPostCode", "levelDef");

        // Verify the results
        assertThat(result).isEqualTo(Collections.emptyList());
    }

    @Test
    void testGetTypeByEvaluationId() {
        // Setup
        final GroupType groupType = new GroupType();
        groupType.setId(0L);
        groupType.setCode("code");
        groupType.setKpiTypeId(0L);
        groupType.setGroupId(0L);
        groupType.setWeight(0L);
        final GroupTypeMerit groupTypeMerit = new GroupTypeMerit();
        groupTypeMerit.setId(0L);
        groupTypeMerit.setMeritComponentId(0L);
        groupTypeMerit.setGroupTypeId(0L);
        groupTypeMerit.setWeight(0L);
        final InstanceGroupTypeMerit instanceGroupTypeMerit = new InstanceGroupTypeMerit();
        instanceGroupTypeMerit.setId(0L);
        instanceGroupTypeMerit.setGroupTypeMeritId(0L);
        instanceGroupTypeMerit.setInstanceId(0L);
        groupTypeMerit.setInstanceGroupTypeMerits(List.of(instanceGroupTypeMerit));
        groupTypeMerit.setHasInstance(false);
        groupType.setGroupTypeMeritList(List.of(groupTypeMerit));
        final List<GroupType> expectedResult = List.of(groupType);

        // Configure GroupTypeRepository.getTypeByEvaluationId(...).
        final GroupType groupType1 = new GroupType();
        groupType1.setId(0L);
        groupType1.setCode("code");
        groupType1.setKpiTypeId(0L);
        groupType1.setGroupId(0L);
        groupType1.setWeight(0L);
        final GroupTypeMerit groupTypeMerit1 = new GroupTypeMerit();
        groupTypeMerit1.setId(0L);
        groupTypeMerit1.setMeritComponentId(0L);
        groupTypeMerit1.setGroupTypeId(0L);
        groupTypeMerit1.setWeight(0L);
        final InstanceGroupTypeMerit instanceGroupTypeMerit1 = new InstanceGroupTypeMerit();
        instanceGroupTypeMerit1.setId(0L);
        instanceGroupTypeMerit1.setGroupTypeMeritId(0L);
        instanceGroupTypeMerit1.setInstanceId(0L);
        groupTypeMerit1.setInstanceGroupTypeMerits(List.of(instanceGroupTypeMerit1));
        groupTypeMerit1.setHasInstance(false);
        groupType1.setGroupTypeMeritList(List.of(groupTypeMerit1));
        final List<GroupType> groupTypes = List.of(groupType1);
        when(mockRepository.getTypeByEvaluationId(0L, "levelDef")).thenReturn(groupTypes);

        // Run the test
        final List<GroupType> result = groupTypeServiceUnderTest.getTypeByEvaluationId(0L, "levelDef");

        // Verify the results
        assertThat(result).isEqualTo(expectedResult);
    }

    @Test
    void testGetTypeByEvaluationId_GroupTypeRepositoryReturnsNoItems() {
        // Setup
        when(mockRepository.getTypeByEvaluationId(0L, "levelDef")).thenReturn(Collections.emptyList());

        // Run the test
        final List<GroupType> result = groupTypeServiceUnderTest.getTypeByEvaluationId(0L, "levelDef");

        // Verify the results
        assertThat(result).isEqualTo(Collections.emptyList());
    }

    @Test
    void testGetWeightInfoByGroupId() {
        // Setup
        // Configure IKPITypeService.findAll(...).
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
        final List<KPITypeDTO.Info> infos = List.of(kpiTypeDTO);
        when(mockKpiTypeService.findAll()).thenReturn(infos);

        // Configure GroupTypeRepository.getAllByGroupId(...).
        final GroupType groupType = new GroupType();
        groupType.setId(0L);
        groupType.setCode("code");
        groupType.setKpiTypeId(0L);
        groupType.setGroupId(0L);
        groupType.setWeight(0L);
        final GroupTypeMerit groupTypeMerit = new GroupTypeMerit();
        groupTypeMerit.setId(0L);
        groupTypeMerit.setMeritComponentId(0L);
        groupTypeMerit.setGroupTypeId(0L);
        groupTypeMerit.setWeight(0L);
        final InstanceGroupTypeMerit instanceGroupTypeMerit = new InstanceGroupTypeMerit();
        instanceGroupTypeMerit.setId(0L);
        instanceGroupTypeMerit.setGroupTypeMeritId(0L);
        instanceGroupTypeMerit.setInstanceId(0L);
        groupTypeMerit.setInstanceGroupTypeMerits(List.of(instanceGroupTypeMerit));
        groupTypeMerit.setHasInstance(false);
        groupType.setGroupTypeMeritList(List.of(groupTypeMerit));
        final List<GroupType> groupTypes = List.of(groupType);
        when(mockRepository.getAllByGroupId(0L)).thenReturn(groupTypes);

        // Run the test
        final GroupTypeDTO.GroupTypeMaxWeight result = groupTypeServiceUnderTest.getWeightInfoByGroupId(0L);

        // Verify the results
    }

    @Test
    void testGetWeightInfoByGroupId_IKPITypeServiceReturnsNoItems() {
        // Setup
        when(mockKpiTypeService.findAll()).thenReturn(Collections.emptyList());

        // Configure GroupTypeRepository.getAllByGroupId(...).
        final GroupType groupType = new GroupType();
        groupType.setId(0L);
        groupType.setCode("code");
        groupType.setKpiTypeId(0L);
        groupType.setGroupId(0L);
        groupType.setWeight(0L);
        final GroupTypeMerit groupTypeMerit = new GroupTypeMerit();
        groupTypeMerit.setId(0L);
        groupTypeMerit.setMeritComponentId(0L);
        groupTypeMerit.setGroupTypeId(0L);
        groupTypeMerit.setWeight(0L);
        final InstanceGroupTypeMerit instanceGroupTypeMerit = new InstanceGroupTypeMerit();
        instanceGroupTypeMerit.setId(0L);
        instanceGroupTypeMerit.setGroupTypeMeritId(0L);
        instanceGroupTypeMerit.setInstanceId(0L);
        groupTypeMerit.setInstanceGroupTypeMerits(List.of(instanceGroupTypeMerit));
        groupTypeMerit.setHasInstance(false);
        groupType.setGroupTypeMeritList(List.of(groupTypeMerit));
        final List<GroupType> groupTypes = List.of(groupType);
        when(mockRepository.getAllByGroupId(0L)).thenReturn(groupTypes);

        // Run the test
        final GroupTypeDTO.GroupTypeMaxWeight result = groupTypeServiceUnderTest.getWeightInfoByGroupId(0L);

        // Verify the results
    }

//    @Test
//    void testGetWeightInfoByGroupId_GroupTypeRepositoryReturnsNoItems() {
//        // Setup
//        // Configure IKPITypeService.findAll(...).
//        final KPITypeDTO.Info kpiTypeDTO = new KPITypeDTO.Info();
//        kpiTypeDTO.setId(0L);
//        final CatalogDTO.Info levelDefCatalog = new CatalogDTO.Info();
//        levelDefCatalog.setId(0L);
//        levelDefCatalog.setDescription("description");
//        levelDefCatalog.setCatalogTypeId(0L);
//        final CatalogTypeDTO catalogType = new CatalogTypeDTO();
//        catalogType.setTitle("title");
//        catalogType.setCode("code");
//        levelDefCatalog.setCatalogType(catalogType);
//        kpiTypeDTO.setLevelDefCatalog(levelDefCatalog);
//        final List<KPITypeDTO.Info> infos = List.of(kpiTypeDTO);
//        when(mockKpiTypeService.findAll()).thenReturn(infos);
//
//        when(mockRepository.getAllByGroupId(0L)).thenReturn(Collections.emptyList());
//
//        // Run the test
//        final GroupTypeDTO.GroupTypeMaxWeight result = groupTypeServiceUnderTest.getWeightInfoByGroupId(0L);
//
//        // Verify the results
//    }

//    @Test
//    void testList() {
//        // Setup
//        when(mockPageableMapper.toPageable(0, 0)).thenReturn(PageRequest.of(0, 1));
//
//        // Configure GroupTypeRepository.findAll(...).
//        final GroupType groupType = new GroupType();
//        groupType.setId(0L);
//        groupType.setCode("code");
//        groupType.setKpiTypeId(0L);
//        groupType.setGroupId(0L);
//        groupType.setWeight(0L);
//        final GroupTypeMerit groupTypeMerit = new GroupTypeMerit();
//        groupTypeMerit.setId(0L);
//        groupTypeMerit.setMeritComponentId(0L);
//        groupTypeMerit.setGroupTypeId(0L);
//        groupTypeMerit.setWeight(0L);
//        final InstanceGroupTypeMerit instanceGroupTypeMerit = new InstanceGroupTypeMerit();
//        instanceGroupTypeMerit.setId(0L);
//        instanceGroupTypeMerit.setGroupTypeMeritId(0L);
//        instanceGroupTypeMerit.setInstanceId(0L);
//        groupTypeMerit.setInstanceGroupTypeMerits(List.of(instanceGroupTypeMerit));
//        groupTypeMerit.setHasInstance(false);
//        groupType.setGroupTypeMeritList(List.of(groupTypeMerit));
//        final Page<GroupType> groupTypes = new PageImpl<>(List.of(groupType));
//        when(mockRepository.findAll(any(Pageable.class))).thenReturn(groupTypes);
//
//        // Configure GroupTypeMapper.entityToDtoInfoList(...).
//        final GroupTypeDTO.Info info = new GroupTypeDTO.Info();
//        info.setWeight(0L);
//        info.setGroupId(0L);
//        info.setId(0L);
//        final KPITypeDTO.Info kpiType = new KPITypeDTO.Info();
//        kpiType.setId(0L);
//        final CatalogDTO.Info levelDefCatalog = new CatalogDTO.Info();
//        levelDefCatalog.setId(0L);
//        levelDefCatalog.setDescription("description");
//        levelDefCatalog.setCatalogTypeId(0L);
//        final CatalogTypeDTO catalogType = new CatalogTypeDTO();
//        catalogType.setTitle("title");
//        catalogType.setCode("code");
//        levelDefCatalog.setCatalogType(catalogType);
//        kpiType.setLevelDefCatalog(levelDefCatalog);
//        info.setKpiType(kpiType);
//        final GroupDTO.Info group = new GroupDTO.Info();
//        info.setGroup(group);
//        info.setTotalWeight(0L);
//        info.setHasAllKpiType(false);
//        final List<GroupTypeDTO.Info> infos = List.of(info);
//        when(mockMapper.entityToDtoInfoList(List.of(new GroupType()))).thenReturn(infos);
//
//        // Run the test
//        final GroupTypeDTO.SpecResponse result = groupTypeServiceUnderTest.list(0, 0);
//
//        // Verify the results
//    }
//
//    @Test
//    void testList_GroupTypeRepositoryReturnsNoItems() {
//        // Setup
//        when(mockPageableMapper.toPageable(0, 0)).thenReturn(PageRequest.of(0, 1));
//        when(mockRepository.findAll(any(Pageable.class))).thenReturn(new PageImpl<>(Collections.emptyList()));
//
//        // Configure GroupTypeMapper.entityToDtoInfoList(...).
//        final GroupTypeDTO.Info info = new GroupTypeDTO.Info();
//        info.setWeight(0L);
//        info.setGroupId(0L);
//        info.setId(0L);
//        final KPITypeDTO.Info kpiType = new KPITypeDTO.Info();
//        kpiType.setId(0L);
//        final CatalogDTO.Info levelDefCatalog = new CatalogDTO.Info();
//        levelDefCatalog.setId(0L);
//        levelDefCatalog.setDescription("description");
//        levelDefCatalog.setCatalogTypeId(0L);
//        final CatalogTypeDTO catalogType = new CatalogTypeDTO();
//        catalogType.setTitle("title");
//        catalogType.setCode("code");
//        levelDefCatalog.setCatalogType(catalogType);
//        kpiType.setLevelDefCatalog(levelDefCatalog);
//        info.setKpiType(kpiType);
//        final GroupDTO.Info group = new GroupDTO.Info();
//        info.setGroup(group);
//        info.setTotalWeight(0L);
//        info.setHasAllKpiType(false);
//        final List<GroupTypeDTO.Info> infos = List.of(info);
//        when(mockMapper.entityToDtoInfoList(List.of(new GroupType()))).thenReturn(infos);
//
//        // Run the test
//        final GroupTypeDTO.SpecResponse result = groupTypeServiceUnderTest.list(0, 0);
//
//        // Verify the results
//    }

//    @Test
//    void testList_GroupTypeMapperReturnsNull() {
//        // Setup
//        when(mockPageableMapper.toPageable(0, 0)).thenReturn(PageRequest.of(0, 1));
//
//        // Configure GroupTypeRepository.findAll(...).
//        final GroupType groupType = new GroupType();
//        groupType.setId(0L);
//        groupType.setCode("code");
//        groupType.setKpiTypeId(0L);
//        groupType.setGroupId(0L);
//        groupType.setWeight(0L);
//        final GroupTypeMerit groupTypeMerit = new GroupTypeMerit();
//        groupTypeMerit.setId(0L);
//        groupTypeMerit.setMeritComponentId(0L);
//        groupTypeMerit.setGroupTypeId(0L);
//        groupTypeMerit.setWeight(0L);
//        final InstanceGroupTypeMerit instanceGroupTypeMerit = new InstanceGroupTypeMerit();
//        instanceGroupTypeMerit.setId(0L);
//        instanceGroupTypeMerit.setGroupTypeMeritId(0L);
//        instanceGroupTypeMerit.setInstanceId(0L);
//        groupTypeMerit.setInstanceGroupTypeMerits(List.of(instanceGroupTypeMerit));
//        groupTypeMerit.setHasInstance(false);
//        groupType.setGroupTypeMeritList(List.of(groupTypeMerit));
//        final Page<GroupType> groupTypes = new PageImpl<>(List.of(groupType));
//        when(mockRepository.findAll(any(Pageable.class))).thenReturn(groupTypes);
//
//        when(mockMapper.entityToDtoInfoList(List.of(new GroupType()))).thenReturn(null);
//
//        // Run the test
//        final GroupTypeDTO.SpecResponse result = groupTypeServiceUnderTest.list(0, 0);
//
//        // Verify the results
//    }

//    @Test
//    void testList_GroupTypeMapperReturnsNoItems() {
//        // Setup
//        when(mockPageableMapper.toPageable(0, 0)).thenReturn(PageRequest.of(0, 1));
//
//        // Configure GroupTypeRepository.findAll(...).
//        final GroupType groupType = new GroupType();
//        groupType.setId(0L);
//        groupType.setCode("code");
//        groupType.setKpiTypeId(0L);
//        groupType.setGroupId(0L);
//        groupType.setWeight(0L);
//        final GroupTypeMerit groupTypeMerit = new GroupTypeMerit();
//        groupTypeMerit.setId(0L);
//        groupTypeMerit.setMeritComponentId(0L);
//        groupTypeMerit.setGroupTypeId(0L);
//        groupTypeMerit.setWeight(0L);
//        final InstanceGroupTypeMerit instanceGroupTypeMerit = new InstanceGroupTypeMerit();
//        instanceGroupTypeMerit.setId(0L);
//        instanceGroupTypeMerit.setGroupTypeMeritId(0L);
//        instanceGroupTypeMerit.setInstanceId(0L);
//        groupTypeMerit.setInstanceGroupTypeMerits(List.of(instanceGroupTypeMerit));
//        groupTypeMerit.setHasInstance(false);
//        groupType.setGroupTypeMeritList(List.of(groupTypeMerit));
//        final Page<GroupType> groupTypes = new PageImpl<>(List.of(groupType));
//        when(mockRepository.findAll(any(Pageable.class))).thenReturn(groupTypes);
//
//        when(mockMapper.entityToDtoInfoList(List.of(new GroupType()))).thenReturn(Collections.emptyList());
//
//        // Run the test
//        final GroupTypeDTO.SpecResponse result = groupTypeServiceUnderTest.list(0, 0);
//
//        // Verify the results
//    }

//    @Test
//    void testCreate() {
//        // Setup
//        final GroupTypeDTO.Create dto = new GroupTypeDTO.Create();
//        dto.setWeight(0L);
//        dto.setGroupId(0L);
//
//        // Configure GroupTypeMapper.dtoCreateToEntity(...).
//        final GroupType groupType = new GroupType();
//        groupType.setId(0L);
//        groupType.setCode("code");
//        groupType.setKpiTypeId(0L);
//        groupType.setGroupId(0L);
//        groupType.setWeight(0L);
//        final GroupTypeMerit groupTypeMerit = new GroupTypeMerit();
//        groupTypeMerit.setId(0L);
//        groupTypeMerit.setMeritComponentId(0L);
//        groupTypeMerit.setGroupTypeId(0L);
//        groupTypeMerit.setWeight(0L);
//        final InstanceGroupTypeMerit instanceGroupTypeMerit = new InstanceGroupTypeMerit();
//        instanceGroupTypeMerit.setId(0L);
//        instanceGroupTypeMerit.setGroupTypeMeritId(0L);
//        instanceGroupTypeMerit.setInstanceId(0L);
//        groupTypeMerit.setInstanceGroupTypeMerits(List.of(instanceGroupTypeMerit));
//        groupTypeMerit.setHasInstance(false);
//        groupType.setGroupTypeMeritList(List.of(groupTypeMerit));
//        when(mockMapper.dtoCreateToEntity(any(GroupTypeDTO.Create.class))).thenReturn(groupType);
//
//        // Configure GroupTypeRepository.save(...).
//        final GroupType groupType1 = new GroupType();
//        groupType1.setId(0L);
//        groupType1.setCode("code");
//        groupType1.setKpiTypeId(0L);
//        groupType1.setGroupId(0L);
//        groupType1.setWeight(0L);
//        final GroupTypeMerit groupTypeMerit1 = new GroupTypeMerit();
//        groupTypeMerit1.setId(0L);
//        groupTypeMerit1.setMeritComponentId(0L);
//        groupTypeMerit1.setGroupTypeId(0L);
//        groupTypeMerit1.setWeight(0L);
//        final InstanceGroupTypeMerit instanceGroupTypeMerit1 = new InstanceGroupTypeMerit();
//        instanceGroupTypeMerit1.setId(0L);
//        instanceGroupTypeMerit1.setGroupTypeMeritId(0L);
//        instanceGroupTypeMerit1.setInstanceId(0L);
//        groupTypeMerit1.setInstanceGroupTypeMerits(List.of(instanceGroupTypeMerit1));
//        groupTypeMerit1.setHasInstance(false);
//        groupType1.setGroupTypeMeritList(List.of(groupTypeMerit1));
//        when(mockRepository.save(new GroupType())).thenReturn(groupType1);
//
//        // Configure GroupTypeMapper.entityToDtoInfo(...).
//        final GroupTypeDTO.Info info = new GroupTypeDTO.Info();
//        info.setWeight(0L);
//        info.setGroupId(0L);
//        info.setId(0L);
//        final KPITypeDTO.Info kpiType = new KPITypeDTO.Info();
//        kpiType.setId(0L);
//        final CatalogDTO.Info levelDefCatalog = new CatalogDTO.Info();
//        levelDefCatalog.setId(0L);
//        levelDefCatalog.setDescription("description");
//        levelDefCatalog.setCatalogTypeId(0L);
//        final CatalogTypeDTO catalogType = new CatalogTypeDTO();
//        catalogType.setTitle("title");
//        catalogType.setCode("code");
//        levelDefCatalog.setCatalogType(catalogType);
//        kpiType.setLevelDefCatalog(levelDefCatalog);
//        info.setKpiType(kpiType);
//        final GroupDTO.Info group = new GroupDTO.Info();
//        info.setGroup(group);
//        info.setTotalWeight(0L);
//        info.setHasAllKpiType(false);
//        when(mockMapper.entityToDtoInfo(new GroupType())).thenReturn(info);
//
//        // Run the test
//        final GroupTypeDTO.Info result = groupTypeServiceUnderTest.create(dto);
//
//        // Verify the results
//    }

//    @Test
//    void testCreate_MessageSourceThrowsNoSuchMessageException() {
//        // Setup
//        final GroupTypeDTO.Create dto = new GroupTypeDTO.Create();
//        dto.setWeight(0L);
//        dto.setGroupId(0L);
//
//        // Configure GroupTypeMapper.dtoCreateToEntity(...).
//        final GroupType groupType = new GroupType();
//        groupType.setId(0L);
//        groupType.setCode("code");
//        groupType.setKpiTypeId(0L);
//        groupType.setGroupId(0L);
//        groupType.setWeight(0L);
//        final GroupTypeMerit groupTypeMerit = new GroupTypeMerit();
//        groupTypeMerit.setId(0L);
//        groupTypeMerit.setMeritComponentId(0L);
//        groupTypeMerit.setGroupTypeId(0L);
//        groupTypeMerit.setWeight(0L);
//        final InstanceGroupTypeMerit instanceGroupTypeMerit = new InstanceGroupTypeMerit();
//        instanceGroupTypeMerit.setId(0L);
//        instanceGroupTypeMerit.setGroupTypeMeritId(0L);
//        instanceGroupTypeMerit.setInstanceId(0L);
//        groupTypeMerit.setInstanceGroupTypeMerits(List.of(instanceGroupTypeMerit));
//        groupTypeMerit.setHasInstance(false);
//        groupType.setGroupTypeMeritList(List.of(groupTypeMerit));
//        when(mockMapper.dtoCreateToEntity(any(GroupTypeDTO.Create.class))).thenReturn(groupType);
//
//        // Configure GroupTypeRepository.getByGroupIdAndKpiTypeId(...).
//        final GroupType groupType1 = new GroupType();
//        groupType1.setId(0L);
//        groupType1.setCode("code");
//        groupType1.setKpiTypeId(0L);
//        groupType1.setGroupId(0L);
//        groupType1.setWeight(0L);
//        final GroupTypeMerit groupTypeMerit1 = new GroupTypeMerit();
//        groupTypeMerit1.setId(0L);
//        groupTypeMerit1.setMeritComponentId(0L);
//        groupTypeMerit1.setGroupTypeId(0L);
//        groupTypeMerit1.setWeight(0L);
//        final InstanceGroupTypeMerit instanceGroupTypeMerit1 = new InstanceGroupTypeMerit();
//        instanceGroupTypeMerit1.setId(0L);
//        instanceGroupTypeMerit1.setGroupTypeMeritId(0L);
//        instanceGroupTypeMerit1.setInstanceId(0L);
//        groupTypeMerit1.setInstanceGroupTypeMerits(List.of(instanceGroupTypeMerit1));
//        groupTypeMerit1.setHasInstance(false);
//        groupType1.setGroupTypeMeritList(List.of(groupTypeMerit1));
//        when(mockRepository.getByGroupIdAndKpiTypeId(0L, 0L)).thenReturn(groupType1);
//
//        when(mockMessageSource.getMessage(eq("exception.duplicate.information"), any(Object[].class),
//                eq(Locale.US))).thenThrow(NoSuchMessageException.class);
//
//        // Run the test
//        assertThatThrownBy(() -> groupTypeServiceUnderTest.create(dto)).isInstanceOf(NoSuchMessageException.class);
//    }

    @Test
    void testGetAllByGroupIdAndKpiTypeId() {
        // Setup
        final GroupType groupType = new GroupType();
        groupType.setId(0L);
        groupType.setCode("code");
        groupType.setKpiTypeId(0L);
        groupType.setGroupId(0L);
        groupType.setWeight(0L);
        final GroupTypeMerit groupTypeMerit = new GroupTypeMerit();
        groupTypeMerit.setId(0L);
        groupTypeMerit.setMeritComponentId(0L);
        groupTypeMerit.setGroupTypeId(0L);
        groupTypeMerit.setWeight(0L);
        final InstanceGroupTypeMerit instanceGroupTypeMerit = new InstanceGroupTypeMerit();
        instanceGroupTypeMerit.setId(0L);
        instanceGroupTypeMerit.setGroupTypeMeritId(0L);
        instanceGroupTypeMerit.setInstanceId(0L);
        groupTypeMerit.setInstanceGroupTypeMerits(List.of(instanceGroupTypeMerit));
        groupTypeMerit.setHasInstance(false);
        groupType.setGroupTypeMeritList(List.of(groupTypeMerit));

        final GroupType expectedResult = new GroupType();
        expectedResult.setId(0L);
        expectedResult.setCode("code");
        expectedResult.setKpiTypeId(0L);
        expectedResult.setGroupId(0L);
        expectedResult.setWeight(0L);
        final GroupTypeMerit groupTypeMerit1 = new GroupTypeMerit();
        groupTypeMerit1.setId(0L);
        groupTypeMerit1.setMeritComponentId(0L);
        groupTypeMerit1.setGroupTypeId(0L);
        groupTypeMerit1.setWeight(0L);
        final InstanceGroupTypeMerit instanceGroupTypeMerit1 = new InstanceGroupTypeMerit();
        instanceGroupTypeMerit1.setId(0L);
        instanceGroupTypeMerit1.setGroupTypeMeritId(0L);
        instanceGroupTypeMerit1.setInstanceId(0L);
        groupTypeMerit1.setInstanceGroupTypeMerits(List.of(instanceGroupTypeMerit1));
        groupTypeMerit1.setHasInstance(false);
        expectedResult.setGroupTypeMeritList(List.of(groupTypeMerit1));

        // Configure GroupTypeRepository.getByGroupIdAndKpiTypeId(...).
        final GroupType groupType1 = new GroupType();
        groupType1.setId(0L);
        groupType1.setCode("code");
        groupType1.setKpiTypeId(0L);
        groupType1.setGroupId(0L);
        groupType1.setWeight(0L);
        final GroupTypeMerit groupTypeMerit2 = new GroupTypeMerit();
        groupTypeMerit2.setId(0L);
        groupTypeMerit2.setMeritComponentId(0L);
        groupTypeMerit2.setGroupTypeId(0L);
        groupTypeMerit2.setWeight(0L);
        final InstanceGroupTypeMerit instanceGroupTypeMerit2 = new InstanceGroupTypeMerit();
        instanceGroupTypeMerit2.setId(0L);
        instanceGroupTypeMerit2.setGroupTypeMeritId(0L);
        instanceGroupTypeMerit2.setInstanceId(0L);
        groupTypeMerit2.setInstanceGroupTypeMerits(List.of(instanceGroupTypeMerit2));
        groupTypeMerit2.setHasInstance(false);
        groupType1.setGroupTypeMeritList(List.of(groupTypeMerit2));
        when(mockRepository.getByGroupIdAndKpiTypeId(0L, 0L)).thenReturn(groupType1);

        // Run the test
        final GroupType result = groupTypeServiceUnderTest.getAllByGroupIdAndKpiTypeId(groupType);

        // Verify the results
        assertThat(result).isEqualTo(expectedResult);
    }

//    @Test
//    void testUpdate() {
//        // Setup
//        final GroupTypeDTO.Update dto = new GroupTypeDTO.Update();
//        dto.setWeight(0L);
//        dto.setGroupId(0L);
//
//        // Configure GroupTypeRepository.findById(...).
//        final GroupType groupType1 = new GroupType();
//        groupType1.setId(0L);
//        groupType1.setCode("code");
//        groupType1.setKpiTypeId(0L);
//        groupType1.setGroupId(0L);
//        groupType1.setWeight(0L);
//        final GroupTypeMerit groupTypeMerit = new GroupTypeMerit();
//        groupTypeMerit.setId(0L);
//        groupTypeMerit.setMeritComponentId(0L);
//        groupTypeMerit.setGroupTypeId(0L);
//        groupTypeMerit.setWeight(0L);
//        final InstanceGroupTypeMerit instanceGroupTypeMerit = new InstanceGroupTypeMerit();
//        instanceGroupTypeMerit.setId(0L);
//        instanceGroupTypeMerit.setGroupTypeMeritId(0L);
//        instanceGroupTypeMerit.setInstanceId(0L);
//        groupTypeMerit.setInstanceGroupTypeMerits(List.of(instanceGroupTypeMerit));
//        groupTypeMerit.setHasInstance(false);
//        groupType1.setGroupTypeMeritList(List.of(groupTypeMerit));
//        final Optional<GroupType> groupType = Optional.of(groupType1);
//        when(mockRepository.findById(0L)).thenReturn(groupType);
//
//        // Configure GroupTypeRepository.save(...).
//        final GroupType groupType2 = new GroupType();
//        groupType2.setId(0L);
//        groupType2.setCode("code");
//        groupType2.setKpiTypeId(0L);
//        groupType2.setGroupId(0L);
//        groupType2.setWeight(0L);
//        final GroupTypeMerit groupTypeMerit1 = new GroupTypeMerit();
//        groupTypeMerit1.setId(0L);
//        groupTypeMerit1.setMeritComponentId(0L);
//        groupTypeMerit1.setGroupTypeId(0L);
//        groupTypeMerit1.setWeight(0L);
//        final InstanceGroupTypeMerit instanceGroupTypeMerit1 = new InstanceGroupTypeMerit();
//        instanceGroupTypeMerit1.setId(0L);
//        instanceGroupTypeMerit1.setGroupTypeMeritId(0L);
//        instanceGroupTypeMerit1.setInstanceId(0L);
//        groupTypeMerit1.setInstanceGroupTypeMerits(List.of(instanceGroupTypeMerit1));
//        groupTypeMerit1.setHasInstance(false);
//        groupType2.setGroupTypeMeritList(List.of(groupTypeMerit1));
//        when(mockRepository.save(new GroupType())).thenReturn(groupType2);
//
//        // Configure GroupTypeMapper.entityToDtoInfo(...).
//        final GroupTypeDTO.Info info = new GroupTypeDTO.Info();
//        info.setWeight(0L);
//        info.setGroupId(0L);
//        info.setId(0L);
//        final KPITypeDTO.Info kpiType = new KPITypeDTO.Info();
//        kpiType.setId(0L);
//        final CatalogDTO.Info levelDefCatalog = new CatalogDTO.Info();
//        levelDefCatalog.setId(0L);
//        levelDefCatalog.setDescription("description");
//        levelDefCatalog.setCatalogTypeId(0L);
//        final CatalogTypeDTO catalogType = new CatalogTypeDTO();
//        catalogType.setTitle("title");
//        catalogType.setCode("code");
//        levelDefCatalog.setCatalogType(catalogType);
//        kpiType.setLevelDefCatalog(levelDefCatalog);
//        info.setKpiType(kpiType);
//        final GroupDTO.Info group = new GroupDTO.Info();
//        info.setGroup(group);
//        info.setTotalWeight(0L);
//        info.setHasAllKpiType(false);
//        when(mockMapper.entityToDtoInfo(new GroupType())).thenReturn(info);
//
//        // Run the test
//        final GroupTypeDTO.Info result = groupTypeServiceUnderTest.update(0L, dto);
//
//        // Verify the results
//        verify(mockMapper).update(eq(new GroupType()), any(GroupTypeDTO.Update.class));
//    }

    @Test
    void testUpdate_GroupTypeRepositoryFindByIdReturnsAbsent() {
        // Setup
        final GroupTypeDTO.Update dto = new GroupTypeDTO.Update();
        dto.setWeight(0L);
        dto.setGroupId(0L);

        when(mockRepository.findById(0L)).thenReturn(Optional.empty());

        // Run the test
        assertThatThrownBy(() -> groupTypeServiceUnderTest.update(0L, dto))
                .isInstanceOf(EvaluationHandleException.class);
    }

//    @Test
//    void testDelete() {
//        // Setup
//        // Configure GroupTypeRepository.findById(...).
//        final GroupType groupType1 = new GroupType();
//        groupType1.setId(0L);
//        groupType1.setCode("code");
//        groupType1.setKpiTypeId(0L);
//        groupType1.setGroupId(0L);
//        groupType1.setWeight(0L);
//        final GroupTypeMerit groupTypeMerit = new GroupTypeMerit();
//        groupTypeMerit.setId(0L);
//        groupTypeMerit.setMeritComponentId(0L);
//        groupTypeMerit.setGroupTypeId(0L);
//        groupTypeMerit.setWeight(0L);
//        final InstanceGroupTypeMerit instanceGroupTypeMerit = new InstanceGroupTypeMerit();
//        instanceGroupTypeMerit.setId(0L);
//        instanceGroupTypeMerit.setGroupTypeMeritId(0L);
//        instanceGroupTypeMerit.setInstanceId(0L);
//        groupTypeMerit.setInstanceGroupTypeMerits(List.of(instanceGroupTypeMerit));
//        groupTypeMerit.setHasInstance(false);
//        groupType1.setGroupTypeMeritList(List.of(groupTypeMerit));
//        final Optional<GroupType> groupType = Optional.of(groupType1);
//        when(mockRepository.findById(0L)).thenReturn(groupType);
//
//        // Run the test
//        groupTypeServiceUnderTest.delete(0L);
//
//        // Verify the results
//        verify(mockRepository).delete(new GroupType());
//    }

    @Test
    void testDelete_GroupTypeRepositoryFindByIdReturnsAbsent() {
        // Setup
        when(mockRepository.findById(0L)).thenReturn(Optional.empty());

        // Run the test
        assertThatThrownBy(() -> groupTypeServiceUnderTest.delete(0L)).isInstanceOf(EvaluationHandleException.class);
    }

//    @Test
//    void testSearch() throws Exception {
//        // Setup
//        final SearchDTO.SearchRq request = new SearchDTO.SearchRq();
//        request.setStartIndex(0);
//        request.setCount(0);
//        final SearchDTO.CriteriaRq criteria = new SearchDTO.CriteriaRq();
//        criteria.setFieldName("fieldName");
//        criteria.setRegex("regex");
//        criteria.setOperator(EOperator.and);
//        criteria.setValue("value");
//        criteria.setStart("start");
//        criteria.setEnd("end");
//        criteria.setCriteria(List.of(new SearchDTO.CriteriaRq()));
//        request.setCriteria(criteria);
//        request.setSortBy("sortBy");
//        request.setDistinct(false);
//
//        // Configure GroupTypeMapper.entityToDtoInfo(...).
//        final GroupTypeDTO.Info info = new GroupTypeDTO.Info();
//        info.setWeight(0L);
//        info.setGroupId(0L);
//        info.setId(0L);
//        final KPITypeDTO.Info kpiType = new KPITypeDTO.Info();
//        kpiType.setId(0L);
//        final CatalogDTO.Info levelDefCatalog = new CatalogDTO.Info();
//        levelDefCatalog.setId(0L);
//        levelDefCatalog.setDescription("description");
//        levelDefCatalog.setCatalogTypeId(0L);
//        final CatalogTypeDTO catalogType = new CatalogTypeDTO();
//        catalogType.setTitle("title");
//        catalogType.setCode("code");
//        levelDefCatalog.setCatalogType(catalogType);
//        kpiType.setLevelDefCatalog(levelDefCatalog);
//        info.setKpiType(kpiType);
//        final GroupDTO.Info group = new GroupDTO.Info();
//        info.setGroup(group);
//        info.setTotalWeight(0L);
//        info.setHasAllKpiType(false);
//        when(mockMapper.entityToDtoInfo(new GroupType())).thenReturn(info);
//
//        // Configure IKPITypeService.findAll(...).
//        final KPITypeDTO.Info kpiTypeDTO = new KPITypeDTO.Info();
//        kpiTypeDTO.setId(0L);
//        final CatalogDTO.Info levelDefCatalog1 = new CatalogDTO.Info();
//        levelDefCatalog1.setId(0L);
//        levelDefCatalog1.setDescription("description");
//        levelDefCatalog1.setCatalogTypeId(0L);
//        final CatalogTypeDTO catalogType1 = new CatalogTypeDTO();
//        catalogType1.setTitle("title");
//        catalogType1.setCode("code");
//        levelDefCatalog1.setCatalogType(catalogType1);
//        kpiTypeDTO.setLevelDefCatalog(levelDefCatalog1);
//        final List<KPITypeDTO.Info> infos = List.of(kpiTypeDTO);
//        when(mockKpiTypeService.findAll()).thenReturn(infos);
//
//        // Run the test
//        final SearchDTO.SearchRs<GroupTypeDTO.Info> result = groupTypeServiceUnderTest.search(request);
//
//        // Verify the results
//    }

//    @Test
//    void testSearch_IKPITypeServiceReturnsNoItems() throws Exception {
//        // Setup
//        final SearchDTO.SearchRq request = new SearchDTO.SearchRq();
//        request.setStartIndex(0);
//        request.setCount(0);
//        final SearchDTO.CriteriaRq criteria = new SearchDTO.CriteriaRq();
//        criteria.setFieldName("fieldName");
//        criteria.setRegex("regex");
//        criteria.setOperator(EOperator.and);
//        criteria.setValue("value");
//        criteria.setStart("start");
//        criteria.setEnd("end");
//        criteria.setCriteria(List.of(new SearchDTO.CriteriaRq()));
//        request.setCriteria(criteria);
//        request.setSortBy("sortBy");
//        request.setDistinct(false);
//
//        // Configure GroupTypeMapper.entityToDtoInfo(...).
//        final GroupTypeDTO.Info info = new GroupTypeDTO.Info();
//        info.setWeight(0L);
//        info.setGroupId(0L);
//        info.setId(0L);
//        final KPITypeDTO.Info kpiType = new KPITypeDTO.Info();
//        kpiType.setId(0L);
//        final CatalogDTO.Info levelDefCatalog = new CatalogDTO.Info();
//        levelDefCatalog.setId(0L);
//        levelDefCatalog.setDescription("description");
//        levelDefCatalog.setCatalogTypeId(0L);
//        final CatalogTypeDTO catalogType = new CatalogTypeDTO();
//        catalogType.setTitle("title");
//        catalogType.setCode("code");
//        levelDefCatalog.setCatalogType(catalogType);
//        kpiType.setLevelDefCatalog(levelDefCatalog);
//        info.setKpiType(kpiType);
//        final GroupDTO.Info group = new GroupDTO.Info();
//        info.setGroup(group);
//        info.setTotalWeight(0L);
//        info.setHasAllKpiType(false);
//        when(mockMapper.entityToDtoInfo(new GroupType())).thenReturn(info);
//
//        when(mockKpiTypeService.findAll()).thenReturn(Collections.emptyList());
//
//        // Run the test
//        final SearchDTO.SearchRs<GroupTypeDTO.Info> result = groupTypeServiceUnderTest.search(request);
//
//        // Verify the results
//    }
}
