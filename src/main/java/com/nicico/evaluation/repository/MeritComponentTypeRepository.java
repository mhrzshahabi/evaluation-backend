package com.nicico.evaluation.repository;

import com.nicico.evaluation.model.MeritComponentType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MeritComponentTypeRepository extends JpaRepository<MeritComponentType, Long>, JpaSpecificationExecutor<MeritComponentType> {
    List<MeritComponentType> findAllByMeritComponentId(Long meritComponentId);
}
