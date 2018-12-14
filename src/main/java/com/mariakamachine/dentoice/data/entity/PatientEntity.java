package com.mariakamachine.dentoice.data.entity;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.mariakamachine.dentoice.rest.dto.Patient;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.io.Serializable;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_EMPTY;
import static javax.persistence.GenerationType.IDENTITY;

@Entity(name = "patient")
@Table(name = "patients")
@EntityListeners(AuditingEntityListener.class)
@NoArgsConstructor
@Data
@JsonInclude(NON_EMPTY)
public class PatientEntity implements Serializable {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(nullable = false, updatable = false, unique = true)
    private Long id;
    @NotBlank
    private String name;
    private String attributes;

    public PatientEntity(Patient patient) {
        this.name = patient.getName();
        this.attributes = patient.getAttributes();
    }

    public PatientEntity updateEntity(Patient patient) {
        this.name = patient.getName();
        this.attributes = patient.getAttributes();
        return this;
    }

}
