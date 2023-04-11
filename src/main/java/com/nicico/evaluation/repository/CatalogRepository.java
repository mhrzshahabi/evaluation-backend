package com.nicico.evaluation.repository;

import com.nicico.evaluation.model.Catalog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CatalogRepository extends JpaRepository<Catalog, Long>, JpaSpecificationExecutor<Catalog> {

    @Query("from Catalog catalog join CatalogType type  on catalog.catalogTypeId= type.id where type.code = :code ")
    List<Catalog> findAllByCatalogTypeCode(@Param("code") String code);
}
