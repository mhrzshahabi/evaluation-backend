package com.nicico.evaluation.repository;

import com.nicico.evaluation.model.EvaluationItem;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EvaluationItemRepository extends JpaRepository<EvaluationItem, Long>, JpaSpecificationExecutor<EvaluationItem> {

    @EntityGraph(attributePaths = {"evaluationItemInstance.instance"})
    @Query("from EvaluationItem  evalItem " +
            " join Evaluation eval on eval.id = evalItem.evaluationId " +
//            " join EvaluationItemInstance evalItemInstance on evalItemInstance.evaluationItemId = evalItem.id " +
//            " join Instance instance on instance.id = evalItemInstance.instanceId " +
            " join PostMeritComponent postMerit on postMerit.id  = evalItem.postMeritComponentId " +
            " join MeritComponent merit on merit.id = postMerit.meritComponentId " +
            " where eval.id = :evaluationId")
    List<EvaluationItem> getAllPostMeritByEvalId(@Param("evaluationId") Long evaluationId);

    @EntityGraph(attributePaths = {"evaluationItemInstance.instance"})
    @Query("from EvaluationItem  evalItem " +
            " join Evaluation eval on eval.id = evalItem.evaluationId " +
//            " join EvaluationItemInstance evalItemInstance on evalItemInstance.evaluationItemId = evalItem.id " +
//            " join Instance instance on instance.id = evalItemInstance.instanceId " +
            " join GroupTypeMerit groupTypeMerit on groupTypeMerit.id  = evalItem.groupTypeMeritId " +
            " join MeritComponent merit on merit.id = groupTypeMerit.meritComponentId " +
            " where eval.id = :evaluationId")
    List<EvaluationItem> getAllGroupTypeMeritByEvalId(@Param("evaluationId") Long evaluationId);

//    @Query(value = """
//         SELECT
//             tbl_instance.id,
//              tbl_instance.c_title,
//             tbl_evaluation_item.evaluation_id,
//             tbl_evaluation_item.id,
//             tbl_evaluation_item.c_description
//           \s
//         FROM
//             tbl_instance
//             INNER JOIN tbl_evaluation_item_instance ON tbl_instance.id = tbl_evaluation_item_instance.instance_id
//             INNER JOIN tbl_evaluation_item ON tbl_evaluation_item_instance.evaluation_item_id = tbl_evaluation_item.id
//         WHERE
//                 tbl_evaluation_item.evaluation_id = :evaluationId
//            """, nativeQuery = true)
//    List<?> getAllGroupTypeMeritByEvalId(@Param("evaluationId") Long evaluationId);
}
