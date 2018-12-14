package com.mariakamachine.dentoice.rest.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@NoArgsConstructor
@Data
public class Dentist {

    Long id;
    String title;
    String firstName;
    @NotBlank
    String lastName;
    @NotBlank
    String street;
    @NotBlank
    String zip;
    @NotBlank
    String city;
    String phone;
    String fax;
    @Email
    String email;

}
