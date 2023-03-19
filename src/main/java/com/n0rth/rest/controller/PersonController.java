package com.n0rth.rest.controller;

import com.n0rth.rest.domain.Person;
import com.n0rth.rest.dto.PersonCreationDTO;
import com.n0rth.rest.dto.PersonDTO;
import com.n0rth.rest.mapper.Mapper;
import com.n0rth.rest.service.PersonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/persons")
public class PersonController {
    private final PersonService service;
    private final Mapper mapper;

    @Autowired
    public PersonController(PersonService service, Mapper mapper) {
        this.service = service;
        this.mapper = mapper;
    }

    @GetMapping
    public List<PersonDTO> getAllPersons() {
        return service.getAll().stream()
                .map(mapper::toDto)
                .collect(Collectors.toList());
    }

    @GetMapping("{id}")
    public PersonDTO getPerson(@PathVariable Long id) {
        Person person = service.getById(id);

        return mapper.toDto(person);
    }

    @PostMapping
    public PersonDTO createPerson(@RequestBody PersonCreationDTO dto) {
        Person person = mapper.toPerson(dto);
        person = service.save(person);

        return mapper.toDto(person);
    }

    @PutMapping("{id}")
    public PersonDTO updatePerson(@RequestBody PersonCreationDTO person, @PathVariable Long id) {
        Person currentPerson = service.getById(id);
        currentPerson.setName(person.getName());
        currentPerson.setSurname(person.getSurname());
        currentPerson.setBirthDate(person.getBirthDate());
        currentPerson = service.save(currentPerson);
        return mapper.toDto(currentPerson);
    }

    @DeleteMapping("{id}")
    public void deletePerson(@PathVariable Long id) {
        service.delete(id);
    }


}
