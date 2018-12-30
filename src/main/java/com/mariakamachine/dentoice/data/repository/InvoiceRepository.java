package com.mariakamachine.dentoice.data.repository;

import com.mariakamachine.dentoice.data.entity.InvoiceEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface InvoiceRepository extends JpaRepository<InvoiceEntity, Long> {

    @Transactional(readOnly = true)
    Optional<InvoiceEntity> findById(long id);

    @Transactional(readOnly = true)
    List<InvoiceEntity> findAllByDentistIdOrderByDateAsc(long id);

    @Transactional(readOnly = true)
    List<InvoiceEntity> findAllByDentistIdAndDateAfterAndDateBeforeOrderByDateAsc(long id, LocalDate from, LocalDate to);

}
