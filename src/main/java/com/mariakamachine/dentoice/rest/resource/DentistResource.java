package com.mariakamachine.dentoice.rest.resource;

import com.mariakamachine.dentoice.data.entity.DentistEntity;
import com.mariakamachine.dentoice.rest.dto.Dentist;
import com.mariakamachine.dentoice.service.DentistService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import java.util.List;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.MediaType.APPLICATION_JSON_UTF8_VALUE;

@RestController
@RequestMapping("/v1/dentists")
@CrossOrigin
public class DentistResource {

    private final DentistService service;

    @Autowired
    public DentistResource(DentistService service) {
        this.service = service;
    }

    @PostMapping(path = "/create", consumes = APPLICATION_JSON_UTF8_VALUE)
    @ResponseStatus(CREATED)
    public DentistEntity create(@RequestBody @Valid Dentist dentist) {
        return service.create(dentist);
    }

    @PatchMapping(path = "/{id}", consumes = APPLICATION_JSON_UTF8_VALUE)
    public DentistEntity update(@PathVariable Long id, @RequestBody @Valid Dentist dentist) {
        return service.update(id, dentist);
    }

    @DeleteMapping(path = "/{id}")
    public void delete(@PathVariable Long id) {
        service.delete(id);
    }

    @GetMapping(path = "/{id}", produces = APPLICATION_JSON_UTF8_VALUE)
    public DentistEntity get(@PathVariable Long id) {
        return service.getById(id);
    }

    @GetMapping(params = {"lastName"}, produces = APPLICATION_JSON_UTF8_VALUE)
    public List<DentistEntity> get(@RequestParam @NotBlank String lastName) {
        return service.getByLastName(lastName);
    }

    @GetMapping(params = {"firstName", "lastName"}, produces = APPLICATION_JSON_UTF8_VALUE)
    public List<DentistEntity> get(@RequestParam String firstName, @RequestParam @NotBlank String lastName) {
        return service.getByFirstNameAndLastName(firstName, lastName);
    }

    @GetMapping(produces = APPLICATION_JSON_UTF8_VALUE)
    public List<DentistEntity> getAll() {
        return service.getAll();
    }

}
