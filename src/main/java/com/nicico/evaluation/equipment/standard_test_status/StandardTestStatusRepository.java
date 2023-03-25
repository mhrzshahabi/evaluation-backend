package com.nicico.evaluation.equipment.standard_test_status;

import org.springframework.data.domain.Page;

import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface StandardTestStatusRepository extends PagingAndSortingRepository<StandardTestStatus, Long>, JpaSpecificationExecutor<StandardTestStatus> {

     Optional<StandardTestStatus> findById(Long id);
     Optional<StandardTestStatus> findByTitle(String title);

    List<StandardTestStatus> findAll();

    Page<StandardTestStatus> findAll(Pageable pageable);

    void deleteByIdIn(List<Long> ids);



}
