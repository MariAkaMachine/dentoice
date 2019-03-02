package com.mariakamachine.dentoice.data.repository;

import com.mariakamachine.dentoice.data.entity.MonthlyEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
public interface MonthliesRepository extends JpaRepository<MonthlyEntity, Long> {

    @Transactional(readOnly = true)
    Optional<MonthlyEntity> findById(long id);

    @Transactional(readOnly = true)
    List<MonthlyEntity> findAllByDentistIdOrderByDateDesc(long id);

}
