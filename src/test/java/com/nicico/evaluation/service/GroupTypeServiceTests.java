package com.nicico.evaluation.service;


import com.nicico.copper.common.dto.search.SearchDTO;
import com.nicico.evaluation.common.PageableMapper;
import com.nicico.evaluation.dto.GroupTypeDTO;
import com.nicico.evaluation.exception.EvaluationHandleException;
import com.nicico.evaluation.mapper.GroupTypeMapper;
import com.nicico.evaluation.model.GroupType;
import com.nicico.evaluation.repository.GroupTypeRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.*;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class GroupTypeServiceTests {

    @InjectMocks
    public GroupTypeService groupTypeService;

    @Mock
    private GroupTypeRepository groupTypeRepository;
    @Mock
    private GroupTypeMapper groupTypeMapper;
    @Mock
    private PageableMapper pageableMapper;

    @Test
    public void getByIdTest() {
        //init
        Optional<GroupType> groupType = Optional.of(generateGroupType("testCode1", 1L, 1L, 10L));
        groupType.get().setId(1L);
        GroupTypeDTO.Info groupTypeInfo = generateGroupTypeInfo(1L, "testCode1", 1L, 1L, 10L);
        //act
        when(groupTypeRepository.findById(anyLong())).thenReturn(groupType);
        when(groupTypeMapper.entityToDtoInfo(any(GroupType.class))).thenReturn(groupTypeInfo);
        GroupTypeDTO.Info groupTypeInfoRes = groupTypeService.get(1L);
        //assert
        assertNotNull(groupTypeInfoRes);
        assertEquals(groupTypeInfoRes.getId(), groupTypeInfo.getId());
        assertEquals(groupTypeInfoRes.getCode(), groupTypeInfo.getCode());
        assertEquals(groupTypeInfoRes.getGroupId(), groupTypeInfo.getGroupId());
        assertEquals(groupTypeInfoRes.getKpiTypeId(), groupTypeInfo.getKpiTypeId());
        assertEquals(groupTypeInfoRes.getWeight(), groupTypeInfo.getWeight());
    }

    @Test
    public void listPageableTest() {
        //init
        List<GroupType> groupTypes = generateGroupTypeList();
        List<GroupTypeDTO.Info> groupTypeInfoList = generateGroupTypeInfoList();
        Page<GroupType> pageGroupType = new PageImpl<>(groupTypes);
        Pageable pageable = PageRequest.of(0, 10, Sort.by(Sort.Direction.DESC, "id"));
        //Act
        when(groupTypeRepository.findAll(any(Pageable.class))).thenReturn(pageGroupType);
        when(groupTypeMapper.entityToDtoInfoList(anyList())).thenReturn(groupTypeInfoList);
        when(pageableMapper.toPageable(anyInt(), anyInt())).thenReturn(pageable);
        GroupTypeDTO.SpecResponse specResponse = groupTypeService.list(10, 0);
        //Assert
        assertNotNull(specResponse);
        assertEquals(specResponse.getResponse().getData().size(), groupTypeInfoList.size());
        GroupTypeDTO.Info groupType0 = specResponse.getResponse().getData().get(0);
        assertEquals(groupType0.getCode(), "testCode1");
        assertEquals(groupType0.getGroupId(), 1L);
        assertEquals(groupType0.getKpiTypeId(), 1L);
        assertEquals(groupType0.getWeight(), 10L);
        GroupTypeDTO.Info groupType1 = specResponse.getResponse().getData().get(1);
        assertEquals(groupType1.getCode(), "testCode2");
        assertEquals(groupType1.getGroupId(), 1L);
        assertEquals(groupType1.getKpiTypeId(), 1L);
        assertEquals(groupType1.getWeight(), 10L);
    }

    @Test
    public void searchTest() throws IllegalAccessException, NoSuchFieldException {
        //init
        SearchDTO.SearchRq request = new SearchDTO.SearchRq();
        //act
        SearchDTO.SearchRs<GroupTypeDTO.Info> result = groupTypeService.search(request);
        //assert
        assertNotNull(result);
    }


    @Test
    public void createTest() {
        //init
        GroupTypeDTO.Create groupTypeCreate = new GroupTypeDTO.Create();
        groupTypeCreate.setCode("testCode");
        groupTypeCreate.setGroupId(1L);
        groupTypeCreate.setKpiTypeId(1L);
        groupTypeCreate.setWeight(10L);

        GroupType groupType = generateGroupType("testCode", 1L, 1L, 10L);
        groupType.setId(1L);

        GroupTypeDTO.Info groupTypeInfo = generateGroupTypeInfo(1L, "testCode", 1L, 1L, 10L);

        //Action
        when(groupTypeRepository.save(any(GroupType.class))).thenReturn(groupType);
        when(groupTypeMapper.dtoCreateToEntity(any(GroupTypeDTO.Create.class))).thenReturn(groupType);
        when(groupTypeMapper.entityToDtoInfo(any(GroupType.class))).thenReturn(groupTypeInfo);
        GroupTypeDTO.Info groupTypeInfoRes = groupTypeService.create(groupTypeCreate);

        //Assert
        assertNotNull(groupTypeInfoRes);
        assertEquals(groupTypeInfoRes.getId(), groupType.getId());
        assertEquals(groupTypeInfoRes.getCode(), groupTypeInfo.getCode());
        assertEquals(groupTypeInfoRes.getGroupId(), groupTypeInfo.getGroupId());
        assertEquals(groupTypeInfoRes.getKpiTypeId(), groupTypeInfo.getKpiTypeId());
        assertEquals(groupTypeInfoRes.getWeight(), groupTypeInfo.getWeight());

    }

    @Test
    public void createExceptionTest() {
        //init
        GroupTypeDTO.Create groupTypeCreate = new GroupTypeDTO.Create();
        groupTypeCreate.setCode("testCode");
        groupTypeCreate.setWeight(10L);

        //Action
        when(groupTypeRepository.save(any(GroupType.class))).thenThrow(EvaluationHandleException.class);

        assertThrows(EvaluationHandleException.class, () -> {
            groupTypeService.create(groupTypeCreate);
        });
    }

//    @Test
//    public void updateTest() {
//        //init
//        GroupTypeDTO.Update groupTypeUpdate = new GroupTypeDTO.Update();
//        groupTypeUpdate.setId(1L).setCode("NEWtestCode1").setGroupId(1L).setKpiTypeId(1L).setWeight(10L);
//        GroupType groupType = generateGroupType("OLDtestCode1", 1L, 1L, 10L);
//        groupType.setId(1L);
//        Optional<GroupType> oldGroupType = Optional.of(groupType);
//        GroupType newGroupType = generateGroupType("NEWtestCode1", 1L, 1L, 10L);
//        GroupTypeDTO.Info groupTypeInfo = generateGroupTypeInfo(1L, "NEWtestCode1", 1L, 1L, 10L);
//
//        //act
//        when(groupTypeRepository.findById(anyLong())).thenReturn(oldGroupType);
//        when(groupTypeRepository.save(any(GroupType.class))).thenReturn(newGroupType);
//        when(groupTypeMapper.entityToDtoInfo(any(GroupType.class))).thenReturn(groupTypeInfo);
//        GroupTypeDTO.Info groupTypeInfoRes = groupTypeService.update(groupTypeUpdate);
//
//        //assert
//        assertNotNull(groupTypeInfoRes);
//        assertEquals(groupTypeInfoRes.getId(), groupType.getId());
//        assertEquals(groupTypeInfoRes.getCode(), groupTypeUpdate.getCode());
//        assertEquals(groupTypeInfoRes.getGroupId(), groupTypeUpdate.getGroupId());
//        assertEquals(groupTypeInfoRes.getKpiTypeId(), groupTypeUpdate.getKpiTypeId());
//        assertEquals(groupTypeInfoRes.getWeight(), groupTypeUpdate.getWeight());
//
//    }

    @Test
    public void deleteTest() {
        //init
        Optional<GroupType> groupType = Optional.of(generateGroupType("deleteCode", 1L, 1L, 10L));
        //act
        when(groupTypeRepository.findById(anyLong())).thenReturn(groupType);
        doNothing().when(groupTypeRepository).delete(any(GroupType.class));
        groupTypeService.delete(1L);
        //assert
        verify(groupTypeRepository, times(1)).delete(groupType.get());
    }

    @Test
    public void getByIdExceptionTest() {
        //init
        GroupType groupType = generateGroupType("testCode1", 1L, 1L, 10L);
        groupType.setId(1L);
        Optional<GroupType> groupTypeOP = Optional.of(groupType);
        //act
        when(groupTypeRepository.findById(2L)).thenReturn(groupTypeOP);
        //assert
        assertThrows(RuntimeException.class, () -> {
            groupTypeService.get(groupType.getId());
        });
    }
//
//


    private GroupType generateGroupType(String code, Long groupId, Long KpiTypeId, Long weight) {
        GroupType groupType = new GroupType();
        groupType.setCode(code);
        groupType.setGroupId(groupId);
        groupType.setKpiTypeId(KpiTypeId);
        groupType.setWeight(weight);
        return groupType;
    }

    private List<GroupType> generateGroupTypeList() {
        return Arrays.asList(
                generateGroupType("testCode1", 1L, 1L, 10L),
                generateGroupType("testCode2", 1L, 1L, 10L)
        );
    }

    private GroupTypeDTO.Info generateGroupTypeInfo(Long id, String code, Long groupId, Long KpiTypeId, Long weight) {
        GroupTypeDTO.Info groupTypeInfo = new GroupTypeDTO.Info();
        groupTypeInfo.setId(id);
        groupTypeInfo.setCode(code);
        groupTypeInfo.setGroupId(groupId);
        groupTypeInfo.setKpiTypeId(KpiTypeId);
        groupTypeInfo.setWeight(weight);
        return groupTypeInfo;
    }

    private List<GroupTypeDTO.Info> generateGroupTypeInfoList() {
        return Arrays.asList(
                generateGroupTypeInfo(1L, "testCode1", 1L, 1L, 10L),
                generateGroupTypeInfo(2L, "testCode2", 1L, 1L, 10L)
        );
    }


}
