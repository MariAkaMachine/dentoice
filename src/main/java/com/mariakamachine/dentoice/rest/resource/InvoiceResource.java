package com.mariakamachine.dentoice.rest.resource;

import com.mariakamachine.dentoice.data.entity.InvoiceEntity;
import com.mariakamachine.dentoice.model.FileResource;
import com.mariakamachine.dentoice.rest.dto.Invoice;
import com.mariakamachine.dentoice.service.InvoiceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.Min;
import java.time.LocalDate;
import java.util.List;

import static org.springframework.format.annotation.DateTimeFormat.ISO.DATE;
import static org.springframework.http.HttpHeaders.CONTENT_DISPOSITION;
import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.NO_CONTENT;
import static org.springframework.http.MediaType.APPLICATION_JSON_UTF8_VALUE;
import static org.springframework.http.MediaType.APPLICATION_OCTET_STREAM_VALUE;
import static org.springframework.http.ResponseEntity.ok;

@RestController
@RequestMapping("/invoices")
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

    @PatchMapping(path = "/{id}", consumes = APPLICATION_JSON_UTF8_VALUE)
    public InvoiceEntity update(@PathVariable Long id, @RequestBody Invoice invoice) {
        return service.update(id, invoice);
    }

    @DeleteMapping(path = "/{id}")
    @ResponseStatus(NO_CONTENT)
    public void delete(@PathVariable Long id) {
        service.delete(id);
    }

    @GetMapping(path = "/{id}", produces = APPLICATION_JSON_UTF8_VALUE)
    public InvoiceEntity getById(@PathVariable Long id) {
        return service.getById(id);
    }

    @GetMapping(path = "/{id}/xml", produces = APPLICATION_OCTET_STREAM_VALUE)
    @ResponseBody
    public ResponseEntity<Resource> getXmlById(@PathVariable Long id) {
        FileResource xmlResource = service.getXmlById(id);
        return ok()
                .header(CONTENT_DISPOSITION, "attachment; filename=\"" + xmlResource.getFileName() + "\"")
                .body(xmlResource.getResource());
    }

    @GetMapping(path = "/{id}/pdf", produces = APPLICATION_OCTET_STREAM_VALUE)
    @ResponseBody
    public ResponseEntity<Resource> getPdfById(@PathVariable Long id) {
        FileResource pdfResource = service.getPdfById(id);
        return ok()
                .header(CONTENT_DISPOSITION, "attachment; filename=\"" + pdfResource.getFileName() + "\"")
                .body(pdfResource.getResource());
    }

    @GetMapping(path = "/from/{from}/to/{to}", produces = APPLICATION_JSON_UTF8_VALUE)
    public List<InvoiceEntity> getAllFromTo(@PathVariable @DateTimeFormat(iso = DATE) LocalDate from, @PathVariable @DateTimeFormat(iso = DATE) LocalDate to) {
        return service.getAllFromTo(from, to);
    }

    @GetMapping(path = "/from/{from}/to/{to}", params = {"dentist"}, produces = APPLICATION_JSON_UTF8_VALUE)
    public List<InvoiceEntity> getAllFromToByDentist(@PathVariable @DateTimeFormat(iso = DATE) LocalDate from, @PathVariable @DateTimeFormat(iso = DATE) LocalDate to, @RequestParam @Min(1) Long dentist) {
        return service.getAllByDentistFromTo(dentist, from, to);
    }

    @GetMapping(path = "/estimates", produces = APPLICATION_JSON_UTF8_VALUE)
    public List<InvoiceEntity> getAllEstimates() {
        return service.getAllEstimates();
    }

    @GetMapping(path = "/estimates", params = {"dentist"}, produces = APPLICATION_JSON_UTF8_VALUE)
    public List<InvoiceEntity> getAllEstimates(@RequestParam @Min(1) Long dentist) {
        return service.getAllEstimatesByDentist(dentist);
    }

}
