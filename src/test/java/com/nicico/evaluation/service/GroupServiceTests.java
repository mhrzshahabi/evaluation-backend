package com.nicico.evaluation.service;

import com.nicico.copper.common.dto.search.SearchDTO;
import com.nicico.evaluation.common.PageableMapper;
import com.nicico.evaluation.dto.GroupDTO;
import com.nicico.evaluation.mapper.GroupMapper;
import com.nicico.evaluation.model.Group;
import com.nicico.evaluation.repository.GroupRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.*;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
//@SpringBootTest(classes = {GroupMapperImpl.class})
public class GroupServiceTests {

    @InjectMocks
    public GroupService groupService;

    @Mock
    private GroupRepository groupRepository;
    @Mock
    private GroupMapper groupMapper;
    @Mock
    private PageableMapper pageableMapper;


    @Test
    public void listPageableTest() {
        //init
        List<Group> groups = generateGroupList();
        List<GroupDTO.Info> groupInfos = generateGroupInfoList();
        Page<Group> pageGroup = new PageImpl<>(groups);
        Pageable pageable = PageRequest.of(0, 10, Sort.by(Sort.Direction.DESC, "id"));
        //Act
        when(groupRepository.findAll(any(Pageable.class))).thenReturn(pageGroup);
        when(groupMapper.entityToDtoInfoList(anyList())).thenReturn(groupInfos);
        when(pageableMapper.toPageable(anyInt(), anyInt())).thenReturn(pageable);
        GroupDTO.SpecResponse specResponse = groupService.list(10, 0);
        //Assert
        assertNotNull(specResponse);
        assertEquals(specResponse.getResponse().getData().size(), 2);
        GroupDTO.Info gi0 = specResponse.getResponse().getData().get(0);
        assertEquals(gi0.getCode(), "testCode1");
        GroupDTO.Info gi1 = specResponse.getResponse().getData().get(1);
        assertEquals(gi1.getCode(), "testCode2");
    }

    @Test
    public void getByIdTest() {
        //init
        Optional<Group> group = Optional.of(generateGroup("testCode1", "testTitle1", Boolean.FALSE));
        GroupDTO.Info groupInfo = generateGroupInfo(1L, "testCode1", "testTitle1", Boolean.FALSE);
        //act
        when(groupRepository.findById(anyLong())).thenReturn(group);
        when(groupMapper.entityToDtoInfo(any(Group.class))).thenReturn(groupInfo);
        GroupDTO.Info groupRes = groupService.get(1L);
        //assert
        assertNotNull(groupRes);
        assertEquals(groupRes.getCode(), groupInfo.getCode());
        assertEquals(groupRes.getDefinitionAllowed(), groupInfo.getDefinitionAllowed());
    }

    @Test
    public void getByIdExceptionTest() {
        //init
        Group group = generateGroup("testCode1", "testTitle1", Boolean.FALSE);
        group.setId(1L);
        Optional<Group> groupOP = Optional.of(group);
        //act
        when(groupRepository.findById(2L)).thenReturn(groupOP);
        //assert
        assertThrows(RuntimeException.class, () -> {
            groupService.get(group.getId());
        });
    }

//    @Test
//    public void updateTest() {
//        //init
//        GroupDTO.Update groupUpdate = new GroupDTO.Update();
//        groupUpdate.setId(1L).setCode("NEWtestCode1").setTitle("NEWtestTitle1").setDefinitionAllowed(Boolean.TRUE);
//        Group group = generateGroup("OLDtestCode1", "OLDtestTitle1", Boolean.FALSE);
//        group.setId(1L);
//        Optional<Group> oldGroup = Optional.of(group);
//        Group newGroup = generateGroup("NEWtestCode1", "NEWtestTitle1", Boolean.TRUE);
//        GroupDTO.Info groupInfo = generateGroupInfo(1L, "NEWtestCode1", "NEWtestTitle1", Boolean.TRUE);
//
//        //act
//        when(groupRepository.findById(anyLong())).thenReturn(oldGroup);
//        when(groupRepository.save(any(Group.class))).thenReturn(newGroup);
//        when(groupMapper.entityToDtoInfo(any(Group.class))).thenReturn(groupInfo);
//        GroupDTO.Info groupRes = groupService.update(groupUpdate);
//
//        //assert
//        assertNotNull(groupRes);
//        assertEquals(groupRes.getId(), groupUpdate.getId());
//        assertEquals(newGroup.getCode(), groupRes.getCode());
//        assertNotEquals(oldGroup.get().getDefinitionAllowed(), groupRes.getDefinitionAllowed());
//    }

    @Test
    public void deleteTest() {
        //init
        Optional<Group> group = Optional.of(generateGroup("deleteCode", "deleteTitle", Boolean.TRUE));
        //act
        when(groupRepository.findById(anyLong())).thenReturn(group);
        doNothing().when(groupRepository).delete(any(Group.class));
        groupService.delete(1L);
        //assert
        verify(groupRepository, times(1)).delete(group.get());
    }

    @Test
    public void searchTest() throws IllegalAccessException, NoSuchFieldException {
        //init
        SearchDTO.SearchRq request = new SearchDTO.SearchRq();
        //act
        SearchDTO.SearchRs<GroupDTO.Info> result = groupService.search(request);
        //assert
        assertNotNull(result);
    }


    private Group generateGroup(String code, String title, Boolean definitionAllowed) {
        Group group1 = new Group();
        group1.setCode(code);
        group1.setTitle(title);
        group1.setDefinitionAllowed(definitionAllowed);
        return group1;
    }

    private List<Group> generateGroupList() {
        return Arrays.asList(
                generateGroup("testCode1", "testTitle1", Boolean.TRUE),
                generateGroup("testCode2", "testTitle2", Boolean.FALSE)
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
