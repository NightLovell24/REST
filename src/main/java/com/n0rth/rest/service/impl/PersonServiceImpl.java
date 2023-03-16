package com.n0rth.rest.service.impl;

import com.n0rth.rest.dao.PersonRepository;
import com.n0rth.rest.domain.Person;
import com.n0rth.rest.service.PersonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PersonServiceImpl implements PersonService {

    private final PersonRepository repository;

    @Autowired
    public PersonServiceImpl(PersonRepository repository) {
        this.repository = repository;
    }

    @Override
    public void save(Person person) {
        repository.save(person);
    }

    @Override
    public void delete(Long id) {
        repository.deleteById(id);
    }

    @Override
    public Person getById(Long id) {
        return repository.getReferenceById(id);
    }

    @Override
    public Person getByName(String name) {
        return repository.findByName(name);
    }

    @Override
    public List<Person> getAll() {
        return repository.findAll();
    }
}
