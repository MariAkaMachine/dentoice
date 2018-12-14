package com.mariakamachine.dentoice.service;

import com.mariakamachine.dentoice.data.entity.PatientEntity;
import com.mariakamachine.dentoice.data.repository.PatientRepository;
import com.mariakamachine.dentoice.exception.NotFoundException;
import com.mariakamachine.dentoice.rest.dto.Patient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

import static java.lang.String.format;

@Service
public class PatientService {

    private final PatientRepository repository;

    @Autowired

    public PatientService(PatientRepository repository) {
        this.repository = repository;
    }

    public PatientEntity create(Patient patient) {
        return repository.save(new PatientEntity(patient));
    }

    public PatientEntity update(long id, Patient patient) {
        PatientEntity entity = repository.findById(id)
                .orElseThrow(() -> new NotFoundException(format("could not find Patient with [ id: %d ]", id)));
        return repository.save(entity.updateEntity(patient));
    }

    public void delete(long id) {
        // TODO must not delete if ID in invoices
        repository.deleteById(id);
    }

    public PatientEntity getById(long id) {
        return repository.findById(id)
                .orElseThrow(() -> new NotFoundException(format("could not find Patient with [ id: %d ]", id)));
    }

    public List<PatientEntity> getByName(String name) {
        return repository.findByNameStartsWithIgnoreCase(name);
    }

    public List<PatientEntity> getAll() {
        return repository.findAll();
    }

}
