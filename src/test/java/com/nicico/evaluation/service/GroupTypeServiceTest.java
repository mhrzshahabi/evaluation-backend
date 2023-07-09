package com.nicico.evaluation.service;

import com.nicico.evaluation.common.PageableMapper;
import com.nicico.evaluation.dto.CatalogDTO;
import com.nicico.evaluation.dto.CatalogTypeDTO;
import com.nicico.evaluation.dto.GroupTypeDTO;
import com.nicico.evaluation.dto.KPITypeDTO;
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

    @Test
    void testGetWeightInfoByGroupId_GroupTypeRepositoryReturnsNoItems() {
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

        when(mockRepository.getAllByGroupId(0L)).thenReturn(Collections.emptyList());

        // Run the test
        final GroupTypeDTO.GroupTypeMaxWeight result = groupTypeServiceUnderTest.getWeightInfoByGroupId(0L);

        // Verify the results
    }

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

    @Test
    void testDelete_GroupTypeRepositoryFindByIdReturnsAbsent() {
        // Setup
        when(mockRepository.findById(0L)).thenReturn(Optional.empty());

        // Run the test
        assertThatThrownBy(() -> groupTypeServiceUnderTest.delete(0L)).isInstanceOf(EvaluationHandleException.class);
    }

}
