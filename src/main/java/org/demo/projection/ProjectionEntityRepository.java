package org.demo.projection;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface ProjectionEntityRepository extends JpaRepository<ProjectionEntity, Long> {

    @Query("SELECT DISTINCT pe FROM ProjectionEntity pe JOIN FETCH pe.children pec WHERE pec.counter > 4")
    List<ProjectionEntity> fetchAllWithChildrenWhenCounterGt3();

}
