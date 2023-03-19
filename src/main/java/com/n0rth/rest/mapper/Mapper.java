package com.n0rth.rest.mapper;

import com.n0rth.rest.domain.Person;
import com.n0rth.rest.dto.PersonCreationDTO;
import com.n0rth.rest.dto.PersonDTO;
import com.n0rth.rest.service.PersonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class Mapper {

    private final PersonService service;

    @Autowired
    public Mapper(PersonService service) {
        this.service = service;
    }

    public PersonDTO toDto(Person person) {
        int age = service.getAge(person);

        return new PersonDTO(person.getName(), person.getSurname(), age);
    }

    public Person toPerson(PersonCreationDTO dto) {

        return new Person(0L, dto.getName(), dto.getSurname(), dto.getBirthDate());
    }

    public PersonCreationDTO toCreationDTO(Person person) {
        return new PersonCreationDTO(person.getName(), person.getSurname(), person.getBirthDate());
    }
}
