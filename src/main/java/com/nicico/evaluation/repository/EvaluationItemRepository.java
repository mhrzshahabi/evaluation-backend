package com.nicico.evaluation.repository;

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
            " where eval.id = :evaluationId")
    List<EvaluationItem> getAllPostMeritByEvalId(@Param("evaluationId") Long evaluationId);

    @Query("from EvaluationItem  evalItem " +
            " join Evaluation eval on eval.id = evalItem.evaluationId " +
            " join GroupTypeMerit groupTypeMerit on groupTypeMerit.id  = evalItem.groupTypeMeritId " +
            " join MeritComponent merit on merit.id = groupTypeMerit.meritComponentId " +
            " where eval.id = :evaluationId")
    List<EvaluationItem> getAllGroupTypeMeritByEvalId(@Param("evaluationId") Long evaluationId);
}
