package com.mariakamachine.dentoice.data.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.mariakamachine.dentoice.config.postgres.CostWrapperEntityJsonbUserType;
import com.mariakamachine.dentoice.data.enums.InsuranceType;
import com.mariakamachine.dentoice.data.enums.InvoiceType;
import com.mariakamachine.dentoice.data.jsonb.EffortJsonb;
import com.mariakamachine.dentoice.data.jsonb.MaterialJsonb;
import com.mariakamachine.dentoice.rest.dto.Invoice;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;
import org.springframework.data.jpa.convert.threeten.Jsr310JpaConverters.LocalDateConverter;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_EMPTY;
import static com.mariakamachine.dentoice.util.invoice.InvoiceCalculator.calculateInvoice;
import static java.util.stream.Collectors.toList;
import static javax.persistence.EnumType.STRING;
import static javax.persistence.FetchType.LAZY;

@Entity(name = "invoice")
@Table(name = "invoices")
@TypeDef(name = "CostWrapperEntityJsonbUserType", typeClass = CostWrapperEntityJsonbUserType.class)
@EntityListeners(AuditingEntityListener.class)
@NoArgsConstructor
@Data
@JsonInclude(NON_EMPTY)
public class InvoiceEntity implements Serializable {

    @Id
    @GenericGenerator(name = "invoice_id", strategy = "com.mariakamachine.dentoice.util.invoice.InvoiceIdGenerator")
    @GeneratedValue(generator = "invoice_id")
    @Column(nullable = false, updatable = false, unique = true)
    private Long id;
    @NotNull
    @ManyToOne(targetEntity = DentistEntity.class, fetch = LAZY, optional = false)
    @JoinColumn(name = "dentists_id", nullable = false)
    private DentistEntity dentist;
    @NotBlank
    private String patient;
    @NotBlank
    private String description;
    @NotBlank
    @Column(name = "xml_number")
    private String xmlNumber;
    @NotNull
    @Enumerated(STRING)
    @Column(name = "invoice_type")
    private InvoiceType invoiceType;
    @NotNull
    @Enumerated(STRING)
    @Column(name = "insurance_type")
    private InsuranceType insuranceType;
    @NotNull
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    @Convert(converter = LocalDateConverter.class)
    private LocalDate date;
    @NotNull
    private Integer mwst;
    @Type(type = "CostWrapperEntityJsonbUserType")
    private CostWrapperEntity costs;
    @NotNull
    private BigDecimal brutto;

    public InvoiceEntity updateEntity(Invoice invoice, DentistEntity dentist) {
        this.dentist = dentist;
        this.patient = invoice.getPatient();
        this.description = invoice.getDescription();
        this.xmlNumber = invoice.getXmlNumber();
        this.invoiceType = invoice.getInvoiceType();
        this.insuranceType = invoice.getInsuranceType();
        this.date = invoice.getDate();
        this.mwst = invoice.getMwst();
        List<EffortJsonb> effortJsonbList = invoice.getEfforts().stream()
                .map(EffortJsonb::new)
                .collect(toList());
        List<MaterialJsonb> materialJsonbList = invoice.getMaterials().stream()
                .map(MaterialJsonb::new)
                .collect(toList());
        this.costs = new CostWrapperEntity(effortJsonbList, materialJsonbList);
        this.brutto = calculateInvoice(this).getBrutto();
        return this;
    }

}
