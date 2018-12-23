package com.mariakamachine.dentoice.rest.resource;

import com.mariakamachine.dentoice.data.entity.EffortEntity;
import com.mariakamachine.dentoice.rest.dto.Effort;
import com.mariakamachine.dentoice.service.EffortService;
import com.mariakamachine.dentoice.util.validation.Numeric;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.MediaType.APPLICATION_JSON_UTF8_VALUE;

@RestController
@RequestMapping("/v1/efforts")
@CrossOrigin
@Validated
public class EffortResource {

    private final EffortService service;

    @Autowired
    public EffortResource(EffortService service) {
        this.service = service;
    }

    @PostMapping(path = "/create", consumes = APPLICATION_JSON_UTF8_VALUE)
    @ResponseStatus(CREATED)
    public EffortEntity create(@RequestBody @Valid Effort effort) {
        return service.create(effort);
    }

    @PatchMapping(path = "/{position}", consumes = APPLICATION_JSON_UTF8_VALUE)
    public EffortEntity update(@PathVariable @Numeric String position, @RequestBody @Valid Effort effort) {
        return service.update(position, effort);
    }

    @DeleteMapping(path = "/{position}")
    public void delete(@PathVariable @Numeric String position) {
        service.delete(position);
    }

    @GetMapping(path = "/{position}", produces = APPLICATION_JSON_UTF8_VALUE)
    public EffortEntity get(@PathVariable @Numeric String position) {
        return service.getByPosition(position);
    }

    @GetMapping(produces = APPLICATION_JSON_UTF8_VALUE)
    public List<EffortEntity> getAll() {
        return service.getAll();
    }

}
