package com.nicico.evaluation.repository;

import com.nicico.evaluation.model.MeritComponentAudit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MeritComponentAuditRepository extends JpaRepository<MeritComponentAudit, Long>, JpaSpecificationExecutor<MeritComponentAudit> {

    @Query(value = """
            SELECT
                *
            FROM
                tbl_merit_component_aud
            WHERE
                rev = (
                    SELECT
                        MAX(rev)
                    FROM
                        tbl_merit_component_aud
                    WHERE
                        id = :meritComponentId
                        AND status_catalog_id = :statusCatalogId
                )
                AND id = :meritComponentId
            """, nativeQuery = true)
    Optional<MeritComponentAudit> findLastActiveByMeritComponentId(Long meritComponentId, Long statusCatalogId);

    @Query(value = """
            SELECT
                *
            FROM
                tbl_merit_component_aud
            WHERE
                rev = :rev
                AND id = :meritComponentId
            """, nativeQuery = true)
    Optional<MeritComponentAudit> findAllByRevAndMeritComponentId(Long rev, Long meritComponentId);

    @Query(value = """
            SELECT
                *
            FROM
                (
                    SELECT
                        id,
                        rev,
                        c_created_by,
                        d_created_date,
                        c_last_modified_by,
                        d_last_modified_date,
                        c_title,
                        c_code,
                        status_catalog_id,
                        c_description,
                        RANK() OVER(
                            PARTITION BY id
                            ORDER BY
                                rev DESC
                        ) rev_rank
                    FROM
                        tbl_merit_component_aud
                    WHERE
                        id = :meritComponentId
                ) meritAudit
            WHERE
                rev_rank = 2
            """, nativeQuery = true)
    Optional<MeritComponentAudit> getPreviousById(Long meritComponentId);

//    @Query(value = """
//            SELECT
//                COUNT(*)
//            FROM
//                tbl_merit_component
//            WHERE
//                status_catalog_id = :reExaminationStatusId
//                          """, nativeQuery = true)
//    Integer getNumberOfExpertWorkInWorkSpace(Long reExaminationStatusId);

}
