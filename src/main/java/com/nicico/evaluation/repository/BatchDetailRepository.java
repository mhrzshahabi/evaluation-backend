package com.nicico.evaluation.repository;

import com.nicico.evaluation.model.BatchDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface BatchDetailRepository extends JpaRepository<BatchDetail, Long>, JpaSpecificationExecutor<BatchDetail> {

}
