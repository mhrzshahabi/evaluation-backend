package com.nicico.evaluation.equipment.standard_test;

import com.nicico.evaluation.equipment.standard_test_status.StandardTestStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface StandardTestRepository extends PagingAndSortingRepository<StandardTest, Long>, JpaSpecificationExecutor<StandardTestStatus> {

     Optional<StandardTest> findById(Long id);
     Optional<StandardTest> findByTitle(String title);

    List<StandardTest> findAll();

    Page<StandardTest> findAll(Pageable pageable);

    void deleteByIdIn(List<Long> ids);



}
