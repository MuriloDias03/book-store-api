package com.murilodias03.bookstore.repositories;

import com.murilodias03.bookstore.integrationtests.testcontainers.AbstractIntegrationTest;
import com.murilodias03.bookstore.model.Person;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class PersonRepositoryTest extends AbstractIntegrationTest {

    @Autowired
    PersonRepository personRepository;
    private static Person person;

    @BeforeAll
    static void setUp() {
        person = new Person();
    }


    @Test
    @Order(1)
    void findPeopleByName() {
        Pageable pageable = PageRequest.of(
                0,
                12,
                Sort.by(Sort.Direction.ASC, "firstName"));

        person = personRepository.findPeopleByName("awn", pageable).getContent().get(1);

        assertNotNull(person);
        assertNotNull(person.getId());
        assertEquals("Fawnia", person.getFirstName());
        assertEquals("Tutt", person.getLastName());
        assertEquals("36 Graedel Avenue", person.getAddress());
        assertEquals("Female", person.getGender());
        assertTrue(person.getEnabled());
    }


    @Test
    @Order(2)
    void disablePerson() {

        Long id = person.getId();
        personRepository.disablePerson(id);

        var result = personRepository.findById(id);
        person = result.get();

        assertNotNull(person);
        assertNotNull(person.getId());
        assertEquals("Fawnia", person.getFirstName());
        assertEquals("Tutt", person.getLastName());
        assertEquals("36 Graedel Avenue", person.getAddress());
        assertEquals("Female", person.getGender());
        assertFalse(person.getEnabled());
    }
}