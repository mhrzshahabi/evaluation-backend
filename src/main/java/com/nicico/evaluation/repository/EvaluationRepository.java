package com.nicico.evaluation.repository;

import com.nicico.evaluation.model.Evaluation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EvaluationRepository extends JpaRepository<Evaluation, Long>, JpaSpecificationExecutor<Evaluation> {

    Evaluation findByEvaluationPeriodIdAndAssessPostCode(Long id, String assessPostCode);

    List<Evaluation> findAllByIdIn(List<Long> id);

    @Query(value = """
            SELECT
                tbl_evaluation.c_assess_post_code
            FROM
                tbl_evaluation
                WHERE evaluation_period_id = :id
                GROUP BY tbl_evaluation.c_assess_post_code
                          """, nativeQuery = true)
    List<String> getUsedPostInEvaluation(Long id);

}
