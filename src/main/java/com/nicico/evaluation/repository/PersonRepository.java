package com.nicico.evaluation.repository;

import com.nicico.evaluation.model.Person;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PersonRepository extends JpaRepository<Person, Long>, JpaSpecificationExecutor<Person> {

    Person findByNationalCode(String nationalCode);
    Person findByPostCode(String postCode);
    List<Person> findAllByNationalCodeIn(List<String> nationalCode);

}
