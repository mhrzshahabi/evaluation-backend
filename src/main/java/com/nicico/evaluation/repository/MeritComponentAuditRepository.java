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

    @Query(value = """
            SELECT
                COUNT(*)
            FROM
                (
                    SELECT
                        *
                    FROM
                        (
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
                                        RANK()
                                        OVER(PARTITION BY id
                                             ORDER BY
                                                 rev DESC
                                        ) AS rev_rank
                                    FROM
                                        tbl_merit_component_aud
                                    WHERE
                                        id IN (
                                            SELECT
                                                id
                                            FROM
                                                tbl_merit_component
                                            WHERE
                                                status_catalog_id = :reExaminationStatusId
                                        )
                                ) meritaudit
                            WHERE
                                rev_rank = 3
                                AND c_last_modified_by = :userName
                        ) rank_3
                    UNION
                    SELECT
                        *
                    FROM
                        (
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
                                        RANK()
                                        OVER(PARTITION BY id
                                             ORDER BY
                                                 rev DESC
                                        ) AS rev_rank
                                    FROM
                                        tbl_merit_component_aud
                                    WHERE
                                        id IN (
                                            SELECT
                                                id
                                            FROM
                                                tbl_merit_component
                                            WHERE
                                                status_catalog_id = :reExaminationStatusId
                                        )
                                ) meritaudit
                            WHERE
                                rev_rank = 2
                                AND c_created_by = :userName
                        ) rank_2
                    WHERE
                        id NOT IN (
                            SELECT
                                id
                            FROM
                                (
                                    SELECT
                                        id, rev, c_created_by, c_last_modified_by, RANK()
                                                                                   OVER(PARTITION BY id
                                                                                        ORDER BY
                                                                                            rev DESC
                                                                                   ) AS rev_rank
                                    FROM
                                        tbl_merit_component_aud
                                    WHERE
                                        id IN (
                                            SELECT
                                                id
                                            FROM
                                                tbl_merit_component
                                            WHERE
                                                status_catalog_id = :reExaminationStatusId
                                        )
                                ) meritaudit
                            WHERE
                                rev_rank = 3
                                AND c_last_modified_by = :userName
                        )
                )
                          """, nativeQuery = true)
    Integer getNumberOfExpertWorkInWorkSpace(Long reExaminationStatusId, String userName);

    @Query(value = """
            SELECT
                id
            FROM
                (
                    SELECT
                        *
                    FROM
                        (
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
                                        RANK()
                                        OVER(PARTITION BY id
                                             ORDER BY
                                                 rev DESC
                                        ) AS rev_rank
                                    FROM
                                        tbl_merit_component_aud
                                    WHERE
                                        id IN (
                                            SELECT
                                                id
                                            FROM
                                                tbl_merit_component
                                            WHERE
                                                status_catalog_id = :reExaminationStatusId
                                        )
                                ) meritaudit
                            WHERE
                                rev_rank = 3
                                AND c_last_modified_by = :userName
                        ) rank_3
                    UNION
                    SELECT
                        *
                    FROM
                        (
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
                                        RANK()
                                        OVER(PARTITION BY id
                                             ORDER BY
                                                 rev DESC
                                        ) AS rev_rank
                                    FROM
                                        tbl_merit_component_aud
                                    WHERE
                                        id IN (
                                            SELECT
                                                id
                                            FROM
                                                tbl_merit_component
                                            WHERE
                                                status_catalog_id = :reExaminationStatusId
                                        )
                                ) meritaudit
                            WHERE
                                rev_rank = 2
                                AND c_created_by = :userName
                        ) rank_2
                    WHERE
                        id NOT IN (
                            SELECT
                                id
                            FROM
                                (
                                    SELECT
                                        id, rev, c_created_by, c_last_modified_by, RANK()
                                                                                   OVER(PARTITION BY id
                                                                                        ORDER BY
                                                                                            rev DESC
                                                                                   ) AS rev_rank
                                    FROM
                                        tbl_merit_component_aud
                                    WHERE
                                        id IN (
                                            SELECT
                                                id
                                            FROM
                                                tbl_merit_component
                                            WHERE
                                                status_catalog_id = :reExaminationStatusId
                                        )
                                ) meritaudit
                            WHERE
                                rev_rank = 3
                                AND c_last_modified_by = :userName
                        )
                )
                          """, nativeQuery = true)
    List<Long> getExpertWorkInWorkSpace(Long reExaminationStatusId, String userName);
}
