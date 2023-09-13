package com.nicico.evaluation.repository;

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
}
