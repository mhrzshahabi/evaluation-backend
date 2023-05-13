package com.nicico.evaluation.repository;

import com.nicico.evaluation.model.SensitiveEvents;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SensitiveEventsRepository extends JpaRepository<SensitiveEvents, Long>, JpaSpecificationExecutor<SensitiveEvents> {

    List<SensitiveEvents> findAllByNationalCode(String nationalCode);
}
