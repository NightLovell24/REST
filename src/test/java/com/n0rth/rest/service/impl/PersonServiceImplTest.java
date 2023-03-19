package com.n0rth.rest.service.impl;

import com.n0rth.rest.dao.PersonRepository;
import com.n0rth.rest.domain.Person;
import com.n0rth.rest.exceptionhandling.exception.PersonNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
public class PersonServiceImplTest {

    @InjectMocks
    PersonServiceImpl service;

    @Mock
    PersonRepository repository;

    @Test
    void Should_Save_Person() {
        Person personToSave = Person.builder().id(1L).name("Jack")
                .surname("Jackson").birthDate
                        (LocalDate.of(2002, 11, 10)).build();
        when(repository.save(any(Person.class))).thenReturn(personToSave);

        Person actual = service.save(new Person());

        assertThat(actual).usingRecursiveComparison().isEqualTo(personToSave);
        verify(repository, times(1)).save(any(Person.class));
        verifyNoMoreInteractions(repository);
    }

    @Test
    void Should_Get_Person_By_Id() {
        Long personId = 2L;
        Person expectedPerson = Person.builder().id(personId).name("Pharadei")
                .surname("Bigal").birthDate
                        (LocalDate.of(1958, 6, 21)).build();
        when(repository.findById(personId)).thenReturn(Optional.of(expectedPerson));

        Person actual = service.getById(personId);

        assertThat(actual).usingRecursiveComparison().isEqualTo(expectedPerson);
        verify(repository, times(1)).findById(personId);
        verifyNoMoreInteractions(repository);
    }

    @Test
    void Should_Throw_PersonNotFoundException_When_Person_Doesnt_Exist() {

        when(repository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(PersonNotFoundException.class,
                () -> service.getById(anyLong()));
        verify(repository, times(1)).findById(anyLong());
        verifyNoMoreInteractions(repository);
    }

    @Test
    void Should_Get_All_Persons() {
        when(repository.findAll()).thenReturn(List.of(new Person(), new Person(),
                new Person()));

        assertThat(service.getAll()).hasSize(3);
        verify(repository, times(1)).findAll();
        verifyNoMoreInteractions(repository);
    }

    @Test
    void Should_Delete_One_Person() {
        doNothing().when(repository).deleteById(anyLong());

        service.delete(anyLong());

        verify(repository, times(1)).deleteById(anyLong());
        verifyNoMoreInteractions(repository);
    }

    @Test
    void Should_Return_Correct_Age() {
        Person person = Person.builder().birthDate
                (LocalDate.of(2011, 3, 17)).build();
        int expectedAge = 12;

        int actualAge = service.getAge(person);

        assertThat(actualAge).isEqualTo(expectedAge);
    }

    @Test
    void Should_Return_Incorrect_Age() {
        Person person = Person.builder().birthDate
                (LocalDate.of(2003, 7, 24)).build();

        int incorrectExpectedAge = 14;

        int actualAge = service.getAge(person);

        assertThat(actualAge).isNotEqualTo(incorrectExpectedAge);
    }

}
