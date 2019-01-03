package com.mariakamachine.dentoice.rest.dto;

import com.mariakamachine.dentoice.data.enums.InsuranceType;
import com.mariakamachine.dentoice.data.enums.InvoiceType;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.List;

import static org.springframework.format.annotation.DateTimeFormat.ISO.DATE;

@NoArgsConstructor
@Data
public class Invoice {

    @NotNull
    @Min(0)
    Long dentist;
    @NotBlank
    String patient;
    @NotBlank
    String description;
    @NotBlank
    String xmlNumber;
    @NotNull
    InvoiceType invoiceType;
    @NotNull
    InsuranceType insuranceType;
    @DateTimeFormat(iso = DATE)
    LocalDate date;
    @NotNull
    @Min(0)
    Integer mwst;
    @NotNull
    List<Effort> efforts;
    @NotNull
    List<Material> materials;

}
