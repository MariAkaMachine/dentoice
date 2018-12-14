package com.mariakamachine.dentoice.data.repository;

import com.mariakamachine.dentoice.data.entity.MaterialEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
public interface MaterialRepository extends JpaRepository<MaterialEntity, String> {

    @Transactional(readOnly = true)
    Optional<MaterialEntity> findByPosition(String position);

    @Transactional(readOnly = true)
    List<MaterialEntity> findAll();

}
