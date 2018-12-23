package com.mariakamachine.dentoice.rest.resource;

import com.mariakamachine.dentoice.data.entity.InvoiceEntity;
import com.mariakamachine.dentoice.rest.dto.Invoice;
import com.mariakamachine.dentoice.service.InvoiceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

import static org.springframework.format.annotation.DateTimeFormat.ISO.DATE;
import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.MediaType.APPLICATION_JSON_UTF8_VALUE;
import static org.springframework.http.MediaType.APPLICATION_PDF_VALUE;

@RestController
@RequestMapping("/v1/invoices")
@CrossOrigin
@Validated
public class InvoiceResource {

    private final InvoiceService service;

    @Autowired
    public InvoiceResource(InvoiceService service) {
        this.service = service;
    }

    @PostMapping(path = "/create", consumes = APPLICATION_JSON_UTF8_VALUE)
    @ResponseStatus(CREATED)
    public InvoiceEntity create(@RequestBody Invoice invoice) {
        return service.create(invoice);
    }

    @GetMapping(path = "/{id}", produces = APPLICATION_JSON_UTF8_VALUE)
    public InvoiceEntity getById(@PathVariable Long id) {
        return service.getById(id);
    }

    @GetMapping(path = "/{id}/pdf", produces = APPLICATION_PDF_VALUE)
    public byte[] getPdfById(@PathVariable Long id) {
        return service.getPdfById(id);
    }

    @GetMapping(path = "/from/{from}/to/{to}/dentists/{id}/pdf", produces = APPLICATION_PDF_VALUE)
    public byte[] getByDentistId(@PathVariable @DateTimeFormat(iso = DATE) LocalDate from, @PathVariable @DateTimeFormat(iso = DATE) LocalDate to, @PathVariable Long id) {
        return service.getMonthlyPdf(from, to, id);
    }

}
