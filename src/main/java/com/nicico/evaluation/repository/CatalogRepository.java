package com.nicico.evaluation.repository;

import com.nicico.evaluation.model.Catalog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CatalogRepository extends JpaRepository<Catalog, Long>, JpaSpecificationExecutor<Catalog> {

    @Query("from Catalog catalog join CatalogType type  on catalog.catalogTypeId= type.id where type.code = :code order by catalog.value")
    List<Catalog> findAllByCatalogTypeCode(@Param("code") String code);

    Optional<Catalog> findByCode(String code);
    List<Catalog> findAllByCodeIn(List<String> codes);
}
