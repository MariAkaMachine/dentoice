package com.mariakamachine.dentoice.rest.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;

import static org.springframework.format.annotation.DateTimeFormat.ISO.DATE;

@NoArgsConstructor
@Data
public class Monthly {

    @NotNull
    @Min(0)
    Long dentist;
    @DateTimeFormat(iso = DATE)
    LocalDate date;
    @NotNull
    @Min(0)
    Integer skonto;
    @NotEmpty
    Long[] invoices;

}
