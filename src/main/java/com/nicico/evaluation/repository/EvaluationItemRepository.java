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
            " join MeritComponentAudit meritAudit on meritAudit.auditId.id = evalItem.meritId and meritAudit.auditId.rev = evalItem.meritRev " +
            " where eval.id = :evaluationId and groupTypeMerit.id in ( :groupTypeMeritIds)")
    List<EvaluationItem> getAllGroupTypeMeritByEvalId(@Param("evaluationId") Long evaluationId, List<Long> groupTypeMeritIds);

    List<EvaluationItem> getAllByEvaluationId(@Param("evaluationId") Long evaluationId);
}
