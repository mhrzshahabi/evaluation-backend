package com.nicico.evaluation.service;

import com.nicico.copper.common.dto.search.SearchDTO;
import com.nicico.evaluation.common.PageableMapper;
import com.nicico.evaluation.dto.KPITypeDTO;
import com.nicico.evaluation.exception.EvaluationHandleException;
import com.nicico.evaluation.mapper.KPITypeMapper;
import com.nicico.evaluation.model.KPIType;
import com.nicico.evaluation.repository.KPITypeRepository;
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
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class KPITypeServiceTests {

    @InjectMocks
    public KPITypeService kpiTypeService;

    @Mock
    private KPITypeRepository kpiTypeRepository;
    @Mock
    private KPITypeMapper kpiTypeMapper;
    @Mock
    private PageableMapper pageableMapper;

    @Test
    public void getByIdTest() {
        //init
        Optional<KPIType> kpiType = Optional.of(generateKPIType("testCode1", "testTitle1", 1L));
        KPITypeDTO.Info kpiTypeInfo = generateKpiTypeInfo(1L, "testCode1", "testTitle1", 1L);
        //act
        when(kpiTypeRepository.findById(anyLong())).thenReturn(kpiType);
        when(kpiTypeMapper.entityToDtoInfo(any(KPIType.class))).thenReturn(kpiTypeInfo);
        KPITypeDTO.Info kpiTypeRes = kpiTypeService.get(1L);
        //assert
        assertNotNull(kpiTypeRes);
        assertEquals(kpiTypeRes.getCode(), kpiTypeInfo.getCode());
        assertEquals(kpiTypeRes.getTitle(), kpiTypeInfo.getTitle());
        assertEquals(kpiTypeRes.getLevelDefCatalogId(), kpiTypeInfo.getLevelDefCatalogId());
    }

    @Test
    public void listPageableTest() {
        //init
        List<KPIType> kpiTypes = generateKPITypeList();
        List<KPITypeDTO.Info> kpiTypeInfoList = generatekpiTypeInfoList();
        Page<KPIType> pageKpiType = new PageImpl<>(kpiTypes);
        Pageable pageable = PageRequest.of(0, 10, Sort.by(Sort.Direction.DESC, "id"));
        //Act
        when(kpiTypeRepository.findAll(any(Pageable.class))).thenReturn(pageKpiType);
        when(kpiTypeMapper.entityToDtoInfoList(anyList())).thenReturn(kpiTypeInfoList);
        when(pageableMapper.toPageable(anyInt(), anyInt())).thenReturn(pageable);
        KPITypeDTO.SpecResponse specResponse = kpiTypeService.list(10, 0);
        //Assert
        assertNotNull(specResponse);
        assertEquals(specResponse.getResponse().getData().size(), 2);
        KPITypeDTO.Info kpiType0 = specResponse.getResponse().getData().get(0);
        assertEquals(kpiType0.getCode(), "testCode1");
        assertEquals(kpiType0.getLevelDefCatalogId(), 1L);
        KPITypeDTO.Info kpiType1 = specResponse.getResponse().getData().get(1);
        assertEquals(kpiType1.getCode(), "testCode2");
        assertEquals(kpiType0.getLevelDefCatalogId(), 1L);
    }

    @Test
    public void searchTest() throws IllegalAccessException, NoSuchFieldException {
        //init
        SearchDTO.SearchRq request = new SearchDTO.SearchRq();
        //act
        SearchDTO.SearchRs<KPITypeDTO.Info> result = kpiTypeService.search(request);
        //assert
        assertNotNull(result);
    }


    @Test
    public void createTest() {
        //init
        KPITypeDTO.Create kpiTypeCreate = new KPITypeDTO.Create();
        kpiTypeCreate.setCode("testCode");
        kpiTypeCreate.setTitle("testTitle");
        kpiTypeCreate.setLevelDefCatalogId(1L);

        KPIType kpiType = generateKPIType("testCode", "testTitle", 1L);

        KPITypeDTO.Info kpiTypeInfo = generateKpiTypeInfo(1L, "testCode", "testTitle", 1L);

        //Action
        when(kpiTypeRepository.save(any(KPIType.class))).thenReturn(kpiType);
        when(kpiTypeMapper.dtoCreateToEntity(any(KPITypeDTO.Create.class))).thenReturn(kpiType);
        when(kpiTypeMapper.entityToDtoInfo(any(KPIType.class))).thenReturn(kpiTypeInfo);
        KPITypeDTO.Info kpiTypeInfoRes = kpiTypeService.create(kpiTypeCreate);

        //Assert
        assertNotNull(kpiTypeInfoRes);
        assertEquals(kpiType.getCode(), kpiTypeInfoRes.getCode());
        assertEquals(kpiType.getTitle(), kpiTypeInfoRes.getTitle());
        assertEquals(kpiTypeInfoRes.getCode(), "testCode");
        assertEquals(kpiTypeInfoRes.getId(), 1L);
        assertEquals(kpiTypeInfoRes.getLevelDefCatalogId(), 1L);

    }

    @Test
    public void createExceptionTest() {
        //init
        KPITypeDTO.Create kpiTypeCreate = new KPITypeDTO.Create();
        kpiTypeCreate.setCode("testCode");
        kpiTypeCreate.setTitle("testTitle");
        kpiTypeCreate.setLevelDefCatalogId(1L);

        //Action
        when(kpiTypeRepository.save(any(KPIType.class))).thenThrow(EvaluationHandleException.class);

        assertThrows(EvaluationHandleException.class, () -> {
            kpiTypeService.create(kpiTypeCreate);
        });
    }

    @Test
    public void updateTest() {
        //init
        KPITypeDTO.Update kpiTypeUpdate = new KPITypeDTO.Update();
        kpiTypeUpdate.setId(1L).setCode("NEWtestCode1").setTitle("NEWtestTitle1").setLevelDefCatalogId(1L);
        KPIType kpiType = generateKPIType("OLDtestCode1", "OLDtestTitle1", 1L);
        kpiType.setId(1L);
        Optional<KPIType> oldKpiType = Optional.of(kpiType);
        KPIType newkpiType = generateKPIType("NEWtestCode1", "NEWtestTitle1", 1L);
        KPITypeDTO.Info kpiTypeInfo = generateKpiTypeInfo(1L, "NEWtestCode1", "NEWtestTitle1", 1L);

        //act
        when(kpiTypeRepository.findById(anyLong())).thenReturn(oldKpiType);
        when(kpiTypeRepository.save(any(KPIType.class))).thenReturn(newkpiType);
        when(kpiTypeMapper.entityToDtoInfo(any(KPIType.class))).thenReturn(kpiTypeInfo);
        KPITypeDTO.Info kpiTypeInfoRes = kpiTypeService.update(kpiTypeUpdate);

        //assert
        assertNotNull(kpiTypeInfoRes);
        assertEquals(kpiTypeInfoRes.getId(), kpiType.getId());
        assertEquals(kpiTypeInfoRes.getCode(), kpiTypeUpdate.getCode());
        assertEquals(kpiTypeInfoRes.getTitle(), kpiTypeUpdate.getTitle());
        assertEquals(kpiTypeInfoRes.getLevelDefCatalogId(), kpiTypeUpdate.getLevelDefCatalogId());
    }

    @Test
    public void deleteTest() {
        //init
        Optional<KPIType> kpiType = Optional.of(generateKPIType("deleteCode", "deleteTitle", 1L));
        //act
        when(kpiTypeRepository.findById(anyLong())).thenReturn(kpiType);
        doNothing().when(kpiTypeRepository).delete(any(KPIType.class));
        kpiTypeService.delete(1L);
        //assert
        verify(kpiTypeRepository, times(1)).delete(kpiType.get());
    }

    @Test
    public void getByIdExceptionTest() {
        //init
        KPIType kpiType = generateKPIType("testCode1", "testTitle1", 1L);
        kpiType.setId(1L);
        Optional<KPIType> kpiTypeOP = Optional.of(kpiType);
        //act
        when(kpiTypeRepository.findById(2L)).thenReturn(kpiTypeOP);
        //assert
        assertThrows(RuntimeException.class, () -> {
            kpiTypeService.get(kpiType.getId());
        });
    }
//
//


    private KPIType generateKPIType(String code, String title, Long levelDefCatalog) {
        KPIType kpiType = new KPIType();
        kpiType.setCode(code);
        kpiType.setTitle(title);
        kpiType.setLevelDefCatalogId(levelDefCatalog);
        return kpiType;
    }

    private List<KPIType> generateKPITypeList() {
        return Arrays.asList(
                generateKPIType("testCode1", "testTitle1", 1L),
                generateKPIType("testCode2", "testTitle2", 1L)
        );
    }

    private KPITypeDTO.Info generateKpiTypeInfo(Long id, String code, String title, Long levelDefCatalog) {
        KPITypeDTO.Info kpiTypeInfo = new KPITypeDTO.Info();
        kpiTypeInfo.setId(id);
        kpiTypeInfo.setCode(code);
        kpiTypeInfo.setTitle(title);
        kpiTypeInfo.setLevelDefCatalogId(levelDefCatalog);
        return kpiTypeInfo;
    }

    private List<KPITypeDTO.Info> generatekpiTypeInfoList() {
        return Arrays.asList(
                generateKpiTypeInfo(1L, "testCode1", "testTitle1", 1L),
                generateKpiTypeInfo(2L, "testCode2", "testTitle2", 1L)
        );
    }


}
