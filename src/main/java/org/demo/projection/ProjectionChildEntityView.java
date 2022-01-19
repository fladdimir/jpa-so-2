package org.demo.projection;

import com.blazebit.persistence.view.EntityView;
import com.blazebit.persistence.view.IdMapping;

@EntityView(ProjectionChildEntity.class)
public interface ProjectionChildEntityView {

    @IdMapping
    Long getId();

    int getCounter();
}
