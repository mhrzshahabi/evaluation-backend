package com.nicico.evaluation.service;

import com.nicico.evaluation.common.PageableMapper;
import com.nicico.evaluation.dto.GradeDTO;
import com.nicico.evaluation.dto.GroupDTO;
import com.nicico.evaluation.dto.GroupGradeDTO;
import com.nicico.evaluation.exception.EvaluationHandleException;
import com.nicico.evaluation.iservice.IGradeService;
import com.nicico.evaluation.mapper.GroupGradeMapper;
import com.nicico.evaluation.model.Grade;
import com.nicico.evaluation.model.Group;
import com.nicico.evaluation.model.GroupGrade;
import com.nicico.evaluation.repository.GroupGradeRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class GroupGradeServiceTest {

    @Mock
    private GroupGradeMapper mockMapper;
    @Mock
    private IGradeService mockGradeService;
    @Mock
    private PageableMapper mockPageableMapper;
    @Mock
    private GroupGradeRepository mockRepository;
    @Mock
    private ResourceBundleMessageSource mockMessageSource;

    private GroupGradeService groupGradeServiceUnderTest;

    @BeforeEach
    void setUp() {
        groupGradeServiceUnderTest = new GroupGradeService(mockMapper, mockGradeService, mockPageableMapper,
                mockRepository, mockMessageSource);
    }

    @Test
    void testGet() {
        // Setup
        // Configure GroupGradeRepository.findById(...).
        final Optional<GroupGrade> groupGrade = Optional.of(
                new GroupGrade(0L, 0L, "gradeCode", new Group(0L, "code", "title", false, null), 0L, "gradeTitle"));
        when(mockRepository.findById(0L)).thenReturn(groupGrade);

        // Configure GroupGradeMapper.entityToDtoInfo(...).
        final GroupGradeDTO.Info info = new GroupGradeDTO.Info();
        info.setGroupId(0L);
        info.setId(0L);
        info.setGradeCode("gradeCode");
        info.setGradeTitle("gradeTitle");
        final GroupDTO.Info group = new GroupDTO.Info();
        group.setId(0L);
        final GradeDTO.Info info1 = new GradeDTO.Info();
        info1.setId(0L);
        info1.setCode("code");
        info1.setTitle("gradeTitle");
        info1.setGroupId(0L);
        info1.setGroup(null);
        group.setGrade(List.of(info1));
        info.setGroup(group);
        final GradeDTO.Info grade = new GradeDTO.Info();
        grade.setId(0L);
        grade.setCode("code");
        grade.setTitle("gradeTitle");
        grade.setGroupId(0L);
        grade.setGroup(null);
        info.setGrade(grade);
        when(mockMapper.entityToDtoInfo(new GroupGrade(0L, 0L, "gradeCode", new Group(0L, "code", "title", false, null), 0L,
                "gradeTitle"))).thenReturn(info);

        // Run the test
        final GroupGradeDTO.Info result = groupGradeServiceUnderTest.get(0L);

        // Verify the results
    }

    @Test
    void testGet_GroupGradeRepositoryReturnsAbsent() {
        // Setup
        when(mockRepository.findById(0L)).thenReturn(Optional.empty());

        // Run the test
        assertThatThrownBy(() -> groupGradeServiceUnderTest.get(0L)).isInstanceOf(EvaluationHandleException.class);
    }

    @Test
    void testList() {
        // Setup
        when(mockPageableMapper.toPageable(0, 0)).thenReturn(PageRequest.of(0, 1));

        // Configure GroupGradeRepository.findAll(...).
        final Page<GroupGrade> groupGrades = new PageImpl<>(
                List.of(new GroupGrade(0L, 0L, "gradeCode", new Group(0L, "code", "title", false, null), 0L, "gradeTitle")));
        when(mockRepository.findAll(any(Pageable.class))).thenReturn(groupGrades);

        // Configure GroupGradeMapper.entityToDtoInfoList(...).
        final GroupGradeDTO.Info info = new GroupGradeDTO.Info();
        info.setGroupId(0L);
        info.setId(0L);
        info.setGradeCode("gradeCode");
        info.setGradeTitle("gradeTitle");
        final GroupDTO.Info group = new GroupDTO.Info();
        group.setId(0L);
        final GradeDTO.Info info1 = new GradeDTO.Info();
        info1.setId(0L);
        info1.setCode("code");
        info1.setTitle("gradeTitle");
        info1.setGroupId(0L);
        info1.setGroup(null);
        group.setGrade(List.of(info1));
        info.setGroup(group);
        final GradeDTO.Info grade = new GradeDTO.Info();
        grade.setId(0L);
        grade.setCode("code");
        grade.setTitle("gradeTitle");
        grade.setGroupId(0L);
        grade.setGroup(null);
        info.setGrade(grade);
        final List<GroupGradeDTO.Info> infos = List.of(info);
        when(mockMapper.entityToDtoInfoList(
                List.of(new GroupGrade(0L, 0L, "gradeCode", new Group(0L, "code", "title", false, null), 0L,
                        "gradeTitle")))).thenReturn(infos);

        // Run the test
        final GroupGradeDTO.SpecResponse result = groupGradeServiceUnderTest.list(0, 0);

        // Verify the results
    }

    @Test
    void testList_GroupGradeMapperReturnsNull() {
        // Setup
        when(mockPageableMapper.toPageable(0, 0)).thenReturn(PageRequest.of(0, 1));

        // Configure GroupGradeRepository.findAll(...).
        final Page<GroupGrade> groupGrades = new PageImpl<>(
                List.of(new GroupGrade(0L, 0L, "gradeCode", new Group(0L, "code", "title", false, null), 0L, "gradeTitle")));
        when(mockRepository.findAll(any(Pageable.class))).thenReturn(groupGrades);

        when(mockMapper.entityToDtoInfoList(
                List.of(new GroupGrade(0L, 0L, "gradeCode", new Group(0L, "code", "title", false, null), 0L,
                        "gradeTitle")))).thenReturn(null);

        // Run the test
        final GroupGradeDTO.SpecResponse result = groupGradeServiceUnderTest.list(0, 0);

        // Verify the results
    }

    @Test
    void testList_GroupGradeMapperReturnsNoItems() {
        // Setup
        when(mockPageableMapper.toPageable(0, 0)).thenReturn(PageRequest.of(0, 1));

        // Configure GroupGradeRepository.findAll(...).
        final Page<GroupGrade> groupGrades = new PageImpl<>(
                List.of(new GroupGrade(0L, 0L, "gradeCode", new Group(0L, "code", "title", false, null), 0L, "gradeTitle")));
        when(mockRepository.findAll(any(Pageable.class))).thenReturn(groupGrades);

        when(mockMapper.entityToDtoInfoList(
                List.of(new GroupGrade(0L, 0L, "gradeCode", new Group(0L, "code", "title", false, null), 0L,
                        "gradeTitle")))).thenReturn(Collections.emptyList());

        // Run the test
        final GroupGradeDTO.SpecResponse result = groupGradeServiceUnderTest.list(0, 0);

        // Verify the results
    }

    @Test
    void testCreateAll() {
        // Setup
        final GroupGradeDTO.Create create = new GroupGradeDTO.Create();
        create.setGroupId(0L);
        create.setGradeCode("code");
        create.setGradeTitle("gradeTitle");
        create.setGradeId(0L);
        final List<GroupGradeDTO.Create> requests = List.of(create);

        // Configure GroupGradeMapper.dtoCreateToEntity(...).
        final GroupGrade groupGrade = new GroupGrade(0L, 0L, "gradeCode", new Group(0L, "code", "title", false, null), 0L,
                "gradeTitle");
        when(mockMapper.dtoCreateToEntity(any(GroupGradeDTO.Create.class))).thenReturn(groupGrade);

        // Configure GroupGradeRepository.save(...).
        final GroupGrade groupGrade1 = new GroupGrade(0L, 0L, "gradeCode", new Group(0L, "code", "title", false, null), 0L,
                "gradeTitle");
        when(mockRepository.save(new GroupGrade(0L, 0L, "gradeCode", new Group(0L, "code", "title", false, null), 0L,
                "gradeTitle"))).thenReturn(groupGrade1);

        // Configure GroupGradeMapper.entityToDtoInfo(...).
        final GroupGradeDTO.Info info = new GroupGradeDTO.Info();
        info.setGroupId(0L);
        info.setId(0L);
        info.setGradeCode("gradeCode");
        info.setGradeTitle("gradeTitle");
        final GroupDTO.Info group = new GroupDTO.Info();
        group.setId(0L);
        final GradeDTO.Info info1 = new GradeDTO.Info();
        info1.setId(0L);
        info1.setCode("code");
        info1.setTitle("gradeTitle");
        info1.setGroupId(0L);
        info1.setGroup(null);
        group.setGrade(List.of(info1));
        info.setGroup(group);
        final GradeDTO.Info grade = new GradeDTO.Info();
        grade.setId(0L);
        grade.setCode("code");
        grade.setTitle("gradeTitle");
        grade.setGroupId(0L);
        grade.setGroup(null);
        info.setGrade(grade);
        when(mockMapper.entityToDtoInfo(new GroupGrade(0L, 0L, "gradeCode", new Group(0L, "code", "title", false, null), 0L,
                "gradeTitle"))).thenReturn(info);

        // Run the test
        final List<GroupGradeDTO.Info> result = groupGradeServiceUnderTest.createAll(requests);

        // Verify the results
    }

    @Test
    void testCreate() {
        // Setup
        final GroupGradeDTO.Create dto = new GroupGradeDTO.Create();
        dto.setGroupId(0L);
        dto.setGradeCode("code");
        dto.setGradeTitle("gradeTitle");
        dto.setGradeId(0L);

        // Configure GroupGradeMapper.dtoCreateToEntity(...).
        final GroupGrade groupGrade = new GroupGrade(0L, 0L, "gradeCode", new Group(0L, "code", "title", false, null), 0L,
                "gradeTitle");
        when(mockMapper.dtoCreateToEntity(any(GroupGradeDTO.Create.class))).thenReturn(groupGrade);

        // Configure GroupGradeRepository.save(...).
        final GroupGrade groupGrade1 = new GroupGrade(0L, 0L, "gradeCode", new Group(0L, "code", "title", false, null), 0L,
                "gradeTitle");
        when(mockRepository.save(new GroupGrade(0L, 0L, "gradeCode", new Group(0L, "code", "title", false, null), 0L,
                "gradeTitle"))).thenReturn(groupGrade1);

        // Configure GroupGradeMapper.entityToDtoInfo(...).
        final GroupGradeDTO.Info info = new GroupGradeDTO.Info();
        info.setGroupId(0L);
        info.setId(0L);
        info.setGradeCode("gradeCode");
        info.setGradeTitle("gradeTitle");
        final GroupDTO.Info group = new GroupDTO.Info();
        group.setId(0L);
        final GradeDTO.Info info1 = new GradeDTO.Info();
        info1.setId(0L);
        info1.setCode("code");
        info1.setTitle("gradeTitle");
        info1.setGroupId(0L);
        info1.setGroup(null);
        group.setGrade(List.of(info1));
        info.setGroup(group);
        final GradeDTO.Info grade = new GradeDTO.Info();
        grade.setId(0L);
        grade.setCode("code");
        grade.setTitle("gradeTitle");
        grade.setGroupId(0L);
        grade.setGroup(null);
        info.setGrade(grade);
        when(mockMapper.entityToDtoInfo(new GroupGrade(0L, 0L, "gradeCode", new Group(0L, "code", "title", false, null), 0L,
                "gradeTitle"))).thenReturn(info);

        // Run the test
        final GroupGradeDTO.Info result = groupGradeServiceUnderTest.create(dto);

        // Verify the results
    }



    @Test
    void testCreateGroupGrade_GroupGradeRepositoryFindAllByGradeIdInReturnsNoItems() {
        // Setup
        final GroupGradeDTO.CreateAll dto = new GroupGradeDTO.CreateAll();
        dto.setGroupId(0L);
        dto.setGradeCodes(List.of("value"));

        // Configure IGradeService.getAllByCodeIn(...).
        final GradeDTO.Info info = new GradeDTO.Info();
        info.setId(0L);
        info.setCode("code");
        info.setTitle("gradeTitle");
        info.setGroupId(0L);
        info.setGroup(null);
        final List<GradeDTO.Info> infos = List.of(info);
        when(mockGradeService.getAllByCodeIn(List.of("value"))).thenReturn(infos);

        when(mockRepository.findAllByGradeIdIn(List.of(0L))).thenReturn(Collections.emptyList());

        // Configure GroupGradeMapper.dtoCreateToEntity(...).
        final GroupGrade groupGrade = new GroupGrade(0L, 0L, "gradeCode", new Group(0L, "code", "title", false, null), 0L,
                "gradeTitle");
        when(mockMapper.dtoCreateToEntity(any(GroupGradeDTO.Create.class))).thenReturn(groupGrade);

        // Configure GroupGradeRepository.save(...).
        final GroupGrade groupGrade1 = new GroupGrade(0L, 0L, "gradeCode", new Group(0L, "code", "title", false, null), 0L,
                "gradeTitle");
        when(mockRepository.save(new GroupGrade(0L, 0L, "gradeCode", new Group(0L, "code", "title", false, null), 0L,
                "gradeTitle"))).thenReturn(groupGrade1);

        // Configure GroupGradeMapper.entityToDtoInfo(...).
        final GroupGradeDTO.Info info1 = new GroupGradeDTO.Info();
        info1.setGroupId(0L);
        info1.setId(0L);
        info1.setGradeCode("gradeCode");
        info1.setGradeTitle("gradeTitle");
        final GroupDTO.Info group = new GroupDTO.Info();
        group.setId(0L);
        final GradeDTO.Info info2 = new GradeDTO.Info();
        info2.setId(0L);
        info2.setCode("code");
        info2.setTitle("gradeTitle");
        info2.setGroupId(0L);
        info2.setGroup(null);
        group.setGrade(List.of(info2));
        info1.setGroup(group);
        final GradeDTO.Info grade = new GradeDTO.Info();
        grade.setId(0L);
        grade.setCode("code");
        grade.setTitle("gradeTitle");
        grade.setGroupId(0L);
        grade.setGroup(null);
        info1.setGrade(grade);
        when(mockMapper.entityToDtoInfo(new GroupGrade(0L, 0L, "gradeCode", new Group(0L, "code", "title", false, null), 0L,
                "gradeTitle"))).thenReturn(info1);

        // Run the test
        final List<GroupGradeDTO.Info> result = groupGradeServiceUnderTest.createGroupGrade(dto);

        // Verify the results
    }




    @Test
    void testUpdate_GroupGradeRepositoryFindByIdReturnsAbsent() {
        // Setup
        final GroupGradeDTO.CreateAll dto = new GroupGradeDTO.CreateAll();
        dto.setGroupId(0L);
        dto.setGradeCodes(List.of("value"));

        // Configure GroupGradeRepository.findAllByGroupId(...).
        final List<GroupGrade> groupGrades = List.of(
                new GroupGrade(0L, 0L, "gradeCode", new Group(0L, "code", "title", false, null), 0L, "gradeTitle"));
        when(mockRepository.findAllByGroupId(0L)).thenReturn(groupGrades);

        when(mockRepository.findById(0L)).thenReturn(Optional.empty());

        // Run the test
        assertThatThrownBy(() -> groupGradeServiceUnderTest.update(dto)).isInstanceOf(EvaluationHandleException.class);
    }



    @Test
    void testUpdate_GroupGradeRepositoryFindAllByGradeIdInReturnsNoItems() {
        // Setup
        final GroupGradeDTO.CreateAll dto = new GroupGradeDTO.CreateAll();
        dto.setGroupId(0L);
        dto.setGradeCodes(List.of("value"));

        // Configure GroupGradeRepository.findAllByGroupId(...).
        final List<GroupGrade> groupGrades = List.of(
                new GroupGrade(0L, 0L, "gradeCode", new Group(0L, "code", "title", false, null), 0L, "gradeTitle"));
        when(mockRepository.findAllByGroupId(0L)).thenReturn(groupGrades);

        // Configure GroupGradeRepository.findById(...).
        final Optional<GroupGrade> groupGrade = Optional.of(
                new GroupGrade(0L, 0L, "gradeCode", new Group(0L, "code", "title", false, null), 0L, "gradeTitle"));
        when(mockRepository.findById(0L)).thenReturn(groupGrade);

        // Configure IGradeService.getAllByCodeIn(...).
        final GradeDTO.Info info = new GradeDTO.Info();
        info.setId(0L);
        info.setCode("code");
        info.setTitle("gradeTitle");
        info.setGroupId(0L);
        info.setGroup(null);
        final List<GradeDTO.Info> infos = List.of(info);
        when(mockGradeService.getAllByCodeIn(List.of("value"))).thenReturn(infos);

        when(mockRepository.findAllByGradeIdIn(List.of(0L))).thenReturn(Collections.emptyList());

        // Configure GroupGradeMapper.dtoCreateToEntity(...).
        final GroupGrade groupGrade1 = new GroupGrade(0L, 0L, "gradeCode", new Group(0L, "code", "title", false, null), 0L,
                "gradeTitle");
        when(mockMapper.dtoCreateToEntity(any(GroupGradeDTO.Create.class))).thenReturn(groupGrade1);

        // Configure GroupGradeRepository.save(...).
        final GroupGrade groupGrade2 = new GroupGrade(0L, 0L, "gradeCode", new Group(0L, "code", "title", false, null), 0L,
                "gradeTitle");
        when(mockRepository.save(new GroupGrade(0L, 0L, "gradeCode", new Group(0L, "code", "title", false, null), 0L,
                "gradeTitle"))).thenReturn(groupGrade2);

        // Configure GroupGradeMapper.entityToDtoInfo(...).
        final GroupGradeDTO.Info info1 = new GroupGradeDTO.Info();
        info1.setGroupId(0L);
        info1.setId(0L);
        info1.setGradeCode("gradeCode");
        info1.setGradeTitle("gradeTitle");
        final GroupDTO.Info group = new GroupDTO.Info();
        group.setId(0L);
        final GradeDTO.Info info2 = new GradeDTO.Info();
        info2.setId(0L);
        info2.setCode("code");
        info2.setTitle("gradeTitle");
        info2.setGroupId(0L);
        info2.setGroup(null);
        group.setGrade(List.of(info2));
        info1.setGroup(group);
        final GradeDTO.Info grade = new GradeDTO.Info();
        grade.setId(0L);
        grade.setCode("code");
        grade.setTitle("gradeTitle");
        grade.setGroupId(0L);
        grade.setGroup(null);
        info1.setGrade(grade);
        when(mockMapper.entityToDtoInfo(new GroupGrade(0L, 0L, "gradeCode", new Group(0L, "code", "title", false, null), 0L,
                "gradeTitle"))).thenReturn(info1);

        // Run the test
        final List<GroupGradeDTO.Info> result = groupGradeServiceUnderTest.update(dto);

        // Verify the results
        verify(mockRepository).delete(
                new GroupGrade(0L, 0L, "gradeCode", new Group(0L, "code", "title", false, null), 0L, "gradeTitle"));
    }


    @Test
    void testDeleteAll() {
        // Setup
        // Configure GroupGradeRepository.findById(...).
        final Optional<GroupGrade> groupGrade = Optional.of(
                new GroupGrade(0L, 0L, "gradeCode", new Group(0L, "code", "title", false, null), 0L, "gradeTitle"));
        when(mockRepository.findById(0L)).thenReturn(groupGrade);

        // Run the test
        groupGradeServiceUnderTest.deleteAll(List.of(0L));

        // Verify the results
        verify(mockRepository).delete(
                new GroupGrade(0L, 0L, "gradeCode", new Group(0L, "code", "title", false, null), 0L, "gradeTitle"));
    }

    @Test
    void testDeleteAll_GroupGradeRepositoryFindByIdReturnsAbsent() {
        // Setup
        when(mockRepository.findById(0L)).thenReturn(Optional.empty());

        // Run the test
        assertThatThrownBy(() -> groupGradeServiceUnderTest.deleteAll(List.of(0L)))
                .isInstanceOf(EvaluationHandleException.class);
    }

    @Test
    void testDelete() {
        // Setup
        // Configure GroupGradeRepository.findById(...).
        final Optional<GroupGrade> groupGrade = Optional.of(
                new GroupGrade(0L, 0L, "gradeCode", new Group(0L, "code", "title", false, null), 0L, "gradeTitle"));
        when(mockRepository.findById(0L)).thenReturn(groupGrade);

        // Run the test
        groupGradeServiceUnderTest.delete(0L);

        // Verify the results
        verify(mockRepository).delete(
                new GroupGrade(0L, 0L, "gradeCode", new Group(0L, "code", "title", false, null), 0L, "gradeTitle"));
    }

    @Test
    void testDelete_GroupGradeRepositoryFindByIdReturnsAbsent() {
        // Setup
        when(mockRepository.findById(0L)).thenReturn(Optional.empty());

        // Run the test
        assertThatThrownBy(() -> groupGradeServiceUnderTest.delete(0L)).isInstanceOf(EvaluationHandleException.class);
    }

    @Test
    void testGetGroupGradeByGrade() {
        // Setup
        final Grade grade = new Grade(0L, "code", "title", 0L);

        // Configure GroupGradeRepository.findFirstByGradeCode(...).
        final Optional<GroupGrade> groupGrade = Optional.of(
                new GroupGrade(0L, 0L, "gradeCode", new Group(0L, "code", "title", false, null), 0L, "gradeTitle"));
        when(mockRepository.findFirstByGradeCode("code")).thenReturn(groupGrade);

        // Configure GroupGradeMapper.entityToDtoInfo(...).
        final GroupGradeDTO.Info info = new GroupGradeDTO.Info();
        info.setGroupId(0L);
        info.setId(0L);
        info.setGradeCode("gradeCode");
        info.setGradeTitle("gradeTitle");
        final GroupDTO.Info group = new GroupDTO.Info();
        group.setId(0L);
        final GradeDTO.Info info1 = new GradeDTO.Info();
        info1.setId(0L);
        info1.setCode("code");
        info1.setTitle("gradeTitle");
        info1.setGroupId(0L);
        info1.setGroup(null);
        group.setGrade(List.of(info1));
        info.setGroup(group);
        final GradeDTO.Info grade1 = new GradeDTO.Info();
        grade1.setId(0L);
        grade1.setCode("code");
        grade1.setTitle("gradeTitle");
        grade1.setGroupId(0L);
        grade1.setGroup(null);
        info.setGrade(grade1);
        when(mockMapper.entityToDtoInfo(new GroupGrade(0L, 0L, "gradeCode", new Group(0L, "code", "title", false, null), 0L,
                "gradeTitle"))).thenReturn(info);

        // Run the test
        final GroupGradeDTO.Info result = groupGradeServiceUnderTest.getGroupGradeByGrade(grade);

        // Verify the results
    }

    @Test
    void testGetGroupGradeByGrade_GroupGradeRepositoryReturnsAbsent() {
        // Setup
        final Grade grade = new Grade(0L, "code", "title", 0L);
        when(mockRepository.findFirstByGradeCode("code")).thenReturn(Optional.empty());

        // Run the test
        final GroupGradeDTO.Info result = groupGradeServiceUnderTest.getGroupGradeByGrade(grade);

        // Verify the results
        assertThat(result).isNull();
    }

    @Test
    void testGetGroupGradeByGroup() {
        // Setup
        final Group group = new Group(0L, "code", "title", false, null);

        // Configure GroupGradeRepository.findAllByGroupId(...).
        final List<GroupGrade> groupGrades = List.of(
                new GroupGrade(0L, 0L, "gradeCode", new Group(0L, "code", "title", false, null), 0L, "gradeTitle"));
        when(mockRepository.findAllByGroupId(0L)).thenReturn(groupGrades);

        // Configure GroupGradeMapper.entityToDtoInfo(...).
        final GroupGradeDTO.Info info = new GroupGradeDTO.Info();
        info.setGroupId(0L);
        info.setId(0L);
        info.setGradeCode("gradeCode");
        info.setGradeTitle("gradeTitle");
        final GroupDTO.Info group1 = new GroupDTO.Info();
        group1.setId(0L);
        final GradeDTO.Info info1 = new GradeDTO.Info();
        info1.setId(0L);
        info1.setCode("code");
        info1.setTitle("gradeTitle");
        info1.setGroupId(0L);
        info1.setGroup(null);
        group1.setGrade(List.of(info1));
        info.setGroup(group1);
        final GradeDTO.Info grade = new GradeDTO.Info();
        grade.setId(0L);
        grade.setCode("code");
        grade.setTitle("gradeTitle");
        grade.setGroupId(0L);
        grade.setGroup(null);
        info.setGrade(grade);
        when(mockMapper.entityToDtoInfo(new GroupGrade(0L, 0L, "gradeCode", new Group(0L, "code", "title", false, null), 0L,
                "gradeTitle"))).thenReturn(info);

        // Run the test
        final List<GroupGradeDTO.Info> result = groupGradeServiceUnderTest.getGroupGradeByGroup(group);

        // Verify the results
    }

    @Test
    void testGetGroupGradeByGroup_GroupGradeRepositoryReturnsNoItems() {
        // Setup
        final Group group = new Group(0L, "code", "title", false, null);
        when(mockRepository.findAllByGroupId(0L)).thenReturn(Collections.emptyList());

        // Run the test
        final List<GroupGradeDTO.Info> result = groupGradeServiceUnderTest.getGroupGradeByGroup(group);

        // Verify the results
        assertThat(result).isEqualTo(Collections.emptyList());
    }
}
