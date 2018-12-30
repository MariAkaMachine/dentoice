package com.mariakamachine.dentoice.service;

import com.mariakamachine.dentoice.data.entity.DentistEntity;
import com.mariakamachine.dentoice.data.repository.DentistRepository;
import com.mariakamachine.dentoice.exception.NotFoundException;
import com.mariakamachine.dentoice.rest.dto.Dentist;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

import static java.lang.String.format;

@Service
public class DentistService {

    private final DentistRepository repository;

    @Autowired
    public DentistService(DentistRepository repository) {
        this.repository = repository;
    }

    public DentistEntity create(Dentist dentist) {
        return repository.save(new DentistEntity(dentist));
    }

    public DentistEntity update(long id, Dentist dentist) {
        return repository.save(getById(id)
                .updateEntity(dentist));
    }

    public void delete(long id) {
        repository.deleteById(id);
    }

    public DentistEntity getById(long id) {
        return repository.findById(id)
                .orElseThrow(() -> new NotFoundException(format("could not find dentist with [ id: %d ]", id)));
    }

    public List<DentistEntity> getByLastName(String lastName) {
        return repository.findByLastNameStartsWithIgnoreCase(lastName);
    }

    public List<DentistEntity> getByFirstNameAndLastName(String firstName, String lastName) {
        return repository.findByFirstNameStartsWithIgnoreCaseAndLastNameStartsWithIgnoreCase(firstName, lastName);
    }

    public List<DentistEntity> getAll() {
        return repository.findAll();
    }

}
