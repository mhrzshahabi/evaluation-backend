package com.nicico.evaluation.mapper;

import com.nicico.evaluation.dto.EvaluationDTO;
import com.nicico.evaluation.dto.EvaluationItemDTO;
import com.nicico.evaluation.iservice.IEvaluationItemService;
import com.nicico.evaluation.iservice.IEvaluationService;
import com.nicico.evaluation.iservice.IGroupTypeService;
import com.nicico.evaluation.iservice.IPostService;
import com.nicico.evaluation.model.Evaluation;
import com.nicico.evaluation.model.Post;
import org.mapstruct.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;

import java.util.Arrays;
import java.util.List;

import static com.nicico.evaluation.utility.EvaluationConstant.LEVEL_DEF_POST;

@Mapper(componentModel = "spring", uses = SpecialCaseMapper.class)
public abstract class EvaluationMapper {

    @Lazy
    @Autowired
    private IPostService postService;

    @Lazy
    @Autowired
    private IGroupTypeService groupTypeService;

    @Lazy
    @Autowired
    private IEvaluationService evaluationService;

    @Lazy
    @Autowired
    private IEvaluationItemService evaluationItemService;

    public abstract Evaluation dtoCreateToEntity(EvaluationDTO.Create dto);

    public abstract List<Evaluation> dtoCreateToEntityList(List<EvaluationDTO.Create> dto);

    public abstract EvaluationDTO.Update entityToUpdateDto(Evaluation entity);

    @Mappings({
            @Mapping(target = "id", source = "entity.id"),
            @Mapping(target = "assessorPostTitle", source = "entity.assessorPostCode", qualifiedByName = "getAssessorPostTitle"),
            @Mapping(target = "assessPostTitle", source = "assessPost.postTitle"),
            @Mapping(target = "postGradeTitle", source = "assessPost.postGradeTitle"),
            @Mapping(target = "mojtamaTitle", source = "assessPost.mojtamaTitle"),
            @Mapping(target = "moavenatTitle", source = "assessPost.moavenatTitle"),
            @Mapping(target = "omoorTitle", source = "assessPost.omoorTitle"),
            @Mapping(target = "ghesmatTitle", source = "assessPost.ghesmatTitle")
    })
    public abstract EvaluationDTO.Info entityToDtoInfo(Evaluation entity, Post assessPost);

    public abstract List<EvaluationDTO.Info> entityToDtoInfoList(List<Evaluation> entities);

    @Mappings({
            @Mapping(target = "assessorPostTitle", source = "entity.assessorPostCode", qualifiedByName = "getAssessorPostTitle"),
            @Mapping(target = "assessPostTitle", source = "assessPost.postTitle"),
            @Mapping(target = "evaluationPeriodTitle", source = "entity.evaluationPeriod.title"),
            @Mapping(target = "evaluationPeriodStartDateAssessment", source = "entity.evaluationPeriod.startDateAssessment"),
            @Mapping(target = "evaluationPeriodEndDateAssessment", source = "entity.evaluationPeriod.endDateAssessment"),
            @Mapping(target = "postGradeTitle", source = "assessPost.postGradeTitle"),
            @Mapping(target = "mojtamaTitle", source = "assessPost.mojtamaTitle"),
            @Mapping(target = "moavenatTitle", source = "assessPost.moavenatTitle"),
            @Mapping(target = "omoorTitle", source = "assessPost.omoorTitle"),
            @Mapping(target = "ghesmatTitle", source = "assessPost.ghesmatTitle"),
            @Mapping(target = "statusCatalogTitle", source = "entity.statusCatalog.title")
    })
    public abstract EvaluationDTO.Excel entityToDtoExcel(Evaluation entity, Post assessPost);

    public abstract List<EvaluationDTO.Excel> entityToDtoExcelList(List<Evaluation> entities);

    @Mappings({
            @Mapping(target = "id", source = "evaluationPeriod.id"),
            @Mapping(target = "title", source = "evaluationPeriod.title")
    })
    public abstract EvaluationDTO.EvaluationPeriodDashboard entityToDtoEvaluationPeriodDashboard(Evaluation entity);

    public abstract List<EvaluationDTO.EvaluationPeriodDashboard> entityToDtoEvaluationPeriodDashboardList(List<Evaluation> entities);

    @Mappings({
            @Mapping(target = "behavioralAverageScore", source = "id", qualifiedByName = "getBehavioralAverageScore"),
            @Mapping(target = "developmentAverageScore", source = "id", qualifiedByName = "getDevelopmentAverageScore"),
            @Mapping(target = "operationalAverageScore", source = "id", qualifiedByName = "getOperationalAverageScore")
    })
    public abstract EvaluationDTO.EvaluationAverageScoreData entityToDtoAverageScoreData(Evaluation entity);

    public abstract void update(@MappingTarget Evaluation entity, EvaluationDTO.Update dto);

    @Named("getAssessorPostTitle")
    String getAssessorPostTitle(String assessorPostCode) {
        return postService.getByPostCode(assessorPostCode).getPostTitle();
    }

    @Named("getBehavioralAverageScore")
    Long getBehavioralAverageScore(Long id) {
        return evaluationItemService.getGroupTypeAverageScoreByEvaluationId(id, "رفتاری");
    }

    @Named("getDevelopmentAverageScore")
    Long getDevelopmentAverageScore(Long id) {
        return evaluationItemService.getGroupTypeAverageScoreByEvaluationId(id, "توسعه شایستگی ها");
    }

    @Named("getOperationalAverageScore")
    Double getOperationalAverageScore(Long id) {
        String assessPostCode = Arrays.stream(evaluationService.getById(id).getAssessPostCode().split("/")).findFirst().get();
        Long groupTypeWeight = groupTypeService.getTypeByAssessPostCode(assessPostCode, LEVEL_DEF_POST).stream().findFirst().get().getWeight();
        List<EvaluationItemDTO.PostMeritTupleDTO> postMeritTupleDTOList = evaluationItemService.getAllPostMeritByEvalId(id);
        return postMeritTupleDTOList.stream().mapToDouble(EvaluationItemDTO.PostMeritTupleDTO::getQuestionnaireAnswerCatalogValue).sum() * 100 / groupTypeWeight;
    }

}
