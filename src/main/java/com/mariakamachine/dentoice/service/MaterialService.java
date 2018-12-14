package com.mariakamachine.dentoice.service;

import com.mariakamachine.dentoice.data.entity.MaterialEntity;
import com.mariakamachine.dentoice.data.repository.MaterialRepository;
import com.mariakamachine.dentoice.exception.NotFoundException;
import com.mariakamachine.dentoice.rest.dto.Material;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityExistsException;
import java.util.List;

import static java.lang.String.format;

@Service
public class MaterialService {

    private final MaterialRepository repository;

    @Autowired
    public MaterialService(MaterialRepository repository) {
        this.repository = repository;
    }

    public MaterialEntity create(Material material) {
        if (repository.findByPosition(material.getPosition()).isPresent()) {
            throw new EntityExistsException(format("material with [ position: %s ] already exists", material.getPosition()));
        }
        return repository.save(new MaterialEntity(material));
    }

    public MaterialEntity update(String position, Material material) {
        MaterialEntity entity = repository.findByPosition(position)
                .orElseThrow(() -> new NotFoundException(format("could not find material with [ position: %s ]", position)));
        return repository.save(entity.updateEntity(material));
    }

    public void delete(String position) {
        repository.deleteById(position);
    }

    public MaterialEntity getByPosition(String position) {
        return repository.findByPosition(position)
                .orElseThrow(() -> new NotFoundException(format("could not find material with [ position: %s ]", position)));
    }

    public List<MaterialEntity> getAll() {
        return repository.findAll();
    }

}
