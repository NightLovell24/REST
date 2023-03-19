package com.n0rth.rest.e2e;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.jayway.jsonpath.JsonPath;
import com.n0rth.rest.RestApplication;
import com.n0rth.rest.dao.PersonRepository;
import com.n0rth.rest.domain.Person;
import com.n0rth.rest.dto.PersonCreationDTO;
import com.n0rth.rest.mapper.Mapper;
import com.n0rth.rest.service.PersonService;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.MOCK,
        classes = RestApplication.class)
@AutoConfigureMockMvc
@TestPropertySource(
        locations = "classpath:application-e2e-test.properties")
public class PersonControllerE2ETest {
    @Autowired
    MockMvc mockMvc;
    @Autowired
    PersonService service;
    @Autowired
    Mapper mapper;

    @Autowired
    PersonRepository repository;

    static ObjectMapper objectMapper;

    @BeforeAll
    public static void configure() {
        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
    }

    @BeforeEach
    public void cleanup() {
        repository.deleteAll();
    }

    @Test
    void Should_Successful_GetAll_Persons() throws Exception {
        List<Person> persons = createTestPersons();
        mockMvc.perform(get("/api/v1/persons")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(persons.size())));
    }

    @Test
    void Should_Successful_Get_Person_With_Existing_Id() throws Exception {
        Person person = createTestPerson();
        int expectedAge = service.getAge(person);
        mockMvc.perform(get("/api/v1/persons/" + person.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value(person.getName()))
                .andExpect(jsonPath("$.surname").value(person.getSurname()))
                .andExpect(jsonPath("$.age").value(expectedAge));
    }

    @Test
    void Should_Fail_When_Get_Person_With_Not_Existing_Id() throws Exception {
        mockMvc.perform(get("/api/v1/persons/" + 10)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    void Should_Successful_Create_Person() throws Exception {
        int startPersonsCount = service.getAll().size();
        Person person = getTestPerson();
        PersonCreationDTO creationDTO = mapper.toCreationDTO(person);

        mockMvc.perform(post("/api/v1/persons")
                        .content(objectMapper.writeValueAsString(creationDTO))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value(person.getName()))
                .andExpect(jsonPath("$.surname").value(person.getSurname()))
                .andExpect(jsonPath("$.age").value(service.getAge(person)));

        int currentPersonsCount = service.getAll().size();

        assertThat(startPersonsCount).isNotEqualTo(currentPersonsCount);
    }

    @Test
    void Should_Successful_Update_Person() throws Exception {
        Person person = createTestPerson();

        PersonCreationDTO creationDTO = PersonCreationDTO.builder()
                .name("Freddy").surname("Hilfiger")
                .birthDate(LocalDate.of(2005, 10, 11)).build();

        MvcResult result = mockMvc.perform(put("/api/v1/persons/" + person.getId())
                        .content(objectMapper.writeValueAsString(creationDTO))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value(creationDTO.getName()))
                .andExpect(jsonPath("$.surname").value(creationDTO.getSurname()))
                .andReturn();

        int age = JsonPath.read(result.getResponse().getContentAsString(), "$.age");
        assertThat(age).isNotEqualTo(service.getAge(person));
    }

    @Test
    void Should_Successful_Delete_Person() throws Exception {
        Person person = createTestPerson();
        int startPersonsCount = service.getAll().size();

        mockMvc.perform(delete("/api/v1/persons/" + person.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        int currentPersonsCount = service.getAll().size();

        assertThat(startPersonsCount).isNotEqualTo(currentPersonsCount);
    }


    Person createTestPerson() {
        return service.save(getTestPerson());

    }

    Person getTestPerson() {
        return Person.builder().name("Ivan").surname("Ivanov").
                birthDate(LocalDate.of(2003, 11, 15)).build();
    }

    List<Person> createTestPersons() {
        List<Person> persons = new ArrayList<>();
        Person p1 = createTestPerson();
        Person p2 = service.save(Person.builder().name("Michael").surname("Mayer").
                birthDate(LocalDate.of(1999, 8, 6)).build());

        persons.add(p1);
        persons.add(p2);

        return persons;
    }
}
