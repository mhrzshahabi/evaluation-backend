package com.nicico.evaluation.mapper;

import com.nicico.evaluation.dto.GroupTypeDTO;
import com.nicico.evaluation.model.GroupType;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
public class GroupTypeMapperTests {

    @InjectMocks
    private GroupTypeMapperImpl groupTypeMapper;

    @Test
    public void dtoCreateToEntityTest() {
        //init
        GroupTypeDTO.Create groupTypeCreate = new GroupTypeDTO.Create();
        groupTypeCreate.setGroupId(1L).setCode("testCode1").setKpiTypeId(1L).setWeight(10L);
        GroupType groupType = generateGroupType(null, 1L, 1L, 10L);
        //act
        GroupType groupTypeRes = groupTypeMapper.dtoCreateToEntity(groupTypeCreate);
        //assert
        assertNotNull(groupTypeRes);
        assertNull(groupTypeRes.getId());
        assertEquals(groupTypeRes.getCode(), groupTypeCreate.getCode());
        assertEquals(groupTypeRes.getGroupId(), groupTypeCreate.getGroupId());
        assertEquals(groupTypeRes.getKpiTypeId(), groupTypeCreate.getKpiTypeId());
        assertEquals(groupTypeRes.getWeight(), groupTypeCreate.getWeight());
    }

    @Test
    public void entityToDtoInfoTest() {
        //init
        GroupTypeDTO.Info groupTypeInfo = generateGroupTypeInfo(1L, "testCode1", 1L, 1L, 10L);
        GroupType groupType = generateGroupType("testCode1", 1L, 1L, 10L);
        //act
        GroupTypeDTO.Info groupTypeInfoRes = groupTypeMapper.entityToDtoInfo(groupType);
        //assert
        assertNotNull(groupTypeInfoRes);
        assertNull(groupTypeInfoRes.getGroup());
        assertNull(groupTypeInfoRes.getKpiType());
        assertNull(groupTypeInfoRes.getId());
        assertEquals(groupTypeInfoRes.getCode(), groupTypeInfo.getCode());
        assertEquals(groupTypeInfoRes.getGroupId(), groupTypeInfo.getGroupId());
        assertEquals(groupTypeInfoRes.getKpiTypeId(), groupTypeInfo.getKpiTypeId());
        assertEquals(groupTypeInfoRes.getWeight(), groupTypeInfo.getWeight());
    }

    @Test
    public void entityToDtoInfoListTest() {
        //init
        List<GroupTypeDTO.Info> groupTypeInfos = generateGroupTypeInfoList();
        List<GroupType> groupTypes = generateGroupTypeList();
        //act
        List<GroupTypeDTO.Info> groupTypeInfoRes = groupTypeMapper.entityToDtoInfoList(groupTypes);
        //assert
        assertNotNull(groupTypeInfoRes);
        assertEquals(groupTypeInfoRes.size(), groupTypeInfos.size());

        assertNull(groupTypeInfoRes.get(0).getGroup());
        assertNull(groupTypeInfoRes.get(0).getKpiType());
        assertEquals(groupTypeInfoRes.get(0).getId(), groupTypeInfoRes.get(0).getId());
        assertEquals(groupTypeInfoRes.get(0).getCode(), groupTypeInfoRes.get(0).getCode());
        assertEquals(groupTypeInfoRes.get(0).getGroupId(), groupTypeInfoRes.get(0).getGroupId());
        assertEquals(groupTypeInfoRes.get(0).getKpiTypeId(), groupTypeInfoRes.get(0).getKpiTypeId());
        assertEquals(groupTypeInfoRes.get(0).getWeight(), groupTypeInfoRes.get(0).getWeight());

        assertNull(groupTypeInfoRes.get(1).getGroup());
        assertNull(groupTypeInfoRes.get(1).getKpiType());
        assertEquals(groupTypeInfoRes.get(1).getId(), groupTypeInfoRes.get(1).getId());
        assertEquals(groupTypeInfoRes.get(1).getCode(), groupTypeInfoRes.get(1).getCode());
        assertEquals(groupTypeInfoRes.get(1).getGroupId(), groupTypeInfoRes.get(1).getGroupId());
        assertEquals(groupTypeInfoRes.get(1).getKpiTypeId(), groupTypeInfoRes.get(1).getKpiTypeId());
        assertEquals(groupTypeInfoRes.get(1).getWeight(), groupTypeInfoRes.get(1).getWeight());
    }

    @Test
    public void updateTest() {
        //init
        GroupTypeDTO.Update groupTypeUpdate = new GroupTypeDTO.Update();
        groupTypeUpdate.setId(1L).setCode("testCode1").setGroupId(1L).setKpiTypeId(1L).setWeight(10L);
        GroupType groupType = generateGroupType(null, null, null, null);
        //act
        groupTypeMapper.update(groupType, groupTypeUpdate);
        //assert
        assertEquals(groupType.getId(), groupTypeUpdate.getId());
        assertEquals(groupType.getCode(), groupTypeUpdate.getCode());
        assertEquals(groupType.getGroupId(), groupTypeUpdate.getGroupId());
        assertEquals(groupType.getKpiTypeId(), groupTypeUpdate.getKpiTypeId());
    }


    private GroupType generateGroupType(String code, Long groupId, Long kpiTypeId, Long weight) {
        GroupType groupType = new GroupType();
        groupType.setCode(code);
        groupType.setGroupId(groupId);
        groupType.setKpiTypeId(kpiTypeId);
        groupType.setWeight(weight);
        return groupType;
    }

    private List<GroupType> generateGroupTypeList() {
        return Arrays.asList(
                generateGroupType("testCode1", 1L, 1L, 10L),
                generateGroupType("testCode2", 1L, 1L, 10L)
        );
    }

    private GroupTypeDTO.Info generateGroupTypeInfo(Long id, String code, Long groupId, Long kpiTypeId, Long weight) {
        GroupTypeDTO.Info groupTypeInfo = new GroupTypeDTO.Info();
        groupTypeInfo.setId(id);
        groupTypeInfo.setCode(code);
        groupTypeInfo.setGroupId(groupId);
        groupTypeInfo.setKpiTypeId(kpiTypeId);
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
