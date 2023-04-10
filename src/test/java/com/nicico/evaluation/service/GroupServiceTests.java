package com.nicico.evaluation.service;

import com.nicico.evaluation.common.PageDTO;
import com.nicico.evaluation.common.PageableMapper;
import com.nicico.evaluation.dto.GroupDTO;
import com.nicico.evaluation.exception.ApplicationException;
import com.nicico.evaluation.exception.ServiceException;
import com.nicico.evaluation.mapper.GroupMapper;
import com.nicico.evaluation.model.Group;
import com.nicico.evaluation.repository.GroupRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.InstanceOfAssertFactories.OPTIONAL;
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
    @Mock
    private ApplicationException<ServiceException> applicationException;

    @Test
    public void createTest() {
        //init
        GroupDTO.Create groupCreate = new GroupDTO.Create();
        groupCreate.setCode("testCode");
        groupCreate.setTitle("testTitle");
        groupCreate.setDefinitionAllowed(Boolean.TRUE);

        Group group = new Group();
        group.setCode("testCode");
        group.setTitle("testTitle");
        group.setDefinitionAllowed(Boolean.TRUE);

        GroupDTO.Info groupInfo = new GroupDTO.Info();
        groupInfo.setId(1L);
        groupInfo.setCode("testCode");
        groupInfo.setTitle("testTitle");
        groupInfo.setDefinitionAllowed(Boolean.TRUE);

        //Action
        when(groupRepository.save(any(Group.class))).thenReturn(group);
        when(groupMapper.dtoCreateToEntity(any(GroupDTO.Create.class))).thenReturn(group);
        when(groupMapper.entityToDtoInfo(any(Group.class))).thenReturn(groupInfo);
        GroupDTO.Info groupInfoRes = groupService.create(groupCreate);

        //Assert
        assertNotNull(groupInfoRes);
        assertEquals(group.getCode(), groupInfoRes.getCode());
        assertEquals(group.getTitle(), groupInfoRes.getTitle());
        assertThat(group.getCode()).isEqualTo("testCode");
        assertThat(groupInfoRes.getId()).isEqualTo(1L);

    }

    @Test
    public void listTest() {
        //init
        List<Group> groups = generateGroupList();
        List<GroupDTO.Info> groupInfos = generateGroupInfoList();
        //Act
        when(groupRepository.findAll()).thenReturn(groups);
        when(groupMapper.entityToDtoInfoList(anyList())).thenReturn(groupInfos);
        List<GroupDTO.Info> groupInfosRes = groupService.list();
        //Assert
        assertNotNull(groupInfosRes);
        assertEquals(groupInfosRes.size(), 2);
        assertEquals(groupInfosRes.get(0).getDefinitionAllowed(), Boolean.TRUE);
        assertEquals(groupInfosRes.get(1).getDefinitionAllowed(), Boolean.FALSE);

    }

    @Test
    public void listWithPageableParamTest() {
        //init
        List<Group> groups = generateGroupList();
        List<GroupDTO.Info> groupInfos = generateGroupInfoList();
        Page<Group> pageGroup = new PageImpl(groups);
        PageDTO pageDto = new PageDTO();
        pageDto.setPageSize(pageGroup.getSize());
        pageDto.setTotalPage(pageGroup.getTotalPages());
        pageDto.setTotalItems(pageGroup.getTotalElements());
        pageDto.setList(groupInfos);
        Pageable pageable = PageRequest.of(0, 10, Sort.by(Sort.Direction.DESC, "id"));
        //Act
        when(groupRepository.findAll(any(Pageable.class))).thenReturn(pageGroup);
        when(groupMapper.entityToDtoInfoList(anyList())).thenReturn(groupInfos);
        when(pageableMapper.toPageDto(any(Page.class), anyList())).thenReturn(pageDto);
        PageDTO pageDtoRes = groupService.list(pageable);
        //Assert
        assertNotNull(pageDtoRes);
        assertEquals(pageDtoRes.getList().size(), 2);
        GroupDTO.Info gi0  = (GroupDTO.Info)pageDtoRes.getList().get(0);
        assertEquals(gi0.getDefinitionAllowed(), Boolean.TRUE);
        GroupDTO.Info gi1  = (GroupDTO.Info)pageDtoRes.getList().get(1);
        assertEquals(gi1.getDefinitionAllowed(), Boolean.FALSE);
    }

    @Test
    public void getTest(){
        //init
        Optional<Group> group = Optional.of(generateGroup("testCode1", "testTitle1", Boolean.FALSE));
        GroupDTO.Info groupInfo = generateGroupInfoList().get(0);
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
    public  void updateTest(){
        //init
        GroupDTO.Update groupUpdate = new GroupDTO.Update();
        groupUpdate.setId(1L).setCode("NEWtestCode1").setTitle("NEWtestTitle1").setDefinitionAllowed(Boolean.TRUE);
        Group group = generateGroup("OLDtestCode1", "OLDtestTitle1", Boolean.FALSE);
        group.setId(1L);
        Optional<Group> oldGroup = Optional.of(group);
        Group newGroup = generateGroup("NEWtestCode1", "NEWtestTitle1", Boolean.TRUE);
        GroupDTO.Info groupInfo = generateGroupInfo(1L, "NEWtestCode1", "NEWtestTitle1", Boolean.TRUE);

        //act
        when(groupRepository.findById(anyLong())).thenReturn(oldGroup);
        when(groupRepository.save(any(Group.class))).thenReturn(newGroup);
        when(groupMapper.entityToDtoInfo(any(Group.class))).thenReturn(groupInfo);
        GroupDTO.Info groupRes = groupService.update(groupUpdate);

        //assert
        assertNotNull(groupRes);
        assertEquals(groupRes.getId(), groupUpdate.getId());
        assertEquals(newGroup.getCode(), groupRes.getCode());
        assertNotEquals(oldGroup.get().getDefinitionAllowed(), groupRes.getDefinitionAllowed());
    }

    @Test
    public void deleteTest(){
        //init
        Optional<Group> group = Optional.of(generateGroup("deleteCode", "deleteTitle", Boolean.TRUE));
        //act
        when(groupRepository.findById(anyLong())).thenReturn(group);
        doNothing().when(groupRepository).delete(any(Group.class));
        groupService.delete(1L);
        //assert
        verify(groupRepository , times(1)).delete(group.get());
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
                generateGroup("testCode1", "testTitle1",Boolean.TRUE),
                generateGroup("testCode2","testTitle2",Boolean.FALSE)
        );
    }
    private GroupDTO.Info generateGroupInfo(Long id, String code, String title, Boolean definitionAllowed) {
        GroupDTO.Info groupInfo1 = new GroupDTO.Info();
        groupInfo1.setId(id);
        groupInfo1.setCode(code);
        groupInfo1.setTitle(title);
        groupInfo1.setDefinitionAllowed(definitionAllowed);
        return  groupInfo1;
    }
    private List<GroupDTO.Info> generateGroupInfoList() {
        return Arrays.asList(
                generateGroupInfo(1L, "testCode1", "testTitle1", Boolean.TRUE),
                generateGroupInfo(2L, "testCode2", "testTitle2", Boolean.FALSE)
        );
    }
}
