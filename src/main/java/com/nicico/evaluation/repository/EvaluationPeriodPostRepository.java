package com.nicico.evaluation.repository;

import com.nicico.evaluation.model.EvaluationPeriodPost;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Repository
public interface EvaluationPeriodPostRepository extends JpaRepository<EvaluationPeriodPost, Long>, JpaSpecificationExecutor<EvaluationPeriodPost> {

    Page<EvaluationPeriodPost> findByEvaluationPeriodId(Long evaluationPeriodId, Pageable pageable);

    List<EvaluationPeriodPost> findAllByEvaluationPeriodIdIn(Set<Long> evaluationPeriodIds);

    Optional<EvaluationPeriodPost> findFirstByEvaluationPeriodIdAndPostCode(Long evaluationPeriodPost, String postCode);

    void deleteByEvaluationPeriodId(Long evaluationPeriodPost);

    void deleteByEvaluationPeriodIdAndPostCode(Long evaluationPeriodPost, String postCode);

    @Query(value = """
            SELECT
                periodPost1.*
            FROM
                tbl_evaluation_period_post periodPost1
            WHERE
                periodPost1.c_post_code NOT IN (
                    SELECT
                        periodpost.c_post_code
                    FROM
                        tbl_post_merit_component     postMerit
                        JOIN view_post                    post ON post.post_group_code = postMerit.c_group_post_code
                        JOIN tbl_evaluation_period_post   periodPost ON periodPost.c_post_code = post.post_code
                        JOIN tbl_evaluation_period        evalPeriod ON evalperiod.id = periodPost.evaluation_period_id
                    WHERE
                        evalPeriod.id = :periodId
                ) 
                AND periodPost1.EVALUATION_PERIOD_ID = :periodId
            """, nativeQuery = true
    )
    List<EvaluationPeriodPost> findAllByPeriodIdNotInPostMerit(Long periodId);
}
