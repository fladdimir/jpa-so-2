package org.demo.projection;

import java.util.List;

import com.blazebit.persistence.SubqueryInitiator;
import com.blazebit.persistence.view.EntityView;
import com.blazebit.persistence.view.IdMapping;
import com.blazebit.persistence.view.Limit;
import com.blazebit.persistence.view.Mapping;
import com.blazebit.persistence.view.MappingSubquery;
import com.blazebit.persistence.view.SubqueryProvider;

/**
 * DTO containing only the 3 most popular children DTOs, as well as a sum of the
 * popularity of all children (e.g. as sort criterium).
 */
@EntityView(ProjectionEntity.class)
public interface ProjectionEntityView {

    @IdMapping
    Long getId();

    @Mapping(value = "children")
    @Limit(limit = "3", order = { "counter DESC" })
    List<ProjectionChildEntityView> getThe3MostCountedChildren();

    @MappingSubquery(ChildrenCounterSumSubqueryProvider.class)
    Long getCounterSum();

    class ChildrenCounterSumSubqueryProvider implements SubqueryProvider {

        @Override // todo: use query-dsl
        public <T> T createSubquery(SubqueryInitiator<T> subqueryBuilder) {
            return subqueryBuilder.from(ProjectionChildEntity.class, "child") //
                    .select("SUM(counter)") //
                    .where("child.parent.id").eqExpression("EMBEDDING_VIEW(id)")//
                    .groupBy("child.parent.id")
                    .end();
        }

    }

}
