package com.nicico.evaluation.service;

import com.nicico.copper.common.dto.search.SearchDTO;
import com.nicico.evaluation.common.PageableMapper;
import com.nicico.evaluation.dto.EvaluationPeriodDTO;
import com.nicico.evaluation.mapper.EvaluationPeriodMapper;
import com.nicico.evaluation.model.EvaluationPeriod;
import com.nicico.evaluation.repository.EvaluationPeriodRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.*;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.times;

@ExtendWith(MockitoExtension.class)
public class EvaluationPeriodServiceTests {

    @InjectMocks
    private EvaluationPeriodService evaluationPeriodService;
    @Mock
    private EvaluationPeriodRepository evaluationPeriodRepository;
    @Mock
    private EvaluationPeriodMapper evaluationPeriodMapper;
    @Mock
    private PageableMapper pageableMapper;

    @Test
    public void createTest() {
        //init
        EvaluationPeriodDTO.Create evaluationPeriodCreate = new EvaluationPeriodDTO.Create();
        evaluationPeriodCreate.setTitle("testTitle");
        evaluationPeriodCreate.setStartDate(new Date(1682003325));
        evaluationPeriodCreate.setEndDate(new Date(1683013608));
        evaluationPeriodCreate.setStartDateAssessment(new Date(evaluationPeriodCreate.getStartDate().getTime() + 15000));
        evaluationPeriodCreate.setEndDateAssessment(new Date(evaluationPeriodCreate.getEndDate().getTime() - 15000));
        evaluationPeriodCreate.setDescription("testDesc");

        EvaluationPeriod evaluationPeriod = generateEvaluationPeriod("testTitle",new Date(1682003325), new Date(1683013608),"testDesc");
        evaluationPeriod.setStartDateAssessment(new Date(evaluationPeriod.getStartDate().getTime() + 15000));
        evaluationPeriod.setEndDateAssessment(new Date(evaluationPeriod.getEndDate().getTime() - 15000));
        EvaluationPeriodDTO.Info evaluationPeriodInfo = generateEvaluationPeriodInfo(1L, "testTitle",new Date(1682003325), new Date(1683013608),"testDesc");
        evaluationPeriodInfo.setStartDateAssessment(new Date(evaluationPeriodInfo.getStartDate().getTime() + 15000));
        evaluationPeriodInfo.setEndDateAssessment(new Date(evaluationPeriodInfo.getEndDate().getTime() - 15000));
        //Action
        when(evaluationPeriodRepository.save(any(EvaluationPeriod.class))).thenReturn(evaluationPeriod);
        when(evaluationPeriodMapper.dtoCreateToEntity(any(EvaluationPeriodDTO.Create.class))).thenReturn(evaluationPeriod);
        when(evaluationPeriodMapper.entityToDtoInfo(any(EvaluationPeriod.class))).thenReturn(evaluationPeriodInfo);
        EvaluationPeriodDTO.Info evaluationPeriodInfoRes = evaluationPeriodService.create(evaluationPeriodCreate);

        //Assert
        assertNotNull(evaluationPeriodInfoRes);
        assertTrue(evaluationPeriodInfoRes.getId() >= 1);
        assertEquals(evaluationPeriodCreate.getTitle(), evaluationPeriodInfoRes.getTitle());
        assertEquals(evaluationPeriodCreate.getStartDate(), evaluationPeriodInfoRes.getStartDate());
        assertEquals(evaluationPeriodCreate.getEndDate(), evaluationPeriodInfoRes.getEndDate());
        assertEquals(evaluationPeriodCreate.getDescription(), evaluationPeriodInfoRes.getDescription());
        assertThat(evaluationPeriodCreate.getTitle()).isEqualTo("testTitle");

    }

    @Test
    public void listPageableTest() {
        //init
        List<EvaluationPeriod> evaluationPeriods = generateEvaluationPeriodList();
        List<EvaluationPeriodDTO.Info> evaluationPeriodInfos = generateEvaluationPeriodInfoList();
        Page<EvaluationPeriod> pageEvaluationPeriod = new PageImpl<>(evaluationPeriods);
        Pageable pageable = PageRequest.of(0, 10, Sort.by(Sort.Direction.DESC, "id"));
        //Act
        when(evaluationPeriodRepository.findAll(any(Pageable.class))).thenReturn(pageEvaluationPeriod);
        when(evaluationPeriodMapper.entityToDtoInfoList(anyList())).thenReturn(evaluationPeriodInfos);
        when(pageableMapper.toPageable(anyInt(), anyInt())).thenReturn(pageable);
        EvaluationPeriodDTO.SpecResponse specResponse = evaluationPeriodService.list(10, 0);
        //Assert
        assertNotNull(specResponse);
        assertEquals(specResponse.getResponse().getData().size(), 2);
        EvaluationPeriodDTO.Info ep0 = specResponse.getResponse().getData().get(0);
        assertEquals(ep0.getTitle(), "testTitle1");
        EvaluationPeriodDTO.Info ep1 = specResponse.getResponse().getData().get(1);
        assertEquals(ep1.getTitle(), "testTitle2");
    }

    @Test
    public void getByIdTest() {
        //init
        Optional<EvaluationPeriod> evaluationPeriod = Optional.of(generateEvaluationPeriodList().get(0));
        EvaluationPeriodDTO.Info evaluationPeriodInfo = generateEvaluationPeriodInfoList().get(0);
        //act
        when(evaluationPeriodRepository.findById(anyLong())).thenReturn(evaluationPeriod);
        when(evaluationPeriodMapper.entityToDtoInfo(any(EvaluationPeriod.class))).thenReturn(evaluationPeriodInfo);
        EvaluationPeriodDTO.Info evaluationPeriodInfoRes = evaluationPeriodService.get(1L);
        //assert
        assertNotNull(evaluationPeriodInfoRes);
        assertEquals(evaluationPeriodInfoRes.getTitle(), evaluationPeriod.get().getTitle());
        assertEquals(evaluationPeriodInfoRes.getDescription(), evaluationPeriod.get().getDescription());
    }

    @Test
    public void getByIdExceptionTest() {
        //init
        EvaluationPeriod evaluationPeriod = generateEvaluationPeriodList().get(0);
        evaluationPeriod.setId(1L);
        Optional<EvaluationPeriod> evaluationPeriodOP = Optional.of(evaluationPeriod);
        //act
        when(evaluationPeriodRepository.findById(2L)).thenReturn(evaluationPeriodOP);
        //assert
        assertThrows(RuntimeException.class, () -> {
            evaluationPeriodService.get(evaluationPeriod.getId());
        });
    }

//    @Test
//    public void updateTest() {
//        //init
//        EvaluationPeriodDTO.Update evaluationPeriodUpdate = new EvaluationPeriodDTO.Update();
//        evaluationPeriodUpdate.setId(1L).setTitle("NEWtestTitle1").setDescription("NEWtestDesc1")
//                .setStartDate(new Date(1682003325)).setEndDate(new Date(1683013608));
//        evaluationPeriodUpdate.setStartDateAssessment(new Date(evaluationPeriodUpdate.getStartDate().getTime() + 15000));
//        evaluationPeriodUpdate.setEndDateAssessment(new Date(evaluationPeriodUpdate.getEndDate().getTime() - 15000));
//        EvaluationPeriod evaluationPeriod = generateEvaluationPeriod("OLDtestTitle1", new Date(), new Date(),"OLDtestDesc1");
//        evaluationPeriod.setId(1L);
//        Optional<EvaluationPeriod> oldEvaluationPeriod = Optional.of(evaluationPeriod);
//        EvaluationPeriod newEvaluationPeriod = generateEvaluationPeriod("NEWtestTitle1", new Date(), new Date(),"NEWtestDesc1");
//        EvaluationPeriodDTO.Info evaluationPeriodInfo = generateEvaluationPeriodInfo(1L, "NEWtestTitle1", new Date(),new Date(),"NEWtestDesc1");
//
//        //act
//        when(evaluationPeriodRepository.findById(anyLong())).thenReturn(oldEvaluationPeriod);
//        when(evaluationPeriodRepository.save(any(EvaluationPeriod.class))).thenReturn(newEvaluationPeriod);
//        when(evaluationPeriodMapper.entityToDtoInfo(any(EvaluationPeriod.class))).thenReturn(evaluationPeriodInfo);
//        EvaluationPeriodDTO.Info evaluationPeriodInfoRes = evaluationPeriodService.update(evaluationPeriodUpdate);
//
//        //assert
//        assertNotNull(evaluationPeriodInfoRes);
//        assertEquals(evaluationPeriodInfoRes.getId(), evaluationPeriodUpdate.getId());
//        assertEquals(evaluationPeriodInfoRes.getTitle(), newEvaluationPeriod.getTitle());
//        assertEquals(evaluationPeriodInfoRes.getDescription(), newEvaluationPeriod.getDescription());
//    }

    @Test
    public void deleteTest() {
        //init
        Optional<EvaluationPeriod> evaluationPeriod = Optional.of(generateEvaluationPeriod("deleteTitle",new Date(), new Date(), "deleteDesc"));
        //act
        when(evaluationPeriodRepository.findById(anyLong())).thenReturn(evaluationPeriod);
        doNothing().when(evaluationPeriodRepository).delete(any(EvaluationPeriod.class));
        evaluationPeriodService.delete(1L);
        //assert
        verify(evaluationPeriodRepository, times(1)).delete(evaluationPeriod.get());
    }

    @Test
    public void searchTest() throws IllegalAccessException, NoSuchFieldException {
        //init
        SearchDTO.SearchRq request = new SearchDTO.SearchRq();
        //act
        SearchDTO.SearchRs<EvaluationPeriodDTO.Info> result = evaluationPeriodService.search(request);
        //assert
        assertNotNull(result);
    }


    private EvaluationPeriod generateEvaluationPeriod(String title, Date startDate, Date endDate, String description) {
        EvaluationPeriod evaluationPeriod = new EvaluationPeriod();
        evaluationPeriod.setTitle(title);
        evaluationPeriod.setStartDate(startDate);
        evaluationPeriod.setEndDate(endDate);
        evaluationPeriod.setDescription(description);
        return evaluationPeriod;
    }

    private List<EvaluationPeriod> generateEvaluationPeriodList() {
        return Arrays.asList(
                generateEvaluationPeriod("testTitle1", new Date(), new Date(), "testDesc1"),
                generateEvaluationPeriod("testTitle2", new Date(), new Date(), "testDesc2")
        );
    }

    private EvaluationPeriodDTO.Info generateEvaluationPeriodInfo(Long id, String title, Date startDate, Date endDate, String description) {
        EvaluationPeriodDTO.Info evaluationPeriodInfo = new EvaluationPeriodDTO.Info();
        evaluationPeriodInfo.setId(id);
        evaluationPeriodInfo.setTitle(title);
        evaluationPeriodInfo.setStartDate(startDate);
        evaluationPeriodInfo.setEndDate(endDate);
        evaluationPeriodInfo.setDescription(description);
        return evaluationPeriodInfo;
    }

    private List<EvaluationPeriodDTO.Info> generateEvaluationPeriodInfoList() {
        return Arrays.asList(
                generateEvaluationPeriodInfo(1L,"testTitle1", new Date(), new Date(), "testDesc1"),
                generateEvaluationPeriodInfo(2L, "testTitle2", new Date(), new Date(), "testDesc2")
        );
    }



}
