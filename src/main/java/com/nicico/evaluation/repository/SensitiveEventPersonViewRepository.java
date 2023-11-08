package com.nicico.evaluation.repository;

import com.nicico.evaluation.model.SensitiveEventPersonView;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface SensitiveEventPersonViewRepository extends JpaRepository<SensitiveEventPersonView, Long>, JpaSpecificationExecutor<SensitiveEventPersonView> {

}
