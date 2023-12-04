package com.nicico.evaluation.repository;

import com.nicico.evaluation.dto.EvaluationDTO;
import com.nicico.evaluation.model.Evaluation;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface EvaluationRepository extends JpaRepository<Evaluation, Long>, JpaSpecificationExecutor<Evaluation> {

    Evaluation findByEvaluationPeriodIdAndAssessPostCode(Long id, String assessPostCode);

    Page<Evaluation> findAllByAssessNationalCodeAndStatusCatalogId(String assessNationalCode, Long statusCatalogId, Pageable pageable);

    Optional<Evaluation> findFirstByAssessNationalCodeAndEvaluationPeriodId(String assessNationalCode, Long evaluationPeriodId);

    List<Evaluation> findByAssessorNationalCodeAndEvaluationPeriodId(String assessorNationalCode, Long evaluationPeriodId);

    @Query(value = """
            SELECT
                tbl_evaluation.c_assess_post_code
            FROM
                tbl_evaluation
                WHERE evaluation_period_id = :id
                GROUP BY tbl_evaluation.c_assess_post_code
                          """, nativeQuery = true)
    List<String> getUsedPostInEvaluation(Long id);

    @Modifying
    @Query(value = """
            MERGE INTO TBL_EVALUATION evaluation
            USING (
              SELECT id, C_START_DATE_ASSESSMENT
              FROM tbl_evaluation_period 
              WHERE C_START_DATE_ASSESSMENT  = :startAssessmentDate
            ) evaluationPeriod
            ON (evaluationPeriod.id = evaluation.evaluation_period_id)
            WHEN MATCHED THEN
            UPDATE SET
              STATUS_CATALOG_ID = CASE
                         WHEN (SELECT c_code FROM tbl_catalog  WHERE id = evaluation.STATUS_CATALOG_ID) = 'Validated'
                            THEN (SELECT id FROM tbl_catalog WHERE c_code = 'Awaiting-review')
                         WHEN (SELECT c_code FROM tbl_catalog  WHERE id = evaluation.STATUS_CATALOG_ID) = 'Initial-registration'  
                            THEN (SELECT id FROM tbl_catalog WHERE c_code = 'Termination')
                         ELSE evaluation.STATUS_CATALOG_ID
                       END,
              C_DESCRIPTION = CASE
                         WHEN (SELECT c_code FROM tbl_catalog  WHERE id = evaluation.STATUS_CATALOG_ID) = 'Initial-registration'
                           THEN :message
                        ELSE evaluation.C_DESCRIPTION
                       END
            """, nativeQuery = true)
    void updateEvaluationStatusId(@Param("startAssessmentDate") String startAssessmentDate, @Param("message") String message);

    @Query(value = """
                        SELECT
                            kpi_type_title title,
                            SUM(total_value) / COUNT(evalid) weight
                        FROM
                            (
                                SELECT
                                    eval.id                    evalid,
                                    grouptype.n_weight         grouptypeweight,
                                    grouptype.id               grouptypeid,
                                    eval.c_assess_post_code    post_code,
                                    kpitype.c_title            kpi_type_title,
                                    grade.c_post_grade_title   grade_title,
                                    SUM(evalitem.questionnaire_answer_catalog_value) total_value
                                FROM
                                    tbl_evaluation            eval
                                    JOIN view_post                 post ON eval.c_assess_post_code = post.post_code
                                    JOIN view_grade                grade ON grade.c_post_grade_code = post.post_grade_code
                                    JOIN tbl_evaluation_item       evalitem ON evalitem.evaluation_id = eval.id
                                    JOIN tbl_group_type_merit      grouptypemerit ON grouptypemerit.id = evalitem.group_type_merit_id
                                    JOIN tbl_group_type            grouptype ON grouptype.id = grouptypemerit.group_type_id
                                    JOIN tbl_merit_component_aud   merit ON merit.id = evalitem.merit_component_audit_id
                                                                          AND merit.rev = evalitem.merit_component_audit_rev
                                    JOIN tbl_kpi_type              kpitype ON kpitype.id = grouptype.kpi_type_id
                                    JOIN tbl_evaluation_period     evalperiod ON evalperiod.id = eval.evaluation_period_id
                                WHERE
                                    post.OMOOR_CODE in ( :omoorCode)
                                    AND eval.status_catalog_id = (select id from tbl_catalog  where c_code = 'Finalized')
                                    AND evalPeriod.id = :periodId
                                GROUP BY
                                    eval.id,
                                    eval.c_assess_post_code,
                                    kpitype.c_title,
                                    grade.c_post_grade_title,
                                    grouptype.n_weight,
                                    grouptype.id
                                ORDER BY
                                    eval.id DESC
                            )
                        GROUP BY
                            kpi_type_title
                        UNION
                        SELECT
                            kpi_type_title title,
                            SUM(total_value) / COUNT(evalid) weight
                        FROM
                            (
                                SELECT DISTINCT
                                    eval.id                    evalid,
                                    grouptype.n_weight         grouptypeweight,
                                    grouptype.id               grouptypeid,
                                    eval.c_assess_post_code    post_code,
                                    kpitype.c_title            kpi_type_title,
                                    grade.c_post_grade_title   grade_title,
                                    SUM(evalitem.questionnaire_answer_catalog_value) total_value
                                FROM
                                    tbl_evaluation             eval
                                    JOIN view_post                  post ON eval.c_assess_post_code = post.post_code
                                    JOIN view_grade                 grade ON grade.c_post_grade_code = post.post_grade_code
                                    JOIN tbl_evaluation_item        evalitem ON evalitem.evaluation_id = eval.id
                                    JOIN tbl_post_merit_component   postmerit ON postmerit.id = evalitem.post_merit_id
                                    JOIN tbl_group_grade            groupgrade ON groupgrade.grade_id = grade.id
                                    JOIN tbl_group                  group1 ON group1.id = groupgrade.group_id
                                    JOIN tbl_group_type             grouptype ON grouptype.group_id = group1.id
                                    JOIN tbl_kpi_type               kpitype ON kpitype.id = grouptype.kpi_type_id
                                    JOIN tbl_merit_component_aud    merit ON merit.id = evalitem.merit_component_audit_id
                                                                          AND merit.rev = evalitem.merit_component_audit_rev
                                    JOIN tbl_evaluation_period      evalperiod ON evalperiod.id = eval.evaluation_period_id
                                WHERE
                                    post.OMOOR_CODE in (:omoorCode)
                                    AND eval.status_catalog_id = (select id from tbl_catalog  where c_code = 'Finalized')
                                    AND kpitype.LEVEL_DEF_ID =  (select id from tbl_catalog  where c_code = 'organizationalPosition')
                                    AND evalPeriod.id = :periodId
                                GROUP BY
                                    eval.id,
                                    eval.c_assess_post_code,
                                    kpitype.c_title,
                                    grade.c_post_grade_title,
                                    grouptype.n_weight,
                                    grouptype.id
                                ORDER BY
                                    eval.id DESC
                            )
                        GROUP BY
                            kpi_type_title
            """, nativeQuery = true)
    List<EvaluationDTO.AverageWeightDTO> getFinalizedAverageByGradeAndPeriodEvaluation(Long periodId, List<String> omoorCode);

    @Query(value = """
            SELECT
                post.omoor_code
            FROM
                tbl_evaluation   eval
                JOIN view_post        post ON post.post_code = eval.c_assess_post_code
            WHERE
                eval.c_assess_national_code = :assessNationalCode 
                and eval.EVALUATION_PERIOD_ID = :periodId
                and eval.status_catalog_id = (select id from tbl_catalog  where c_code = 'Finalized')
            """, nativeQuery = true)
    String getOmoorCodeByAssessNationalCodeAndPeriodId(String assessNationalCode, Long periodId);

    @Query(value = """
            SELECT
                post.omoor_code
            FROM
                tbl_evaluation   eval
                JOIN view_post        post ON post.post_code = eval.c_assess_post_code
            WHERE
                eval.c_assessor_national_code = :assessorNationalCode 
                and eval.EVALUATION_PERIOD_ID = :periodId
                and eval.status_catalog_id = (select id from tbl_catalog  where c_code = 'Finalized')
            """, nativeQuery = true)
    List<String> getOmoorCodeByAssessorNationalCodeAndPeriodId(String assessorNationalCode, Long periodId);

    @Query(value = """
            SELECT
                *
            FROM
                (
                    SELECT DISTINCT
                        COUNT(eval.id)
                        OVER(PARTITION BY vpost.omoor_title) AS omoor_finalized_number,
                        vpost.omoor_title                    AS omoor_title
                    FROM
                        tbl_evaluation eval
                        LEFT JOIN view_post      vpost ON vpost.post_code = eval.c_assess_post_code
                    WHERE
                        eval.evaluation_period_id = :evaluationPeriodId
                        AND eval.status_catalog_id = :finalizedStatusCatalogId
                    ORDER BY
                        omoor_finalized_number DESC
                )
            WHERE
                ROWNUM <= 6
                          """, nativeQuery = true)
    List<?> mostParticipationInFinalizedEvaluationPerOmoor(Long evaluationPeriodId, Long finalizedStatusCatalogId);

    @Query(value = """
             SELECT distinct
                   eval.c_assess_full_name as assessFullName,
                   personnel.PERSONEL_NO as personnelNo,
                   personnel.post_title as postTitle,
                   eval.average_score as averageScore,
                   eval.c_assess_national_code as assessNational
               FROM
                   tbl_evaluation            eval
                   JOIN view_post                 post ON eval.c_assess_post_code = post.post_code
                   join view_personnel personnel on personnel.POST_CODE = post.post_code
                   JOIN tbl_evaluation_period  evalperiod ON evalperiod.id = eval.evaluation_period_id
               WHERE
                   post.omoor_code in (:omoorCode)
                   AND eval.status_catalog_id = ( SELECT  id  FROM tbl_catalog  WHERE c_code = 'Finalized')
                   AND evalperiod.id = :periodId
                   order by eval.average_score desc
                 OFFSET :pageNumber ROWS FETCH NEXT :pageSize ROWS ONLY
            """, nativeQuery = true)
    List<EvaluationDTO.BestAssessAverageScoreDTO> getBestAssessesByOmoor(Long periodId, List<String> omoorCode, int pageNumber, int pageSize);

    List<Evaluation> findAllByIdIn(List<Long> ids);

    @Query(value = """
             SELECT
                 COUNT(*)
             FROM
                 tbl_evaluation
             WHERE
                 status_catalog_id = :awaitingReviewStatusId
                 AND c_assessor_national_code = :nationalCode
            """, nativeQuery = true)
    Integer getNumberOfAssessorWorkInWorkSpace(Long awaitingReviewStatusId, String nationalCode);

    @Query(value = """
             SELECT
                 id
             FROM
                 tbl_evaluation
             WHERE
                 status_catalog_id = :awaitingReviewStatusId
                 AND c_assessor_national_code = :nationalCode
            """, nativeQuery = true)
    List<Long> getAssessorWorkInWorkSpace(Long awaitingReviewStatusId, String nationalCode);

}
