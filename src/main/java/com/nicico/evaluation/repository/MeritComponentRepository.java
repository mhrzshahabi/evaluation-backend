package com.nicico.evaluation.repository;

import com.nicico.evaluation.model.MeritComponent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface MeritComponentRepository extends JpaRepository<MeritComponent, Long>, JpaSpecificationExecutor<MeritComponent> {}