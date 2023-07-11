package com.nicico.evaluation.repository;

import com.nicico.evaluation.model.Batch;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BatchRepository extends JpaRepository<Batch, Long>, JpaSpecificationExecutor<Batch> {

    List<Batch> findAllByStatusCatalogCode(String statusCatalogCode);
}
