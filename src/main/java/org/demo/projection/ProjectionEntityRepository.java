package org.demo.projection;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProjectionEntityRepository extends JpaRepository<ProjectionEntity, Long> {

}
