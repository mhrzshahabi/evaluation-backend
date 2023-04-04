package com.nicico.evaluation.repository;

import com.nicico.evaluation.model.CatalogType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface CatalogTypeRepository extends JpaRepository<CatalogType, Long> {

    Optional<CatalogType> findByCode(@Param("code") String code);
}
