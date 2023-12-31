package com.nicico.evaluation.repository;

import com.nicico.evaluation.dto.EvaluationItemDTO;
import com.nicico.evaluation.model.EvaluationItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EvaluationItemRepository extends JpaRepository<EvaluationItem, Long>, JpaSpecificationExecutor<EvaluationItem> {

    @Query("from EvaluationItem  evalItem " +
            " join Evaluation eval on eval.id = evalItem.evaluationId " +
            " join PostMeritComponent postMerit on postMerit.id  = evalItem.postMeritComponentId " +
            " join MeritComponent merit on merit.id = postMerit.meritComponentId " +
            " where eval.id in (:evaluationIds )")
    List<EvaluationItem> getAllPostMeritByEvalId(@Param("evaluationIds") List<Long> evaluationIds);

    @Query("from EvaluationItem  evalItem " +
            " join Evaluation eval on eval.id = evalItem.evaluationId " +
            " join GroupTypeMerit groupTypeMerit on groupTypeMerit.id  = evalItem.groupTypeMeritId " +
            " join MeritComponentAudit meritAudit on meritAudit.auditId.id = evalItem.meritId and meritAudit.auditId.rev = evalItem.meritRev " +
            " where eval.id = :evaluationId and groupTypeMerit.id in ( :groupTypeMeritIds)")
    List<EvaluationItem> getAllGroupTypeMeritByEvalId(@Param("evaluationId") Long evaluationId, List<Long> groupTypeMeritIds);

    List<EvaluationItem> getAllByEvaluationId(@Param("evaluationId") Long evaluationId);

    @Query(value = """
            SELECT
                SUM(tbl_evaluation_item.questionnaire_answer_catalog_value) * 100 / tbl_group_type.n_weight
            FROM
                tbl_evaluation_item
                LEFT JOIN tbl_group_type_merit ON tbl_group_type_merit.id = tbl_evaluation_item.group_type_merit_id
                LEFT JOIN tbl_group_type ON tbl_group_type.id = tbl_group_type_merit.group_type_id
                LEFT JOIN tbl_kpi_type ON tbl_kpi_type.id = tbl_group_type.kpi_type_id
            WHERE
                evaluation_id = :evaluationId
                AND tbl_kpi_type.c_title = :kpiTypeTitle
            GROUP BY
                tbl_group_type.id,
                tbl_group_type.n_weight
                          """, nativeQuery = true)
    Long getGroupTypeAverageScoreByEvaluationId(Long evaluationId, String kpiTypeTitle);

    @Query(value = """
            SELECT
                SUM(item.questionnaire_answer_catalog_value) * 100 / tbl_group_type.n_weight as averageScore,
                MAX(tbl_kpi_type.c_title) as kpiTitle
            FROM
                tbl_evaluation_item item
                LEFT JOIN tbl_group_type_merit ON tbl_group_type_merit.id = item.group_type_merit_id
                LEFT JOIN tbl_group_type ON tbl_group_type.id = tbl_group_type_merit.group_type_id
                LEFT JOIN tbl_kpi_type ON tbl_kpi_type.id = tbl_group_type.kpi_type_id
            WHERE
                evaluation_id IN (
                    :evaluationIds
                )
            GROUP BY
                tbl_group_type.id,
                tbl_group_type.n_weight,
                tbl_kpi_type.id
                """, nativeQuery = true)
    List<EvaluationItemDTO.GroupTypeAverageScoreDto> getAllGroupTypeAverageScoreByEvaluationIds(List<Long> evaluationIds);
}
