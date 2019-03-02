package com.mariakamachine.dentoice.data.repository;

import com.mariakamachine.dentoice.data.entity.InvoiceEntity;
import com.mariakamachine.dentoice.data.enums.InvoiceType;
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
    List<InvoiceEntity> findAllByInvoiceTypeEqualsAndDateAfterAndDateBeforeOrderByDateAsc(InvoiceType invoiceType, LocalDate from, LocalDate to);

    @Transactional(readOnly = true)
    List<InvoiceEntity> findAllByDentistIdAndInvoiceTypeEqualsAndDateAfterAndDateBeforeOrderByDateAsc(long id, InvoiceType invoiceType, LocalDate from, LocalDate to);

    @Transactional(readOnly = true)
    List<InvoiceEntity> findAllByInvoiceTypeEquals(InvoiceType invoiceType);

    @Transactional(readOnly = true)
    List<InvoiceEntity> findAllByDentistIdAndInvoiceTypeEquals(long id, InvoiceType invoiceType);

}
