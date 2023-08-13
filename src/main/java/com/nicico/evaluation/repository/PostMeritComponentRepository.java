package com.nicico.evaluation.repository;

import com.nicico.evaluation.model.PostMeritComponent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PostMeritComponentRepository extends JpaRepository<PostMeritComponent, Long>, JpaSpecificationExecutor<PostMeritComponent> {

    @Query(value = """
            select * from tbl_post_merit_component postMerit 
            join tbl_merit_component merit on merit.id = postMerit.MERIT_COMPONENT_ID
            join view_group_post groupPost on groupPost.GROUP_POST_CODE = postMerit.C_GROUP_POST_CODE
            where postMerit.C_GROUP_POST_CODE = :groupPostCode and catalog.C_CODE = 'Active-Merit'
            """, nativeQuery = true)
    List<PostMeritComponent> findAllByGroupPostCode(String groupPostCode);

    @Query(value = """
                    SELECT
                        postmerit.*
                    FROM
                        tbl_post_merit_component     postmerit
                        JOIN view_post                    post ON post.post_group_code = postmerit.c_group_post_code
                        JOIN tbl_evaluation_period_post   periodpost ON periodpost.c_post_code = post.post_code
                        JOIN tbl_evaluation_period        evalperiod ON evalperiod.id = periodpost.evaluation_period_id
                    WHERE
                        evalperiod.id = :periodId
               
            """, nativeQuery = true
    )
    List<PostMeritComponent> findAllByPeriodIdIn(Long periodId);
}
