package com.mariakamachine.dentoice.data.repository;

import com.mariakamachine.dentoice.data.entity.InvoiceEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface InvoiceRepository extends PagingAndSortingRepository<InvoiceEntity, Long> {

    @Transactional(readOnly = true)
    Optional<InvoiceEntity> findById(long id);

    @Transactional(readOnly = true)
    List<InvoiceEntity> findAllByDateAfterAndDateBeforeOrderByDateAsc(LocalDate from, LocalDate to);

    @Transactional(readOnly = true)
    Page<InvoiceEntity> findAllByDentistId(long id, Pageable pageable);

    @Transactional(readOnly = true)
    List<InvoiceEntity> findAllByDentistIdAndDateAfterAndDateBeforeOrderByDateAsc(long id, LocalDate from, LocalDate to);

}
