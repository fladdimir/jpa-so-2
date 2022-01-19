package org.demo.projection;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Order;

@SpringBootTest
class ProjectionTest {

    @Autowired
    private ProjectionEntityRepository parentRepository;

    @Autowired
    private ProjectionEntityViewRepository parentViewRepository;

    @Autowired
    private ProjectionChildEntityRepository childRepository;

    @BeforeEach
    void beforeEach() {
        childRepository.deleteAllInBatch();
        parentRepository.deleteAllInBatch();

        var counter = List.of(1, 2, 3, 4, 5);
        int nParents = 3;
        IntStream.rangeClosed(1, nParents).forEach(i -> {
            var parent = parentRepository.save(new ProjectionEntity());
            counter.forEach(c -> {
                var child = new ProjectionChildEntity();
                child.setParent(parent);
                child.setCounter(i * c);
                childRepository.save(child);
            });
        });
        assertThat(parentRepository.findAll()).hasSize(nParents);
        assertThat(childRepository.findAll()).hasSize(nParents * counter.size());
        System.out.println("\n BEFORE_EACH DONE \n");
    }

    @Test
    void test() {
        var result = parentViewRepository.findAll();
        assertThat(result).hasSize(3).allMatch(r -> r.getThe3MostCountedChildren().size() == 3);
        assertThat(result.stream().map(ProjectionEntityView::getCounterSum)).containsExactlyInAnyOrder(15L, 30L, 45L);
        assertThat(result.stream().map(ProjectionEntityView::getThe3MostCountedChildren)
                .map(cl -> cl.stream().map(ProjectionChildEntityView::getCounter).collect(Collectors.toList())))
                        .containsExactlyInAnyOrder(
                                List.of(5, 4, 3), List.of(10, 8, 6), List.of(15, 12, 9));
    }

    @Test
    void testPaged() {
        Pageable pageable = PageRequest.of(1, 2, Sort.by(Order.desc("counterSum"), Order.asc("id")));
        var resultPaged = parentViewRepository.findAll(pageable);
        assertThat(resultPaged.size()).isEqualTo(1);
        var result3 = resultPaged.get(0);
        assertThat(result3.getCounterSum()).isEqualTo(15);
        assertThat(result3.getThe3MostCountedChildren().stream().map(ProjectionChildEntityView::getCounter))
                .containsExactly(5, 4, 3);
    }

}
