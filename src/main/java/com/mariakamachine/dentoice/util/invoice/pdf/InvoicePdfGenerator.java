package com.mariakamachine.dentoice.util.invoice.pdf;

import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Table;
import com.mariakamachine.dentoice.data.entity.DentistEntity;
import com.mariakamachine.dentoice.data.entity.InvoiceEntity;
import com.mariakamachine.dentoice.data.entity.MonthlyEntity;
import com.mariakamachine.dentoice.model.FileResource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ByteArrayResource;

import java.io.ByteArrayOutputStream;
import java.util.List;

import static com.mariakamachine.dentoice.util.invoice.pdf.PdfCellFormatter.cell;
import static com.mariakamachine.dentoice.util.invoice.pdf.PdfCellFormatter.fineCell;
import static java.lang.String.format;
import static java.lang.System.lineSeparator;

@Slf4j
public class InvoicePdfGenerator {

    public FileResource generateMonthlyPdf(MonthlyEntity monthlyEntity, List<InvoiceEntity> invoices) {
        final String pdfName = monthlyEntity.getDescription();
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        //        Document pdf = new Document(A4, 50, 50, 110, 150);
        Document pdf = new Document(new PdfDocument(new PdfWriter(stream)));


//        try {


//            PdfWriter writer = new PdfWriter(stream);
//            pdf.open();
//            writer.setPageEvent(new PdfPageNumberEvent(pdfName, true));
//            pdf.add(recipientDetails(monthlyEntity.getDentist()));
//            addTablesToPdf(pdf, writer, new MonthlyPdfInvoice().generateTables(monthlyEntity, invoices));
        pdf.close();
//        } catch (DocumentException e) {
//            log.error("unable to generate pdf invoice for monthly invoice", e);
//        }
        return new FileResource(new ByteArrayResource(stream.toByteArray()), format("%s - %s.pdf", invoices.get(0).getDentist().getLastName(), pdfName));
    }

    public FileResource generatePdf(InvoiceEntity invoice) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        Document pdf = new Document(new PdfDocument(new PdfWriter(stream)));
//        Document pdf = new Document(A4, 50, 50, 110, 150);
//        try {
//            PdfWriter writer = getInstance(pdf, stream);
//            pdf.open();
//            writer.setPageEvent(new PdfPageNumberEvent(valueOf(invoice.getId()), false));
        pdf.add(recipientDetails(invoice.getDentist()));
//            addTablesToPdf(pdf, writer, new PdfInvoice().generateTables(invoice));
        pdf.close();
//        } catch (DocumentException e) {
//            log.error("unable to generate pdf invoice for invoice {}", invoice.getId(), e);
//        }
        return new FileResource(new ByteArrayResource(stream.toByteArray()), format("%s.pdf", invoice.getId()));
    }

//    private void addTablesToPdf(Document pdf, PdfWriter writer, List<PdfPTable> tables) throws DocumentException {
//        int counter = 1;
//        for (PdfPTable table : tables) {
//            if (counter == tables.size()) {
//                table.writeSelectedRows(0, -1, pdf.left(), table.getTotalHeight() + 45, writer.getDirectContent());
//            } else {
//                pdf.add(table);
//            }
//            counter++;
//        }
//    }

    private Table recipientDetails(DentistEntity dentist) {
        Table table = new Table(1);
//        table.setWidthPercentage(50);
//        table.setHorizontalAlignment(ALIGN_LEFT);

        table.addCell(fineCell("Udo Baumann - Waldkapellenweg 7 - 72108 Rottenburg", 0));
        String recipient = new String()
                .concat(dentist.getTitle()).concat(lineSeparator())
                .concat(dentist.getFirstName()).concat(" ").concat(dentist.getLastName()).concat(lineSeparator())
                .concat(dentist.getStreet()).concat(lineSeparator())
                .concat(dentist.getZip()).concat(" ").concat(dentist.getCity()).concat(lineSeparator());
        table.addCell(cell(recipient));
        return table;
    }

}
