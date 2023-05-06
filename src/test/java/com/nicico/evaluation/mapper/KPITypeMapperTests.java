package com.nicico.evaluation.mapper;

import com.nicico.evaluation.dto.KPITypeDTO;
import com.nicico.evaluation.model.KPIType;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
public class KPITypeMapperTests {


    @InjectMocks
    private KPITypeMapperImpl kpiTypeMapper;

    @Test
    public void dtoCreateToEntityTest() {
        //init
        KPITypeDTO.Create kpiTypeCreate = new KPITypeDTO.Create();
        kpiTypeCreate.setCode("testCode1").setTitle("testTitle1").setLevelDefCatalogId(1L);
        KPIType kpiType = generateKpiType(null, "testCode1", "testTitle1", 1L);
        //act
        KPIType kpiTypeRes = kpiTypeMapper.dtoCreateToEntity(kpiTypeCreate);
        //assert
        assertNotNull(kpiTypeRes);
        assertNull(kpiTypeRes.getId());
        assertEquals(kpiTypeRes.getCode(), kpiTypeCreate.getCode());
        assertEquals(kpiTypeRes.getTitle(), kpiTypeCreate.getTitle());
        assertEquals(kpiTypeRes.getLevelDefCatalogId(), kpiTypeCreate.getLevelDefCatalogId());
    }

    @Test
    public void entityToDtoInfoTest() {
        //init
        KPITypeDTO.Info kpiTypeInfo = generateKpiTypeInfo(1L, "testCode1", "testTitle1", 1L);
        KPIType kpiType = generateKpiType(1L, "testCode1", "testTitle1", 1L);
        //act
        KPITypeDTO.Info kpiTypeInfoRes = kpiTypeMapper.entityToDtoInfo(kpiType);
        //assert
        assertNotNull(kpiTypeInfoRes);
        assertNull(kpiTypeInfoRes.getLevelDefCatalog());
        assertEquals(kpiTypeInfoRes.getId(), kpiTypeInfo.getId());
        assertEquals(kpiTypeInfoRes.getCode(), kpiTypeInfo.getCode());
        assertEquals(kpiTypeInfoRes.getTitle(), kpiTypeInfo.getTitle());
        assertEquals(kpiTypeInfoRes.getLevelDefCatalogId(), kpiTypeInfo.getLevelDefCatalogId());
    }

    @Test
    public void entityToDtoInfoListTest() {
        //init
        List<KPITypeDTO.Info> kpiTypeInfos = generateKpiTypeInfoList();
        List<KPIType> kpiTypes = generateKpiTypeList();
        //act
        List<KPITypeDTO.Info> kpiTypeInfoRes = kpiTypeMapper.entityToDtoInfoList(kpiTypes);
        //assert
        assertNotNull(kpiTypeInfoRes);
        assertEquals(kpiTypeInfoRes.size(), kpiTypeInfos.size());

        assertNull(kpiTypeInfoRes.get(0).getLevelDefCatalog());
        assertEquals(kpiTypeInfoRes.get(0).getId(), kpiTypeInfos.get(0).getId());
        assertEquals(kpiTypeInfoRes.get(0).getCode(), kpiTypeInfos.get(0).getCode());
        assertEquals(kpiTypeInfoRes.get(0).getTitle(), kpiTypeInfos.get(0).getTitle());
        assertEquals(kpiTypeInfoRes.get(0).getLevelDefCatalogId(), kpiTypeInfos.get(0).getLevelDefCatalogId());

        assertNull(kpiTypeInfoRes.get(1).getLevelDefCatalog());
        assertEquals(kpiTypeInfoRes.get(1).getId(), kpiTypeInfos.get(1).getId());
        assertEquals(kpiTypeInfoRes.get(1).getCode(), kpiTypeInfos.get(1).getCode());
        assertEquals(kpiTypeInfoRes.get(1).getTitle(), kpiTypeInfos.get(1).getTitle());
        assertEquals(kpiTypeInfoRes.get(1).getLevelDefCatalogId(), kpiTypeInfos.get(1).getLevelDefCatalogId());
    }

//    @Test
//    public void updateTest() {
//        //init
//        KPITypeDTO.Update kpiTypeUpdate = new KPITypeDTO.Update();
//        kpiTypeUpdate.setId(1L).setCode("testCode1").setTitle("testTitle1").setLevelDefCatalogId(1L);
//        KPIType kpiType = generateKpiType(null, null, null, null);
//        //act
//        kpiTypeMapper.update(kpiType, kpiTypeUpdate);
//        //assert
//        assertEquals(kpiType.getId(), kpiTypeUpdate.getId());
//        assertEquals(kpiType.getCode(), kpiTypeUpdate.getCode());
//        assertEquals(kpiType.getTitle(), kpiTypeUpdate.getTitle());
//        assertEquals(kpiType.getLevelDefCatalogId(), kpiTypeUpdate.getLevelDefCatalogId());
//    }


    private KPIType generateKpiType(Long id, String code, String title, Long levelDefCatalogId) {
        KPIType kpiType = new KPIType();
        kpiType.setId(id);
        kpiType.setCode(code);
        kpiType.setTitle(title);
        kpiType.setLevelDefCatalogId(levelDefCatalogId);
        return kpiType;
    }

    private List<KPIType> generateKpiTypeList() {
        return Arrays.asList(
                generateKpiType(1L, "testCode1", "testTitle1", 1L),
                generateKpiType(2L, "testCode2", "testTitle2", 1L)
        );
    }

    private KPITypeDTO.Info generateKpiTypeInfo(Long id, String code, String title, Long levelDefCatalogId) {
        KPITypeDTO.Info kpiTypeInfo = new KPITypeDTO.Info();
        kpiTypeInfo.setId(id);
        kpiTypeInfo.setCode(code);
        kpiTypeInfo.setTitle(title);
        kpiTypeInfo.setLevelDefCatalogId(levelDefCatalogId);
        return kpiTypeInfo;
    }

    private List<KPITypeDTO.Info> generateKpiTypeInfoList() {
        return Arrays.asList(
                generateKpiTypeInfo(1L, "testCode1", "testTitle1", 1L),
                generateKpiTypeInfo(2L, "testCode2", "testTitle2", 1L)
        );
    }


}
