package org.demo.polymorphic;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

import org.hibernate.internal.SessionImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class AnimalsTest {

    @Autowired
    private AnimalRepository animalRepository;

    @Autowired
    private EntityManagerFactory entityManagerFactory;

    private EntityManager em;

    @BeforeEach
    void beforeEach() {
        animalRepository.deleteAllInBatch();
        assertThat(animalRepository.findAll()).isEmpty();

        cat = animalRepository.save(Cat.builder().id("cat_1").build());
        dog = animalRepository.save(Dog.builder().id("dog_1").build());
        sheep = animalRepository.save(Sheep.builder().id("sheep_1").build());

        assertThat(animalRepository.findAll()).hasSize(3);

        em = entityManagerFactory.createEntityManager();
    }

    private void assertCorrectPets(List<?> pets) {
        assertThat(pets.stream().map(Animal.class::cast)).containsExactlyInAnyOrder(cat, dog); // .doesNotContain(sheep);
    }

    Cat cat;
    Dog dog;
    Sheep sheep;

    @Test
    @SuppressWarnings("unchecked")
    void testInterfaceQuery() {

        List<PetInterface> pets = em.unwrap(SessionImpl.class).createCriteria(PetInterface.class).list();

        assertCorrectPets(pets);
    }

    @Test
    @SuppressWarnings("unchecked")
    void testTypeQuery() {

        var petClasses = List.of(Cat.class, Dog.class);
        List<Animal> pets = em.createQuery("SELECT a FROM Animal a WHERE TYPE(a) in :classes")
                .setParameter("classes", petClasses).getResultList();

        assertCorrectPets(pets);
    }

    @Test
    @SuppressWarnings("unchecked")
    void testCommonSuperClassQuery() {

        List<AbstractPet> pets = em.createQuery("SELECT p from AbstractPet p").getResultList();

        assertCorrectPets(pets);
    }
}
