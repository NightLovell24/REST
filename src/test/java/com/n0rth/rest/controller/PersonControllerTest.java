package com.n0rth.rest.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.n0rth.rest.domain.Person;
import com.n0rth.rest.dto.PersonCreationDTO;
import com.n0rth.rest.dto.PersonDTO;
import com.n0rth.rest.exceptionhandling.exception.PersonNotFoundException;
import com.n0rth.rest.mapper.Mapper;
import com.n0rth.rest.service.PersonService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@WebMvcTest(PersonController.class)
public class PersonControllerTest {

    @MockBean
    PersonService service;
    @MockBean
    Mapper mapper;

    ObjectMapper objectMapper = new ObjectMapper();

    @Autowired
    private MockMvc mockMvc;

    @Test
    void Should_Successful_Create_Person() throws Exception {
        String personName = "Johan";
        Person expectedPerson = Person.builder().name(personName).build();
        PersonCreationDTO creationDTO = PersonCreationDTO.builder().name(personName).build();
        PersonDTO expectedDTO = PersonDTO.builder().name(personName).build();

        when(mapper.toPerson(any(PersonCreationDTO.class))).thenReturn(expectedPerson);
        when(service.save(any(Person.class))).thenReturn(expectedPerson);
        when(mapper.toDto(any(Person.class))).thenReturn(expectedDTO);

        mockMvc.perform(post("/api/v1/persons")
                        .content(objectMapper.writeValueAsString(creationDTO))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value(personName));

    }

    @Test
    void Should_Successful_Update_Person() throws Exception {
        Long id = 1L;
        Person expectedPerson = Person.builder().name("Alfred").surname("Modo").id(id).build();

        String newName = "Mock";
        String newSurname = "Frodo";
        PersonCreationDTO creationDTO = PersonCreationDTO.builder().
                name(newName).surname(newSurname).build();
        PersonDTO dto = PersonDTO.builder().name(newName).surname(newSurname)
                .build();

        when(service.getById(id)).thenReturn(expectedPerson);
        when(service.save(any(Person.class))).
                thenAnswer(invocation -> invocation.getArgument(0));
        when(mapper.toDto(any(Person.class))).thenReturn(dto);

        mockMvc.perform(put("/api/v1/persons/" + id)
                        .content(objectMapper.writeValueAsString(creationDTO))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value(newName))
                .andExpect(jsonPath("$.surname").value(newSurname));
    }

    @Test
    void Should_Fail_Update_Person() throws Exception {
        Long id = 1L;
        PersonCreationDTO creationDTO = PersonCreationDTO.builder().
                build();
        when(service.getById(id)).thenThrow(PersonNotFoundException.class);

        mockMvc.perform(put("/api/v1/persons/" + id)
                        .content(objectMapper.writeValueAsString(creationDTO))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    void Should_Successful_Delete_Person() throws Exception {
        mockMvc.perform(delete("/api/v1/persons/" + 1L)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    void Should_Successful_Get_By_Id_Person() throws Exception {
        Long id = 1L;
        String name = "Jack";
        Person person = Person.builder().name(name).build();
        PersonDTO dto = PersonDTO.builder().name(name).build();

        when(service.getById(id)).thenReturn(person);
        when(mapper.toDto(person)).thenReturn(dto);

        mockMvc.perform(get("/api/v1/persons/" + id)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value(name));
    }
    @Test
    void Should_Fail_Get_By_Id_Person() throws Exception {
        Long id = 1L;

        when(service.getById(id)).thenThrow(PersonNotFoundException.class);

        mockMvc.perform(get("/api/v1/persons/" + id)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }
    @Test
    void Should_Successful_Get_All_Persons() throws Exception {
        when(service.getAll()).thenReturn(List.of(new Person(), new Person(), new Person()));
        when(mapper.toDto(any(Person.class))).thenReturn(new PersonDTO());

        mockMvc.perform(get("/api/v1/persons")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(3)));

    }
}
