package com.nicico.evaluation.repository;

import com.nicico.evaluation.model.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PostRepository extends JpaRepository<Post, Long>, JpaSpecificationExecutor<Post> {

    Optional<Post> findFirstByPostCode(String postCode);

    Optional<Post> findByPostCode(String postCode);

    @Query(value = """
            SELECT
            post.*
            FROM
                view_post   post
            WHERE
                post.post_code IN (
                     select c_post_code from tbl_evaluation_period_post periodPost where periodPost.EVALUATION_PERIOD_ID = :periodId
                )
                AND post.post_code NOT IN (
                    SELECT
                        post1.post_code
                    FROM
                        tbl_group_grade   groupgrade
                        JOIN view_post   post1 ON post1.post_grade_id = groupgrade.grade_id
                    WHERE
                        post1.post_code IN (
                          select c_post_code from tbl_evaluation_period_post periodPost where periodPost.EVALUATION_PERIOD_ID = :periodId
                        )
                )
            """, nativeQuery = true)
    List<Post> findPostGradeHasNotGroupByPeriodId(Long periodId);

    @Query(value = """
            SELECT
            post.*
            FROM
                view_post   post
            WHERE
                post.post_code IN (
                    :postCodes
                )
                AND post.post_code NOT IN (
                    SELECT
                        post1.post_code
                    FROM
                        tbl_group_grade   groupgrade
                        JOIN view_post   post1 ON post1.post_grade_id = groupgrade.grade_id
                    WHERE
                        post1.post_code IN (
                         :postCodes
                        )
                )
            """, nativeQuery = true)
    List<Post> findPostGradeHasNotGroupByPostCode(List<String> postCodes);

    @Query(value = "from Post post join EvaluationPeriodPost periodPost on post.postCode = periodPost.postCode " +
            "where periodPost.evaluationPeriodId = :periodId")
    List<Post> findAllByPostCodeIn(Long periodId);

    @Query(value = """
            SELECT
                  post.*
             FROM
                 view_post post
             WHERE
                 post.post_code IN (
                    select c_post_code from tbl_evaluation_period_post periodPost where periodPost.EVALUATION_PERIOD_ID = :periodId
                 )
                 AND post.post_code NOT IN (
                     SELECT
                        distinct post1.post_code
                     FROM
                         tbl_group_type    grouptype
                         JOIN tbl_group_grade   groupgrade ON groupgrade.group_id = grouptype.group_id
                         JOIN view_post   post1 ON post1.post_grade_id = groupgrade.grade_id
                     WHERE
                         post1.post_code IN (
                             SELECT
                                 c_post_code
                             FROM
                                 tbl_evaluation_period_post periodpost
                             WHERE
                                 periodpost.evaluation_period_id = :periodId
                     )
                 )
             """, nativeQuery = true)
    List<Post> findGroupHasNotGroupTypeByPeriodId(Long periodId);
    
    @Query(value = """
            SELECT
                  post.*
             FROM
                 view_post post
             WHERE
                 post.post_code IN (
                    :postCodes
                 )
                 AND post.post_code NOT IN (
                     SELECT
                        distinct post1.post_code
                     FROM
                         tbl_group_type    grouptype
                         JOIN tbl_group_grade   groupgrade ON groupgrade.group_id = grouptype.group_id
                         JOIN view_post   post1 ON post1.post_grade_id = groupgrade.grade_id
                     WHERE
                         post1.post_code IN (
                            :postCodes
                     )
                 )
             """, nativeQuery = true)
    List<Post> findGroupHasNotGroupTypeByPostCode(List<String> postCodes);

    @Modifying
    @Query(value = """
            BEGIN
                dbms_mview.refresh('view_post');
            END;
            """, nativeQuery = true)
    void refreshViewPost();
}
