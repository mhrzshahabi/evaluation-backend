package com.nicico.evaluation.repository;

import com.nicico.evaluation.model.KPIType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface KPITypeRepository extends JpaRepository<KPIType, Long>, JpaSpecificationExecutor<KPIType> {}
