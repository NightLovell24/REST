package com.n0rth.rest.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PersonCreationDTO {
    private String name;
    private String surname;
    private LocalDate birthDate;
}
