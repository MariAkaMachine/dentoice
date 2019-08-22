package com.mariakamachine.dentoice.util.invoice.pdf;

import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.borders.Border;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.property.TextAlignment;
import com.itextpdf.layout.property.UnitValue;
import com.mariakamachine.dentoice.data.entity.DentistEntity;
import com.mariakamachine.dentoice.data.entity.InvoiceEntity;
import com.mariakamachine.dentoice.data.entity.MonthlyEntity;
import com.mariakamachine.dentoice.model.FileResource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ByteArrayResource;

import java.io.ByteArrayOutputStream;
import java.util.List;

import static com.itextpdf.kernel.events.PdfDocumentEvent.END_PAGE;
import static com.itextpdf.kernel.geom.PageSize.A4;
import static com.itextpdf.layout.property.HorizontalAlignment.RIGHT;
import static com.mariakamachine.dentoice.util.invoice.pdf.PdfCellFormatter.cell;
import static com.mariakamachine.dentoice.util.invoice.pdf.PdfCellFormatter.fineCell;
import static java.lang.String.format;
import static java.lang.String.valueOf;
import static java.lang.System.lineSeparator;

@Slf4j
public class PdfGenerator {

    public FileResource generateMonthlyPdfInvoice(MonthlyEntity monthlyEntity, List<InvoiceEntity> invoices) {
        final String pdfName = monthlyEntity.getDescription();
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        //        Document pdf = new Document(A4, 50, 50, 110, 150);
        PdfDocument pdf = new PdfDocument(new PdfWriter(stream));
        Document doc = new Document(pdf, A4);

//        try {


//            PdfWriter writer = new PdfWriter(stream);
//            pdf.open();
//            writer.setPageEvent(new PdfPageEvent(pdfName, true));
//            pdf.add(recipientDetails(monthlyEntity.getDentist()));
//            addTablesToPdf(pdf, writer, new MonthlyPdfInvoiceGenerator().generateTables(monthlyEntity, invoices));
        pdf.close();
//        } catch (DocumentException e) {
//            log.error("unable to generate pdf invoice for monthly invoice", e);
//        }
        return new FileResource(new ByteArrayResource(stream.toByteArray()), format("%s - %s.pdf", invoices.get(0).getDentist().getLastName(), pdfName));
    }

    public FileResource generatePdfInvoice(InvoiceEntity invoice) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        PdfDocument pdf = new PdfDocument(new PdfWriter(stream));
        Document doc = new Document(pdf, A4);
//        Document pdf = new Document(A4, 50, 50, 110, 150);
        pdf.addEventHandler(END_PAGE, new PdfPageEvent(doc, valueOf(invoice.getId()), false));
        doc.add(headerCell());
        doc.add(recipientDetails(invoice.getDentist()));
        addTablesToPdf(doc, new PdfInvoiceGenerator().generateTables(invoice));
        doc.close();
        return new FileResource(new ByteArrayResource(stream.toByteArray()), format("%s.pdf", invoice.getId()));
    }

    private void addTablesToPdf(Document pdf, List<Table> tables) {
        int counter = 1;
        for (Table table : tables) {
//            if (counter == tables.size()) {
//                table.writeSelectedRows(0, -1, pdf.left(), table.getTotalHeight() + 45, writer.getDirectContent());
//            } else {
            pdf.add(table);
//            }
//            counter++;
        }
    }

    private Table recipientDetails(DentistEntity dentist) {
        Table table = new Table(1);
        table.setFixedPosition(1, 50, 650, UnitValue.createPercentValue(50));
        table.addCell(fineCell("Udo Baumann - Waldkapellenweg 7 - 72108 Rottenburg", 0));
        String recipient = new String()
                .concat(dentist.getTitle()).concat(lineSeparator())
                .concat(dentist.getFirstName()).concat(" ").concat(dentist.getLastName()).concat(lineSeparator())
                .concat(dentist.getStreet()).concat(lineSeparator())
                .concat(dentist.getZip()).concat(" ").concat(dentist.getCity()).concat(lineSeparator());
        table.addCell(cell(recipient));
        return table;
    }

    private Table headerCell() {
        Table table = new Table(1);
        table.setHorizontalAlignment(RIGHT);
        table.addCell(cell("Udo Baumann <> Dental-Technik").setBold());
        String address = new String()
                .concat("Waldkapellenweg 7").concat(lineSeparator())
                .concat("72108 Rottenburg").concat(lineSeparator())
                .concat("tel. 07073 / 919 718").concat(lineSeparator())
                .concat("fax 07073 / 919 719").concat(lineSeparator());
        table.addCell(cell(address, TextAlignment.RIGHT, Border.NO_BORDER));
        return table;
    }

}
