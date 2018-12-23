package com.mariakamachine.dentoice.rest.resource;

import com.mariakamachine.dentoice.data.entity.MaterialEntity;
import com.mariakamachine.dentoice.rest.dto.Material;
import com.mariakamachine.dentoice.service.MaterialService;
import com.mariakamachine.dentoice.util.validation.Numeric;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.MediaType.APPLICATION_JSON_UTF8_VALUE;

@RestController
@RequestMapping("/v1/materials")
@CrossOrigin
@Validated
public class MaterialResource {

    private final MaterialService service;

    @Autowired
    public MaterialResource(MaterialService service) {
        this.service = service;
    }

    @PostMapping(path = "/create", consumes = APPLICATION_JSON_UTF8_VALUE)
    @ResponseStatus(CREATED)
    public MaterialEntity create(@RequestBody @Valid Material material) {
        return service.create(material);
    }

    @PatchMapping(path = "/{position}", consumes = APPLICATION_JSON_UTF8_VALUE)
    public MaterialEntity update(@PathVariable @Numeric String position, @RequestBody @Valid Material material) {
        return service.update(position, material);
    }

    @DeleteMapping(path = "/{position}")
    public void delete(@PathVariable @Numeric String position) {
        service.delete(position);
    }

    @GetMapping(path = "/{position}", produces = APPLICATION_JSON_UTF8_VALUE)
    public MaterialEntity get(@PathVariable @Numeric String position) {
        return service.getByPosition(position);
    }

    @GetMapping(produces = APPLICATION_JSON_UTF8_VALUE)
    public List<MaterialEntity> getAll() {
        return service.getAll();
    }

}
