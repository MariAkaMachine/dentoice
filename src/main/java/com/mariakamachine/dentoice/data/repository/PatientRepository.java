package com.mariakamachine.dentoice.data.repository;

import com.mariakamachine.dentoice.data.entity.PatientEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
public interface PatientRepository extends JpaRepository<PatientEntity, Long> {

    @Transactional(readOnly = true)
    Optional<PatientEntity> findById(long id);

    @Transactional(readOnly = true)
    List<PatientEntity> findByNameStartsWithIgnoreCase(String name);

    @Transactional(readOnly = true)
    List<PatientEntity> findAll();

}
