package com.nicico.evaluation.mapper;

import com.nicico.evaluation.dto.InstanceDTO;
import com.nicico.evaluation.model.Instance;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class InstanceMapperTests {

    @InjectMocks
    private InstanceMapperImpl instanceMapper;

    @Test
    public void dtoCreateToEntityTest() {
        //init
        InstanceDTO.Create instanceCreate = new InstanceDTO.Create();
        instanceCreate.setCode("testCode1").setTitle("testTitle1");
        Instance instance = generateInstance(null, "testCode1", "testTitle1");
        //act
        Instance instanceRes = instanceMapper.dtoCreateToEntity(instanceCreate);
        //assert
        assertNotNull(instanceRes);
        assertNull(instanceRes.getId());
        assertEquals(instanceRes.getCode(), instance.getCode());
        assertEquals(instanceRes.getTitle(), instance.getTitle());
    }

    @Test
    public void entityToDtoInfoTest() {
        //init
        InstanceDTO.Info instanceInfo = generateInstanceInfo(1L, "testCode1", "testTitle1");
        Instance instance = generateInstance(1L, "testCode1", "testTitle1");
        //act
        InstanceDTO.Info instanceInfoRes = instanceMapper.entityToDtoInfo(instance);
        //assert
        assertNotNull(instanceInfoRes);
        assertEquals(instanceInfoRes.getId(), instance.getId());
        assertEquals(instanceInfoRes.getCode(), instance.getCode());
        assertEquals(instanceInfoRes.getTitle(), instance.getTitle());
    }

    @Test
    public void entityToDtoInfoListTest() {
        //init
        List<InstanceDTO.Info> instanceInfos = generateInstanceInfoList();
        List<Instance> instances = generateInstanceList();
        //act
        List<InstanceDTO.Info> instanceInfosRes = instanceMapper.entityToDtoInfoList(instances);
        //assert
        assertNotNull(instanceInfosRes);
        assertEquals(instanceInfosRes.size(), instanceInfos.size());
        assertEquals(instanceInfosRes.get(0).getId(), instanceInfos.get(0).getId());
        assertEquals(instanceInfosRes.get(0).getCode(), instanceInfos.get(0).getCode());
        assertEquals(instanceInfosRes.get(0).getTitle(), instanceInfos.get(0).getTitle());

        assertEquals(instanceInfosRes.get(1).getId(), instanceInfos.get(1).getId());
        assertEquals(instanceInfosRes.get(1).getCode(), instanceInfos.get(1).getCode());
        assertEquals(instanceInfosRes.get(1).getTitle(), instanceInfos.get(1).getTitle());
    }

    @Test
    public void updateTest() {
        //init
        InstanceDTO.Update instanceUpdate = new InstanceDTO.Update();
        instanceUpdate.setId(1L).setCode("testCode1").setTitle("testTitle1");
        Instance instance = generateInstance(null, null, null);
        //act
        instanceMapper.update(instance, instanceUpdate);
        //assert
        assertEquals(instance.getId(), instanceUpdate.getId());
        assertEquals(instance.getCode(), instanceUpdate.getCode());
        assertEquals(instance.getTitle(), instanceUpdate.getTitle());
    }


    private Instance generateInstance(Long id, String code, String title) {
        Instance instance = new Instance();
        instance.setId(id);
        instance.setCode(code);
        instance.setTitle(title);
        return instance;
    }

    private List<Instance> generateInstanceList() {
        return Arrays.asList(
                generateInstance(1L, "testCode1", "testTitle1"),
                generateInstance(2L, "testCode2", "testTitle2")
        );
    }

    private InstanceDTO.Info generateInstanceInfo(Long id, String code, String title) {
        InstanceDTO.Info instanceInfo = new InstanceDTO.Info();
        instanceInfo.setId(id);
        instanceInfo.setCode(code);
        instanceInfo.setTitle(title);
        return instanceInfo;
    }

    private List<InstanceDTO.Info> generateInstanceInfoList() {
        return Arrays.asList(
                generateInstanceInfo(1L, "testCode1", "testTitle1"),
                generateInstanceInfo(2L, "testCode2", "testTitle2")
        );
    }

}
