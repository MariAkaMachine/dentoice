package com.mariakamachine.dentoice.data.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.mariakamachine.dentoice.rest.dto.Monthly;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.convert.threeten.Jsr310JpaConverters.LocalDateConverter;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_EMPTY;
import static javax.persistence.FetchType.LAZY;
import static javax.persistence.GenerationType.IDENTITY;

@Entity(name = "monthly")
@Table(name = "monthlies")
@EntityListeners(AuditingEntityListener.class)
@NoArgsConstructor
@Data
@JsonInclude(NON_EMPTY)
public class MonthlyEntity implements Serializable {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(nullable = false, updatable = false, unique = true)
    private Long id;
    @NotNull
    @ManyToOne(targetEntity = DentistEntity.class, fetch = LAZY, optional = false)
    @JoinColumn(name = "dentists_id", nullable = false)
    private DentistEntity dentist;
    @NotNull
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    @Convert(converter = LocalDateConverter.class)
    private LocalDate date;
    @NotEmpty
    private Long[] invoices;
    @NotNull
    private Integer skonto;
    @NotNull
    private BigDecimal total;

    public MonthlyEntity updateEntity(Monthly monthly, DentistEntity dentist, BigDecimal total) {
        this.dentist = dentist;
        this.date = monthly.getDate();
        this.invoices = monthly.getInvoices();
        this.skonto = monthly.getSkonto();
        this.total = total;
        return this;
    }

}

