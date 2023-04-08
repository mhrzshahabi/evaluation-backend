package com.nicico.evaluation.repository;

import com.nicico.evaluation.model.MeritComponent;
import com.nicico.evaluation.model.MeritComponentType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface MeritComponentTypeRepository extends JpaRepository<MeritComponentType, Long>, JpaSpecificationExecutor<MeritComponentType> {}
