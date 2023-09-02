package com.nicico.evaluation.repository;

import com.nicico.evaluation.model.MeritComponent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MeritComponentRepository extends JpaRepository<MeritComponent, Long>, JpaSpecificationExecutor<MeritComponent> {

    Optional<MeritComponent> findFirstByCode(String code);

    @Query(value = """
            SELECT
                COUNT(*)
            FROM
                tbl_merit_component
            WHERE
                status_catalog_id = :awaitingCreateStatusId
                OR status_catalog_id = :awaitingEditStatusId
                OR status_catalog_id = :awaitingRevokeStatusId
                          """, nativeQuery = true)
    Integer getNumberOfAdminWorkInWorkSpace(Long awaitingCreateStatusId, Long awaitingEditStatusId, Long awaitingRevokeStatusId);

    @Query(value = """
            SELECT
                id
            FROM
                tbl_merit_component
            WHERE
                status_catalog_id = :awaitingCreateStatusId
                OR status_catalog_id = :awaitingEditStatusId
                OR status_catalog_id = :awaitingRevokeStatusId
                          """, nativeQuery = true)
    List<Long> getAdminWorkInWorkSpace(Long awaitingCreateStatusId, Long awaitingEditStatusId, Long awaitingRevokeStatusId);
}
