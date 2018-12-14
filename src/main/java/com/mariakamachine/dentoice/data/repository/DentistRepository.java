package com.mariakamachine.dentoice.data.repository;

import com.mariakamachine.dentoice.data.entity.DentistEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
public interface DentistRepository extends JpaRepository<DentistEntity, Long> {

    @Transactional(readOnly = true)
    Optional<DentistEntity> findById(long id);

    @Transactional(readOnly = true)
    List<DentistEntity> findByLastNameStartsWithIgnoreCase(String lastName);

    @Transactional(readOnly = true)
    List<DentistEntity> findByFirstNameStartsWithIgnoreCaseAndLastNameStartsWithIgnoreCase(String firstName, String lastName);

    @Transactional(readOnly = true)
    List<DentistEntity> findAll();

}
