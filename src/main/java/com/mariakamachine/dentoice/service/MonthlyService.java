package com.mariakamachine.dentoice.service;

import com.mariakamachine.dentoice.data.entity.InvoiceEntity;
import com.mariakamachine.dentoice.data.entity.MonthlyEntity;
import com.mariakamachine.dentoice.data.repository.MonthliesRepository;
import com.mariakamachine.dentoice.exception.NotFoundException;
import com.mariakamachine.dentoice.model.FileResource;
import com.mariakamachine.dentoice.rest.dto.Monthly;
import com.mariakamachine.dentoice.util.invoice.pdf.PdfGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

import static com.mariakamachine.dentoice.util.invoice.InvoiceCalculator.calculateMonthlyInvoiceSum;
import static java.lang.String.format;
import static java.time.LocalDate.now;
import static java.time.format.DateTimeFormatter.ofPattern;
import static java.util.Arrays.stream;
import static java.util.stream.Collectors.toList;

@Service
public class MonthlyService {

    private final MonthliesRepository repository;
    private final InvoiceService invoiceService;
    private final DentistService dentistService;

    @Autowired
    public MonthlyService(MonthliesRepository repository, InvoiceService invoiceService, DentistService dentistService) {
        this.repository = repository;
        this.invoiceService = invoiceService;
        this.dentistService = dentistService;
    }

    public MonthlyEntity create(Monthly monthly) {
        MonthlyEntity entity = new MonthlyEntity();
        entity.setDentist(dentistService.getById(monthly.getDentist()));
        entity.setDescription(now().format(ofPattern("MM/yyyy")));
        entity.setDate(monthly.getDate());
        entity.setSkonto(monthly.getSkonto());
        entity.setInvoices(monthly.getInvoices());
        entity.setTotal(getMonthlyTotal(monthly));
        return repository.save(entity);
    }

    public MonthlyEntity update(long id, Monthly monthly) {
        MonthlyEntity entity = getById(id);
        return repository.save(entity
                .updateEntity(monthly, dentistService.getById(monthly.getDentist()), getMonthlyTotal(monthly)));
    }

    public void delete(long id) {
        repository.deleteById(id);
    }

    public MonthlyEntity getById(long id) {
        return repository.findById(id)
                .orElseThrow(() -> new NotFoundException(format("could not find monthly with [ id: %d ]", id)));
    }

    public List<MonthlyEntity> getAllMonthliesByDentist(long dentistId) {
        return repository.findAllByDentistIdOrderByDateDesc(dentistId);
    }

    public FileResource getMonthlyPdf(long id) {
        MonthlyEntity entity = getById(id);
        return new PdfGenerator().generateMonthlyPdfInvoice(entity, getInvoiceEntities(entity.getInvoices()));
    }

    private List<InvoiceEntity> getInvoiceEntities(Long[] invoices) {
        return stream(invoices)
                .map(invoiceService::getById)
                .collect(toList());
    }

    private BigDecimal getMonthlyTotal(Monthly monthly) {
        return calculateMonthlyInvoiceSum(getInvoiceEntities(monthly.getInvoices()), monthly.getSkonto()).getTotal();
    }

}
