package com.mariakamachine.dentoice.rest.resource;

import com.mariakamachine.dentoice.data.entity.MonthlyEntity;
import com.mariakamachine.dentoice.model.FileResource;
import com.mariakamachine.dentoice.rest.dto.Monthly;
import com.mariakamachine.dentoice.service.MonthlyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.Min;
import java.util.List;

import static org.springframework.http.HttpHeaders.CONTENT_DISPOSITION;
import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.NO_CONTENT;
import static org.springframework.http.MediaType.APPLICATION_JSON_UTF8_VALUE;
import static org.springframework.http.MediaType.APPLICATION_OCTET_STREAM_VALUE;
import static org.springframework.http.ResponseEntity.ok;

@RestController
@RequestMapping("/monthlies")
@CrossOrigin
@Validated
public class MonthlyResource {

    private final MonthlyService service;

    @Autowired
    public MonthlyResource(MonthlyService service) {
        this.service = service;
    }

    @PostMapping(path = "/create", consumes = APPLICATION_JSON_UTF8_VALUE)
    @ResponseStatus(CREATED)
    public MonthlyEntity create(@RequestBody Monthly monthly) {
        return service.create(monthly);
    }

    @PatchMapping(path = "/{id}", consumes = APPLICATION_JSON_UTF8_VALUE)
    public MonthlyEntity update(@PathVariable Long id, @RequestBody Monthly monthly) {
        return service.update(id, monthly);
    }

    @DeleteMapping(path = "/{id}")
    @ResponseStatus(NO_CONTENT)
    public void delete(@PathVariable Long id) {
        service.delete(id);
    }

    // get single
    @GetMapping(path = "/{id}", produces = APPLICATION_JSON_UTF8_VALUE)
    public MonthlyEntity getById(@PathVariable Long id) {
        return service.getById(id);
    }

    // get all by dentist
    @GetMapping(params = {"dentist"}, produces = APPLICATION_JSON_UTF8_VALUE)
    public List<MonthlyEntity> getAllMonthlies(@RequestParam @Min(1) Long dentist) {
        return service.getAllMonthliesByDentist(dentist);
    }

    // get pdf
    @GetMapping(path = "/{id}/pdf", produces = APPLICATION_OCTET_STREAM_VALUE)
    @ResponseBody
    public ResponseEntity<Resource> getByDentistId(@PathVariable Long id) {
        FileResource monthlyPdfResource = service.getMonthlyPdf(id);
        return ok()
                .header(CONTENT_DISPOSITION, "attachment; filename=\"" + monthlyPdfResource.getFileName() + "\"")
                .body(monthlyPdfResource.getResource());
    }

}
