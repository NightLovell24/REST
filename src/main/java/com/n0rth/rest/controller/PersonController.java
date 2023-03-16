package com.n0rth.rest.controller;

import com.n0rth.rest.domain.Person;
import com.n0rth.rest.service.PersonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/persons")
public class PersonController {
    private final PersonService service;

    @Autowired
    public PersonController(PersonService service) {
        this.service = service;
    }

    @GetMapping
    public List<Person> getAllPersons() {
        return service.getAll();
    }

    @GetMapping("{id}")
    public Person getPerson(@PathVariable Long id) {
        return service.getById(id);
    }

    @PostMapping
    public Person createPerson(@RequestBody Person person) {
        service.save(person);
        return person;
    }

    @PutMapping("{id}")
    public Person updatePerson(@RequestBody Person person, @PathVariable Long id) {
        Person currentPerson = service.getById(id);
        currentPerson.setName(person.getName());
        currentPerson.setSurname(person.getSurname());
        currentPerson.setBirthDate(person.getBirthDate());
        service.save(person);
        return person;
    }

    @DeleteMapping("{id}")
    public void deletePerson(@PathVariable Long id) {
        service.delete(id);
    }


}
