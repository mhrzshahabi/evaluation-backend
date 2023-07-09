package com.nicico.evaluation.repository;

import com.nicico.evaluation.model.SensitiveEventPerson;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SensitiveEventPersonRepository extends JpaRepository<SensitiveEventPerson, Long>, JpaSpecificationExecutor<SensitiveEventPerson> {

    List<SensitiveEventPerson> findAllBySensitiveEventId(Long sensitiveEventId);
}
