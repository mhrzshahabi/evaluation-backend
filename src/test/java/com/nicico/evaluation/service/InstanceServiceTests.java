//package com.nicico.evaluation.service;
//
//import com.nicico.copper.common.dto.search.SearchDTO;
//import com.nicico.evaluation.common.PageableMapper;
//import com.nicico.evaluation.dto.InstanceDTO;
//import com.nicico.evaluation.mapper.InstanceMapper;
//import com.nicico.evaluation.model.Instance;
//import com.nicico.evaluation.repository.InstanceRepository;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.junit.jupiter.MockitoExtension;
//import org.springframework.data.domain.*;
//
//import java.util.Arrays;
//import java.util.List;
//import java.util.Optional;
//
//import static org.assertj.core.api.Assertions.assertThat;
//import static org.junit.jupiter.api.Assertions.*;
//import static org.mockito.ArgumentMatchers.*;
//import static org.mockito.ArgumentMatchers.any;
//import static org.mockito.Mockito.*;
//import static org.mockito.Mockito.times;
//
//
//@ExtendWith(MockitoExtension.class)
////@SpringBootTest
//public class InstanceServiceTests {
//
//    @InjectMocks
//    public InstanceService instanceService;
//
//    @Mock
//    private InstanceRepository instanceRepository;
//    @Mock
//    private InstanceMapper instanceMapper;
//    @Mock
//    private PageableMapper pageableMapper;
//
//    @Test
//    public void createTest() {
//        //init
//        InstanceDTO.Create instanceCreate = new InstanceDTO.Create();
//        instanceCreate.setCode("testCode");
//        instanceCreate.setTitle("testTitle");
//
//        Instance instance = generateInstance("testCode", "testTitle");
//
//        InstanceDTO.Info instanceInfo = generateInstanceInfo(1L, "testCode", "testTitle");
//
//        //Action
//        when(instanceRepository.save(any(Instance.class))).thenReturn(instance);
//        when(instanceMapper.dtoCreateToEntity(any(InstanceDTO.Create.class))).thenReturn(instance);
//        when(instanceMapper.entityToDtoInfo(any(Instance.class))).thenReturn(instanceInfo);
//        InstanceDTO.Info instanceInfoRes = instanceService.create(instanceCreate);
//
//        //Assert
//        assertNotNull(instanceInfoRes);
//        assertTrue(instanceInfoRes.getId() >= 1);
//        assertEquals(instanceCreate.getCode(), instanceInfoRes.getCode());
//        assertEquals(instanceCreate.getTitle(), instanceInfoRes.getTitle());
//        assertThat(instanceInfoRes.getCode()).isEqualTo("testCode");
//
//    }
//
//    @Test
//    public void listPageableTest() {
//        //init
//        List<Instance> instances = generateInstanceList();
//        List<InstanceDTO.Info> instanceInfos = generateInstanceInfoList();
//        Page<Instance> pageInstance = new PageImpl<>(instances);
//        Pageable pageable = PageRequest.of(0, 10, Sort.by(Sort.Direction.DESC, "id"));
//        //Act
//        when(instanceRepository.findAll(any(Pageable.class))).thenReturn(pageInstance);
//        when(instanceMapper.entityToDtoInfoList(anyList())).thenReturn(instanceInfos);
//        when(pageableMapper.toPageable(anyInt(), anyInt())).thenReturn(pageable);
//        InstanceDTO.SpecResponse specResponse = instanceService.list(10, 0);
//        //Assert
//        assertNotNull(specResponse);
//        assertEquals(specResponse.getResponse().getData().size(), 2);
//        InstanceDTO.Info in0 = specResponse.getResponse().getData().get(0);
//        assertEquals(in0.getCode(), "testCode1");
//        InstanceDTO.Info in1 = specResponse.getResponse().getData().get(1);
//        assertEquals(in1.getCode(), "testCode2");
//    }
//
//    @Test
//    public void getByIdTest() {
//        //init
//        Optional<Instance> instance = Optional.of(generateInstance("testCode1", "testTitle1"));
//        InstanceDTO.Info instatnceInfo = generateInstanceInfo(1L, "testCode1", "testTitle1");
//        //act
//        when(instanceRepository.findById(anyLong())).thenReturn(instance);
//        when(instanceMapper.entityToDtoInfo(any(Instance.class))).thenReturn(instatnceInfo);
//        InstanceDTO.Info instanceInfoRes = instanceService.get(1L);
//        //assert
//        assertNotNull(instanceInfoRes);
//        assertEquals(instanceInfoRes.getCode(), instance.get().getCode());
//        assertEquals(instatnceInfo.getTitle(), instance.get().getTitle());
//    }
//
//    @Test
//    public void getByIdExceptionTest() {
//        //init
//        Instance instance = generateInstance("testCode1", "testTitle1");
//        instance.setId(1L);
//        Optional<Instance> instanceOP = Optional.of(instance);
//        //act
//        when(instanceRepository.findById(2L)).thenReturn(instanceOP);
//        //assert
//        assertThrows(RuntimeException.class, () -> {
//            instanceService.get(instance.getId());
//        });
//    }
//
//    @Test
//    public void updateTest() {
//        //init
//        InstanceDTO.Update instanceUpdate = new InstanceDTO.Update();
//        instanceUpdate.setId(1L).setCode("NEWtestCode1").setTitle("NEWtestTitle1");
//        Instance instance = generateInstance("OLDtestCode1", "OLDtestTitle1");
//        instance.setId(1L);
//        Optional<Instance> oldInstance = Optional.of(instance);
//        Instance newInstance = generateInstance("NEWtestCode1", "NEWtestTitle1");
//        InstanceDTO.Info InstanceInfo = generateInstanceInfo(1L, "NEWtestCode1", "NEWtestTitle1");
//
//        //act
//        when(instanceRepository.findById(anyLong())).thenReturn(oldInstance);
//        when(instanceRepository.save(any(Instance.class))).thenReturn(newInstance);
//        when(instanceMapper.entityToDtoInfo(any(Instance.class))).thenReturn(InstanceInfo);
//        InstanceDTO.Info instanceInfoRes = instanceService.update(instanceUpdate);
//
//        //assert
//        assertNotNull(instanceInfoRes);
//        assertEquals(instanceInfoRes.getId(), instanceUpdate.getId());
//        assertEquals(instanceInfoRes.getCode(), newInstance.getCode());
//        assertEquals(instanceInfoRes.getTitle(), newInstance.getTitle());
//    }
//
//    @Test
//    public void deleteTest() {
//        //init
//        Optional<Instance> instance = Optional.of(generateInstance("deleteCode", "deleteTitle"));
//        //act
//        when(instanceRepository.findById(anyLong())).thenReturn(instance);
//        doNothing().when(instanceRepository).delete(any(Instance.class));
//        instanceService.delete(1L);
//        //assert
//        verify(instanceRepository, times(1)).delete(instance.get());
//    }
//
//    @Test
//    public void searchTest() throws IllegalAccessException, NoSuchFieldException {
//        //init
//        SearchDTO.SearchRq request = new SearchDTO.SearchRq();
//        //act
//        SearchDTO.SearchRs<InstanceDTO.Info> result = instanceService.search(request);
//        //assert
//        assertNotNull(result);
//    }
//
//
//    private Instance generateInstance(String code, String title) {
//        Instance instance = new Instance();
//        instance.setCode(code);
//        instance.setTitle(title);
//        return instance;
//    }
//
//    private List<Instance> generateInstanceList() {
//        return Arrays.asList(
//                generateInstance("testCode1", "testTitle1"),
//                generateInstance("testCode2", "testTitle2")
//        );
//    }
//
//    private InstanceDTO.Info generateInstanceInfo(Long id, String code, String title) {
//        InstanceDTO.Info instanceInfo = new InstanceDTO.Info();
//        instanceInfo.setId(id);
//        instanceInfo.setCode(code);
//        instanceInfo.setTitle(title);
//        return instanceInfo;
//    }
//
//    private List<InstanceDTO.Info> generateInstanceInfoList() {
//        return Arrays.asList(
//                generateInstanceInfo(1L, "testCode1", "testTitle1"),
//                generateInstanceInfo(2L, "testCode2", "testTitle2")
//        );
//    }
//
//
//}
