package org.demo.simpleentity;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.hibernate.LazyInitializationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class GetOneTest {

    @Autowired
    private SimpleEntityRepository repository;

    @BeforeEach
    void beforeEach() {
        repository.deleteAllInBatch();

        repository.save(new SimpleEntity(ID, "someStringValue"));
    }

    private static final Long ID = 21L;

    @Test
    void testEntityInitialization() {
        assertThat(repository.findAll()).hasSize(1);

        var getE0 = repository.getOne(ID); // EntityManager#getReference
        assertThrows(LazyInitializationException.class, () -> getE0.getSomeString());

        var findE0 = repository.findById(ID).get(); // EntityManager#find
        assertDoesNotThrow(() -> findE0.getSomeString());

        var pseudoQueryE0 = repository.getById(ID); // <- same signature as 'getOne'
        assertThrows(LazyInitializationException.class, () -> pseudoQueryE0.getSomeString());
    }
}
