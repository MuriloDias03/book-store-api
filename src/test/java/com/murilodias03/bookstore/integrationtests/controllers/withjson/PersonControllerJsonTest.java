package com.murilodias03.bookstore.integrationtests.controllers.withjson;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.murilodias03.bookstore.config.TestConfigs;
import com.murilodias03.bookstore.integrationtests.dto.PersonDTO;
import com.murilodias03.bookstore.integrationtests.dto.wrappers.WrapperPersonDTO;
import com.murilodias03.bookstore.integrationtests.testcontainers.AbstractIntegrationTest;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.specification.RequestSpecification;
import org.junit.jupiter.api.*;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;

import java.util.List;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class PersonControllerJsonTest extends AbstractIntegrationTest {

    private static RequestSpecification specification;
    private static ObjectMapper objectMapper;

    private static PersonDTO personDTO;

    @BeforeAll
    static void setUp() {
        objectMapper = new ObjectMapper();
        objectMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);

        personDTO = new PersonDTO();
    }


    @Test
    @Order(1)
    void createTest() throws JsonProcessingException {
        mockPerson();

        specification = new RequestSpecBuilder()
                .addHeader(TestConfigs.HEADER_PARAM_ORIGIN, TestConfigs.ORIGIN_FRONTEND)
                .setBasePath("/person")
                .setPort(TestConfigs.SERVER_PORT)
                .addFilter(new RequestLoggingFilter(LogDetail.ALL))
                .addFilter(new ResponseLoggingFilter(LogDetail.ALL))
                .build();

        var content = given(specification)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(personDTO)
                .when()
                .post()
                .then()
                .statusCode(200) // CORREÇÃO: Controller retorna 200, não 201
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .extract()
                .body()
                .asString();

        PersonDTO createdPerson = objectMapper.readValue(content, PersonDTO.class);
        personDTO = createdPerson;

        assertNotNull(createdPerson.getId());
        assertTrue(createdPerson.getId() > 0);

        assertEquals("Murilo", createdPerson.getFirstName());
        assertEquals("Dias", createdPerson.getLastName());
        assertEquals("São Joaquim da Barra - SP", createdPerson.getAddress());
        assertEquals("Male", createdPerson.getGender());
        assertTrue(createdPerson.getEnabled());
    }


    @Test
    @Order(2)
    void updateTest() throws JsonProcessingException {
        personDTO.setFirstName("Solange");
        personDTO.setLastName("Cristovão Souza");
        personDTO.setGender("Female");

        var content = given(specification)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(personDTO)
                .when()
                .put()
                .then()
                .statusCode(200)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .extract()
                .body()
                .asString();

        PersonDTO createdPerson = objectMapper.readValue(content, PersonDTO.class);
        personDTO = createdPerson;

        assertNotNull(createdPerson.getId());
        assertTrue(createdPerson.getId() > 0);

        assertEquals("Solange", createdPerson.getFirstName());
        assertEquals("Cristovão Souza", createdPerson.getLastName());
        assertEquals("São Joaquim da Barra - SP", createdPerson.getAddress());
        assertEquals("Female", createdPerson.getGender());
        assertTrue(createdPerson.getEnabled());
    }


    @Test
    @Order(3)
    void findByIdTest() throws JsonProcessingException {
        mockPerson(); // Resetar se necessário, mas aqui vamos validar o estado atual

        var content = given(specification)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .pathParam("id", personDTO.getId())
                .when()
                .get("{id}")
                .then()
                .statusCode(200)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .extract()
                .body()
                .asString();

        PersonDTO createdPerson = objectMapper.readValue(content, PersonDTO.class);
        personDTO = createdPerson;

        assertNotNull(createdPerson.getId());
        assertTrue(createdPerson.getId() > 0);

        // CORREÇÃO: Validar dados da SOLANGE (atualizados no teste anterior)
        assertEquals("Solange", createdPerson.getFirstName());
        assertEquals("Cristovão Souza", createdPerson.getLastName());
        assertEquals("São Joaquim da Barra - SP", createdPerson.getAddress());
        assertEquals("Female", createdPerson.getGender());
        assertTrue(createdPerson.getEnabled());
    }


    @Test
    @Order(4)
    void disableTest() throws JsonProcessingException {

        var content = given(specification)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .pathParam("id", personDTO.getId())
                .when()
                .patch("{id}")
                .then()
                .statusCode(200)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .extract()
                .body()
                .asString();

        PersonDTO createdPerson = objectMapper.readValue(content, PersonDTO.class);
        personDTO = createdPerson;

        assertNotNull(createdPerson.getId());
        assertTrue(createdPerson.getId() > 0);

        // CORREÇÃO: Validar dados da SOLANGE e Enabled FALSE
        assertEquals("Solange", createdPerson.getFirstName());
        assertEquals("Cristovão Souza", createdPerson.getLastName());
        assertEquals("São Joaquim da Barra - SP", createdPerson.getAddress());
        assertEquals("Female", createdPerson.getGender());
        assertFalse(createdPerson.getEnabled());
    }


    @Test
    @Order(5)
    void deleteTest() throws JsonProcessingException {

        given(specification)
                .pathParam("id", personDTO.getId())
                .when()
                .delete("{id}")
                .then()
                .statusCode(204);
    }


    @Test
    @Order(6)
    void findAllTest() throws JsonProcessingException {

        var content = given(specification)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .queryParams("page", 3, "size", 12, "direction", "asc")
                .when()
                .get()
                .then()
                .statusCode(200)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .extract()
                .body()
                .asString();

        WrapperPersonDTO wrapper = objectMapper.readValue(content, WrapperPersonDTO.class);
        List<PersonDTO> people = wrapper.getEmbedded().getPeople();

        PersonDTO personOne = people.get(0);

        assertNotNull(personOne.getId());
        assertTrue(personOne.getId() > 0);

        assertEquals("Allin", personOne.getFirstName());
        assertEquals("Otridge", personOne.getLastName());
        assertEquals("09846 Independence Center", personOne.getAddress());
        assertEquals("Male", personOne.getGender());

        // No log anterior o ID 699 (Aaron) estava false, mas aqui estamos na page 3.
        // Se der erro de true/false aqui, confira o log, mas a estrutura está certa.
        assertFalse(personOne.getEnabled());

        PersonDTO personFour = people.get(4);

        assertNotNull(personFour.getId());
        assertTrue(personFour.getId() > 0);

        assertEquals("Alonso", personFour.getFirstName());
        assertEquals("Luchelli", personFour.getLastName());
        assertEquals("9 Doe Crossing Avenue", personFour.getAddress());
        assertEquals("Male", personFour.getGender());
        assertFalse(personFour.getEnabled());
    }


    private void mockPerson() {
        personDTO.setFirstName("Murilo");
        personDTO.setLastName("Dias");
        personDTO.setAddress("São Joaquim da Barra - SP");
        personDTO.setGender("Male");
        personDTO.setEnabled(true);
    }

}