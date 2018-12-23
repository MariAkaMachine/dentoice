package com.mariakamachine.dentoice.service;

import com.mariakamachine.dentoice.config.properties.InvoiceProperties;
import com.mariakamachine.dentoice.data.entity.CostWrapperEntity;
import com.mariakamachine.dentoice.data.entity.InvoiceEntity;
import com.mariakamachine.dentoice.data.jsonb.EffortJsonb;
import com.mariakamachine.dentoice.data.jsonb.MaterialJsonb;
import com.mariakamachine.dentoice.data.repository.InvoiceRepository;
import com.mariakamachine.dentoice.exception.NotFoundException;
import com.mariakamachine.dentoice.rest.dto.Effort;
import com.mariakamachine.dentoice.rest.dto.Invoice;
import com.mariakamachine.dentoice.rest.dto.Material;
import com.mariakamachine.dentoice.util.invoice.InvoicePdfGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static java.lang.String.format;

@Service
public class InvoiceService {

    private final InvoiceRepository invoiceRepository;
    private final InvoiceProperties invoiceProperties;
    private final DentistService dentistService;
    private final EffortService effortService;
    private final MaterialService materialService;

    @Autowired
    public InvoiceService(InvoiceRepository invoiceRepository, InvoiceProperties invoiceProperties, DentistService dentistService, EffortService effortService, MaterialService materialService) {
        this.invoiceRepository = invoiceRepository;
        this.invoiceProperties = invoiceProperties;
        this.dentistService = dentistService;
        this.effortService = effortService;
        this.materialService = materialService;
    }

    public InvoiceEntity create(Invoice invoice) {
        return invoiceRepository.save(createInvoiceEntity(invoice));
    }

    public InvoiceEntity getById(long id) {
        return invoiceRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(format("could not find invoice with [ id: %d ]", id)));
    }

    public List<InvoiceEntity> getAllByDentistId(long id) {
        return invoiceRepository.findAllByDentistIdOrderByDate(id);
    }

    public byte[] getPdfById(Long id) {
//        return new InvoicePdfGenerator().generatePdf(getById(id));
        return new InvoicePdfGenerator().generatePdf(invoiceProperties, new InvoiceEntity());
    }

    public byte[] getMonthlyPdf(Long dentistId, LocalDate from, LocalDate to) {
//        DentistEntity dentist = dentistService.getById(dentistId);
        List<InvoiceEntity> invoices = invoiceRepository.findAllByDentistIdAndDateAfterAndDateBeforeOrderByDateAsc(dentistId, from, to);
        return new InvoicePdfGenerator().generateMonthlyPdf(invoiceProperties, invoices);
    }

    // update

    // delete

    // get all

    private InvoiceEntity createInvoiceEntity(Invoice invoice) {
        InvoiceEntity entity = new InvoiceEntity();
        // get dentist by id
        entity.setDentist(dentistService.getById(invoice.getDentist()));
        entity.setPatient(invoice.getPatient());
        entity.setDescription(invoice.getDescription());
        // TODO check for xml number duplicate for dentist
        entity.setXmlNumber(invoice.getXmlNumber());
        entity.setInvoiceType(invoice.getInvoiceType());
        entity.setInsuranceType(invoice.getInsuranceType());
        entity.setDate(invoice.getDate());
        // complete efforts
        List<EffortJsonb> effortJsonbList = new ArrayList<>();
        for (Effort effort : invoice.getEfforts()) {
            effortJsonbList.add(new EffortJsonb(effort, effortService.getByPosition(effort.getPosition())));
        }
        // complete materials
        List<MaterialJsonb> materialJsonbList = new ArrayList<>();
        for (Material material : invoice.getMaterials()) {
            materialJsonbList.add(new MaterialJsonb(material, materialService.getByPosition(material.getPosition())));
        }
        entity.setCosts(new CostWrapperEntity(effortJsonbList, materialJsonbList));
        return entity;
    }

}
