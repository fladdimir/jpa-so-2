package org.demo.projection;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.Repository;

@org.springframework.stereotype.Repository
public interface ProjectionEntityViewRepository extends Repository<ProjectionEntity, Long> {

    List<ProjectionEntityView> findAll(Pageable pageable);

    List<ProjectionEntityView> findAll();
}
