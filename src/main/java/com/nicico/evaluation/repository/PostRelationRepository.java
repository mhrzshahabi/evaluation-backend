package com.nicico.evaluation.repository;

import com.nicico.evaluation.model.PostRelation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PostRelationRepository extends JpaRepository<PostRelation, Long>, JpaSpecificationExecutor<PostRelation> {

    @Query(value = """
            SELECT
                tbl_evaluation_period_post.id AS evaluationPeriodPostId,
                view_post_relation.post_code,
                view_post_relation.post_title,
                view_post_relation.post_code_parent,
                view_post_relation.post_title_parent,
                view_post_relation.id
            FROM
                     view_post_relation
                INNER JOIN tbl_evaluation_period_post ON view_post_relation.post_code = tbl_evaluation_period_post.
                c_post_code
            WHERE
                tbl_evaluation_period_post.evaluation_period_id = :id
                           """, nativeQuery = true)
    List<?> getPostInfoEvaluationPeriodList(Long id);

    @Query(value = """
            SELECT\s
                      view_post_relation.post_code
                  FROM
                           view_post_relation
                      INNER JOIN tbl_evaluation_period_post ON view_post_relation.post_code = tbl_evaluation_period_post.
                      c_post_code
                  WHERE
                      tbl_evaluation_period_post.evaluation_period_id = :id
                      and
                      view_post_relation.post_code not in (
                         SELECT
                       tbl_evaluation.c_assess_post_code
                   FROM
                       tbl_evaluation
                       WHERE evaluation_period_id = :id
                       GROUP BY tbl_evaluation.c_assess_post_code
                      )
                      """, nativeQuery = true)
    List<String> getUnUsedPostCodeByEvaluationPeriodId(Long id);

    PostRelation getByPostCode(String postCode);

}
