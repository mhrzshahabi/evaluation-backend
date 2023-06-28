
package com.nicico.evaluation.repository;

import com.nicico.evaluation.model.SpecialCase;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SpecialCaseRepository extends JpaRepository<SpecialCase, Long>, JpaSpecificationExecutor<SpecialCase> {

    List<SpecialCase> findByAssessNationalCodeAndStatusCatalogId(String nationalCode, Long statusCatalogId);
}
    