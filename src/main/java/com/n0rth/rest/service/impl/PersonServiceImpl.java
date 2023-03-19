package com.n0rth.rest.service.impl;

import com.n0rth.rest.dao.PersonRepository;
import com.n0rth.rest.domain.Person;
import com.n0rth.rest.exceptionhandling.exception.PersonNotFoundException;
import com.n0rth.rest.service.PersonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.Period;
import java.util.List;
import java.util.Optional;

@Service
public class PersonServiceImpl implements PersonService {

    private final PersonRepository repository;

    @Autowired
    public PersonServiceImpl(PersonRepository repository) {
        this.repository = repository;
    }

    @Override
    public Person save(Person person) {
        return repository.save(person);
    }

    @Override
    public void delete(Long id) {
        repository.deleteById(id);
    }

    @Override
    public Person getById(Long id) {
        Optional<Person> person = repository.findById(id);

        if (person.isEmpty()) throw new PersonNotFoundException("There is no person with " + id + " id");

        return person.get();
    }

    @Override
    public int getAge(Person person) {
        LocalDate currentDate = LocalDate.now();
        Period period = Period.between(currentDate, person.getBirthDate());
        return Math.abs(period.getYears());
    }


    @Override
    public List<Person> getAll() {
        return repository.findAll();
    }
}
