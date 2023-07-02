
package com.nicico.evaluation.repository;

import com.nicico.evaluation.model.SpecialCase;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SpecialCaseRepository extends JpaRepository<SpecialCase, Long>, JpaSpecificationExecutor<SpecialCase> {

    List<SpecialCase> findByAssessNationalCodeAndStatusCatalogId(String nationalCode, Long statusCatalogId);

    List<SpecialCase> findByAssessNationalCodeAndStatusCatalogIdAndIdNotIn(String nationalCode, Long statusCatalogId, List<Long> id);

    @Query(value = "from SpecialCase where endDate < :currentDate and statusCatalogId <> :revokedStatusId ")
    List<SpecialCase> findAllExpireSpecialCase(String currentDate, Long revokedStatusId);

}
    