
package com.mariakamachine.dentoice.data.repository;

import com.mariakamachine.dentoice.data.entity.EffortEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
public interface EffortRepository extends JpaRepository<EffortEntity, String> {

    @Transactional(readOnly = true)
    Optional<EffortEntity> findByPosition(String position);

    @Transactional(readOnly = true)
    List<EffortEntity> findAll();

}
