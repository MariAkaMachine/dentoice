package com.mariakamachine.dentoice.service;

import com.mariakamachine.dentoice.data.entity.EffortEntity;
import com.mariakamachine.dentoice.data.repository.EffortRepository;
import com.mariakamachine.dentoice.exception.NotFoundException;
import com.mariakamachine.dentoice.rest.dto.Effort;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

import static java.lang.String.format;

@Service
public class EffortService {

    private final EffortRepository repository;

    @Autowired
    public EffortService(EffortRepository repository) {
        this.repository = repository;
    }

    public EffortEntity create(Effort effort) {
        return repository.save(new EffortEntity(effort));
    }

    public EffortEntity update(String position, Effort effort) {
        EffortEntity entity = repository.findByPosition(position)
                                      .orElseThrow(() -> new NotFoundException(format("could not find effort with [ position: %s ]", position)));
        return repository.save(entity.updateEntity(effort));
    }

    public void delete(String position) {
        repository.deleteById(position);
    }

    public EffortEntity getByPosition(String position) {
        return repository.findByPosition(position)
                       .orElseThrow(() -> new NotFoundException(format("could not find effort with [ position: %s ]", position)));
    }

    public List<EffortEntity> getAll() {
        return repository.findAll();
    }

}
