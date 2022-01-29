package org.demo.simpleentity;

import java.util.List;
import java.util.Optional;

import org.springframework.data.repository.Repository;

@org.springframework.stereotype.Repository
public interface SimpleEntityRepository extends Repository<SimpleEntity, Long> {

    // methods with same signatures as in SimpleJpaRepository interface(s):

    SimpleEntity getById(Long id); // same signature as 'getOne'

    SimpleEntity getOne(Long id);

    Optional<SimpleEntity> findById(Long id);

    void deleteAllInBatch();

    SimpleEntity save(SimpleEntity entity);

    List<SimpleEntity> findAll();

}
