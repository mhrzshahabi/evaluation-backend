package com.nicico.evaluation.mapper;

import com.nicico.evaluation.dto.GroupDTO;
import com.nicico.evaluation.model.Group;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;


@ExtendWith(MockitoExtension.class)
public class GroupMapperTests {

    @InjectMocks
    private GroupMapperImpl groupMapper;

    @Test
    public void dtoCreateToEntityTest() {
        //init
        GroupDTO.Create groupCreate = new GroupDTO.Create();
        groupCreate.setCode("testCode1").setTitle("testTitle1").setDefinitionAllowed(Boolean.TRUE);
        Group group = generateGroup(null, "testCode1", "testTitle1", Boolean.TRUE);
        //act
        Group groupRes = groupMapper.dtoCreateToEntity(groupCreate);
        //assert
        assertNotNull(groupRes);
        assertNull(groupRes.getId());
        assertEquals(groupRes.getCode(), group.getCode());
        assertEquals(groupRes.getTitle(), group.getTitle());
        assertEquals(groupRes.getDefinitionAllowed(), group.getDefinitionAllowed());
    }

//    @Test
//    public void entityToDtoInfoTest() {
//        //init
//        GroupDTO.Info groupInfo = generateGroupInfo(1L, "testCode1", "testTitle1", Boolean.TRUE);
//        Group group = generateGroup(1L, "testCode1", "testTitle1", Boolean.TRUE);
//        //act
//        GroupDTO.Info groupInfoRes = groupMapper.entityToDtoInfo(group);
//        //assert
//        assertNotNull(groupInfoRes);
//        assertEquals(groupInfoRes.getId(), group.getId());
//        assertEquals(groupInfoRes.getCode(), group.getCode());
//        assertEquals(groupInfoRes.getTitle(), group.getTitle());
//        assertEquals(groupInfoRes.getDefinitionAllowed(), group.getDefinitionAllowed());
//    }

//    @Test
//    public void entityToDtoInfoListTest() {
//        //init
//        List<GroupDTO.Info> groupInfos = generateGroupInfoList();
//        List<Group> groups = generateGroupList();
//        //act
//        List<GroupDTO.Info> groupInfosRes = groupMapper.entityToDtoInfoList(groups);
//        //assert
//        assertNotNull(groupInfosRes);
//        assertEquals(groupInfosRes.size(), groupInfos.size());
//        assertEquals(groupInfosRes.get(0).getId(), groupInfos.get(0).getId());
//        assertEquals(groupInfosRes.get(0).getCode(), groupInfos.get(0).getCode());
//        assertEquals(groupInfosRes.get(0).getTitle(), groupInfos.get(0).getTitle());
//        assertEquals(groupInfosRes.get(0).getDefinitionAllowed(), groupInfos.get(0).getDefinitionAllowed());
//
//        assertEquals(groupInfosRes.get(1).getId(), groupInfos.get(1).getId());
//        assertEquals(groupInfosRes.get(1).getCode(), groupInfos.get(1).getCode());
//        assertEquals(groupInfosRes.get(1).getTitle(), groupInfos.get(1).getTitle());
//        assertEquals(groupInfosRes.get(1).getDefinitionAllowed(), groupInfos.get(1).getDefinitionAllowed());
//    }

//    @Test
//    public void updateTest() {
//        //init
//        GroupDTO.Update groupUpdate = new GroupDTO.Update();
//        groupUpdate.setId(1L).setCode("testCode1").setTitle("testTitle1").setDefinitionAllowed(Boolean.TRUE);
//        Group group = generateGroup(null, null, null, null);
//        //act
//        groupMapper.update(group, groupUpdate);
//        //assert
//        assertEquals(group.getId(), groupUpdate.getId());
//        assertEquals(group.getCode(), groupUpdate.getCode());
//        assertEquals(group.getTitle(), groupUpdate.getTitle());
//        assertEquals(group.getDefinitionAllowed(), groupUpdate.getDefinitionAllowed());
//    }


    private Group generateGroup(Long id, String code, String title, Boolean definitionAllowed) {
        Group group1 = new Group();
        group1.setId(id);
        group1.setCode(code);
        group1.setTitle(title);
        group1.setDefinitionAllowed(definitionAllowed);
        return group1;
    }

    private List<Group> generateGroupList() {
        return Arrays.asList(
                generateGroup(1L, "testCode1", "testTitle1", Boolean.TRUE),
                generateGroup(2L, "testCode2", "testTitle2", Boolean.FALSE)
        );
    }

    private GroupDTO.Info generateGroupInfo(Long id, String code, String title, Boolean definitionAllowed) {
        GroupDTO.Info groupInfo1 = new GroupDTO.Info();
        groupInfo1.setId(id);
        groupInfo1.setCode(code);
        groupInfo1.setTitle(title);
        groupInfo1.setDefinitionAllowed(definitionAllowed);
        return groupInfo1;
    }

    private List<GroupDTO.Info> generateGroupInfoList() {
        return Arrays.asList(
                generateGroupInfo(1L, "testCode1", "testTitle1", Boolean.TRUE),
                generateGroupInfo(2L, "testCode2", "testTitle2", Boolean.FALSE)
        );
    }

}
