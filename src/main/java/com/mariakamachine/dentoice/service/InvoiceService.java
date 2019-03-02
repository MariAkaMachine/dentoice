package com.mariakamachine.dentoice.service;

import com.mariakamachine.dentoice.config.properties.InvoiceProperties;
import com.mariakamachine.dentoice.data.entity.CostWrapperEntity;
import com.mariakamachine.dentoice.data.entity.InvoiceEntity;
import com.mariakamachine.dentoice.data.jsonb.EffortJsonb;
import com.mariakamachine.dentoice.data.jsonb.MaterialJsonb;
import com.mariakamachine.dentoice.data.repository.InvoiceRepository;
import com.mariakamachine.dentoice.exception.NotFoundException;
import com.mariakamachine.dentoice.model.FileResource;
import com.mariakamachine.dentoice.rest.dto.Invoice;
import com.mariakamachine.dentoice.util.invoice.pdf.InvoicePdfGenerator;
import com.mariakamachine.dentoice.util.invoice.xml.InvoiceConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

import static com.mariakamachine.dentoice.data.enums.InvoiceType.ESTIMATE;
import static com.mariakamachine.dentoice.data.enums.InvoiceType.INVOICE;
import static com.mariakamachine.dentoice.util.invoice.InvoiceCalculator.calculateInvoice;
import static com.mariakamachine.dentoice.util.invoice.xml.XmlGenerator.generateInvoiceXmlFile;
import static java.lang.String.format;
import static java.util.stream.Collectors.toList;

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
        InvoiceEntity entity = new InvoiceEntity();
        entity.setDentist(dentistService.getById(invoice.getDentist()));
        entity.setPatient(invoice.getPatient());
        entity.setColor(invoice.getColor());
        entity.setDescription(invoice.getDescription());
        // TODO check for xml number duplicate for dentist
        entity.setXmlNumber(invoice.getXmlNumber());
        entity.setInvoiceType(invoice.getInvoiceType());
        entity.setInsuranceType(invoice.getInsuranceType());
        entity.setDate(invoice.getDate());
        entity.setMwst(invoice.getMwst());
        List<EffortJsonb> effortJsonbList = invoice.getEfforts().stream()
                .map(effort -> new EffortJsonb(effort, effortService.getByPosition(effort.getPosition())))
                .collect(toList());
        List<MaterialJsonb> materialJsonbList = invoice.getMaterials().stream()
                .map(material -> new MaterialJsonb(material, materialService.getByPosition(material.getPosition())))
                .collect(toList());
        entity.setCosts(new CostWrapperEntity(effortJsonbList, materialJsonbList));
        entity.setBrutto(calculateInvoice(entity).getBrutto());
        return invoiceRepository.save(entity);
    }

    public InvoiceEntity update(long id, Invoice invoice) {
        InvoiceEntity invoiceEntity = getById(id);
        return invoiceRepository.save(invoiceEntity
                .updateEntity(invoice, dentistService.getById(invoiceEntity.getDentist().getId())));
    }

    public void delete(long id) {
        invoiceRepository.deleteById(id);
    }

    public InvoiceEntity getById(long id) {
        return invoiceRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(format("could not find invoice with [ id: %d ]", id)));
    }

    public List<InvoiceEntity> getAllFromTo(LocalDate from, LocalDate to) {
        return invoiceRepository.findAllByInvoiceTypeEqualsAndDateAfterAndDateBeforeOrderByDateAsc(INVOICE, from.minusDays(1), to.plusDays(1));
    }

    public List<InvoiceEntity> getAllByDentistFromTo(long dentistId, LocalDate from, LocalDate to) {
        return invoiceRepository.findAllByDentistIdAndInvoiceTypeEqualsAndDateAfterAndDateBeforeOrderByDateAsc(dentistId, INVOICE, from.minusDays(1), to.plusDays(1));
    }

    public List<InvoiceEntity> getAllEstimates() {
        return invoiceRepository.findAllByInvoiceTypeEquals(ESTIMATE);
    }

    public List<InvoiceEntity> getAllEstimatesByDentist(long dentistId) {
        return invoiceRepository.findAllByDentistIdAndInvoiceTypeEquals(dentistId, ESTIMATE);
    }

    public List<InvoiceEntity> getAllMonthliesByDentist(long dentistId) {
        return invoiceRepository.findAllByDentistIdAndInvoiceTypeEquals(dentistId, ESTIMATE);
    }


    public FileResource getXmlById(long id) {
        return generateInvoiceXmlFile(new InvoiceConverter().convertToXmlModel(getById(id), invoiceProperties));
    }

    public FileResource getPdfById(long id) {
        return new InvoicePdfGenerator().generatePdf(getById(id));
    }

}
