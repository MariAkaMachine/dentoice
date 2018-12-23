package com.mariakamachine.dentoice.rest.resource;

import com.mariakamachine.dentoice.data.entity.PatientEntity;
import com.mariakamachine.dentoice.rest.dto.Patient;
import com.mariakamachine.dentoice.service.PatientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import java.util.List;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.MediaType.APPLICATION_JSON_UTF8_VALUE;


@RestController
@RequestMapping("/v1/patients")
@CrossOrigin
@Validated
public class PatientResource {

    private final PatientService service;

    @Autowired
    public PatientResource(PatientService service) {
        this.service = service;
    }

    @PostMapping(path = "/create", consumes = APPLICATION_JSON_UTF8_VALUE)
    @ResponseStatus(CREATED)
    public PatientEntity create(@RequestBody @Valid Patient patient) {
        return service.create(patient);
    }

    @PatchMapping(path = "/{id}", consumes = APPLICATION_JSON_UTF8_VALUE)
    public PatientEntity update(@PathVariable Long id, @RequestBody @Valid Patient patient) {
        return service.update(id, patient);
    }

    @DeleteMapping(path = "/{id}")
    public void delete(@PathVariable Long id) {
        service.delete(id);
    }

    @GetMapping(path = "/{id}", produces = APPLICATION_JSON_UTF8_VALUE)
    public PatientEntity get(@PathVariable Long id) {
        return service.getById(id);
    }

    @GetMapping(params = {"name"}, produces = APPLICATION_JSON_UTF8_VALUE)
    public List<PatientEntity> get(@RequestParam @NotBlank String name) {
        return service.getByName(name);
    }

    @GetMapping(produces = APPLICATION_JSON_UTF8_VALUE)
    public List<PatientEntity> getAll() {
        return service.getAll();
    }

}
