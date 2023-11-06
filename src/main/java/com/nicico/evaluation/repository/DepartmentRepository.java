package com.nicico.evaluation.repository;

import com.nicico.evaluation.model.Department;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DepartmentRepository extends JpaRepository<Department, Long>, JpaSpecificationExecutor<Department> {

    @Query(value = """
            SELECT DISTINCT
                ( c_mojtame_title )
            FROM
                view_department
            WHERE
                c_mojtame_title IS NOT NULL
            OFFSET :page ROWS FETCH NEXT :size ROWS ONLY     
            """, nativeQuery = true)
    List<String> findMojtameList(int page, int size);

    @Query(value = """
            SELECT
                COUNT(*)
            FROM
                (
                    SELECT DISTINCT
                        ( c_mojtame_title )
                    FROM
                        view_department
                    WHERE
                        c_mojtame_title IS NOT NULL
                )   
            """, nativeQuery = true)
    Integer findAllMojtameCount();

    @Query(value = """
            SELECT DISTINCT
                ( c_moavenat_title )
            FROM
                view_department
            WHERE
                c_moavenat_title IS NOT NULL
            OFFSET :page ROWS FETCH NEXT :size ROWS ONLY     
            """, nativeQuery = true)
    List<String> findMoavenatList(int page, int size);

    @Query(value = """
            SELECT
                COUNT(*)
            FROM
                (
                    SELECT DISTINCT
                        ( c_moavenat_title )
                    FROM
                        view_department
                    WHERE
                        c_moavenat_title IS NOT NULL
                )   
            """, nativeQuery = true)
    Integer findAllMoavenatCount();

    @Query(value = """
            SELECT DISTINCT
                ( c_omor_title )
            FROM
                view_department
            WHERE
                c_omor_title IS NOT NULL
            OFFSET :page ROWS FETCH NEXT :size ROWS ONLY     
            """, nativeQuery = true)
    List<String> findOmorList(int page, int size);

    @Query(value = """
            SELECT
                COUNT(*)
            FROM
                (
                    SELECT DISTINCT
                        ( c_omor_title )
                    FROM
                        view_department
                    WHERE
                        c_omor_title IS NOT NULL
                )   
            """, nativeQuery = true)
    Integer findAllOmorCount();

    @Query(value = """
            SELECT DISTINCT
                ( c_ghesmat_title )
            FROM
                view_department
            WHERE
                c_ghesmat_title IS NOT NULL
            OFFSET :page ROWS FETCH NEXT :size ROWS ONLY     
            """, nativeQuery = true)
    List<String> findGhesmatList(int page, int size);

    @Query(value = """
            SELECT
                COUNT(*)
            FROM
                (
                    SELECT DISTINCT
                        ( c_ghesmat_title )
                    FROM
                        view_department
                    WHERE
                        c_ghesmat_title IS NOT NULL
                )   
            """, nativeQuery = true)
    Integer findAllGhesmatCount();

}
