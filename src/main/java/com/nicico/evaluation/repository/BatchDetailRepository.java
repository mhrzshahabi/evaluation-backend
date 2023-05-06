package com.nicico.evaluation.repository;

import com.nicico.evaluation.model.BatchDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BatchDetailRepository extends JpaRepository<BatchDetail, Long>, JpaSpecificationExecutor<BatchDetail> {

    List<BatchDetail> findAllByBatchId(Long batchId);

    List<BatchDetail> findAllByBatchIdAndStatusCatalogTitle(Long batchId, String statusCatalogTitle);
}
