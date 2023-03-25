//package com.nicico.evaluation.equipment.unit_measurment;
//
//import com.nicico.evaluation.equipment.standard_test_status.StandardTestStatus;
//import org.springframework.data.domain.Page;
//import org.springframework.data.domain.Pageable;
//import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
//import org.springframework.data.repository.PagingAndSortingRepository;
//import org.springframework.stereotype.Repository;
//
//import java.util.List;
//import java.util.Optional;
//
//@Repository
//public interface UnitMeasurmentRepository extends PagingAndSortingRepository<UnitMeasurment, Long>, JpaSpecificationExecutor<StandardTestStatus> {
//
//     Optional<UnitMeasurment> findById(Long id);
//     Optional<UnitMeasurment> findByTitle(String title);
//     Optional<UnitMeasurment> findByAbreviation(String abreviation);
//
//    List<UnitMeasurment> findAll();
//
//    Page<UnitMeasurment> findAll(Pageable pageable);
//
//    void deleteByIdIn(List<Long> ids);
//
//
//
//}
