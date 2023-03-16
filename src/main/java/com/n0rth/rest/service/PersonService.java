package com.n0rth.rest.service;

import com.n0rth.rest.domain.Person;

import java.util.List;

public interface PersonService {

    void save(Person person);

    void delete(Long id);

    Person getById(Long id);

    Person getByName(String name);

    List<Person> getAll();
}
