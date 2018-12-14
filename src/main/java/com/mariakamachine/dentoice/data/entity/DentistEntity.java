package com.mariakamachine.dentoice.data.entity;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.mariakamachine.dentoice.rest.dto.Dentist;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import java.io.Serializable;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_EMPTY;
import static javax.persistence.GenerationType.IDENTITY;

@Entity(name = "dentist")
@Table(name = "dentists")
@EntityListeners(AuditingEntityListener.class)
@NoArgsConstructor
@Data
@JsonInclude(NON_EMPTY)
public class DentistEntity implements Serializable {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(nullable = false, updatable = false, unique = true)
    private Long id;
    private String title;
    @Column(name = "first_name")
    private String firstName;
    @NotBlank
    @Column(name = "last_name")
    private String lastName;
    @NotBlank
    private String street;
    @NotBlank
    private String zip;
    @NotBlank
    private String city;
    private String phone;
    private String fax;
    @Email
    private String email;

    public DentistEntity(Dentist dentist) {
        this.title = dentist.getTitle();
        this.firstName = dentist.getFirstName();
        this.lastName = dentist.getLastName();
        this.street = dentist.getStreet();
        this.zip = dentist.getZip();
        this.city = dentist.getCity();
        this.phone = dentist.getPhone();
        this.fax = dentist.getFax();
        this.email = dentist.getEmail();
    }

    public DentistEntity updateEntity(Dentist dentist) {
        this.title = dentist.getTitle();
        this.firstName = dentist.getFirstName();
        this.lastName = dentist.getLastName();
        this.street = dentist.getStreet();
        this.zip = dentist.getZip();
        this.city = dentist.getCity();
        this.phone = dentist.getPhone();
        this.fax = dentist.getFax();
        this.email = dentist.getEmail();
        return this;
    }

}
