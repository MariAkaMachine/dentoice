package com.mariakamachine.dentoice.util.invoice.pdf;

import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Table;
import com.mariakamachine.dentoice.data.entity.InvoiceEntity;
import com.mariakamachine.dentoice.data.entity.MonthlyEntity;
import com.mariakamachine.dentoice.model.FileResource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ByteArrayResource;

import java.io.ByteArrayOutputStream;
import java.util.List;

import static com.itextpdf.kernel.events.PdfDocumentEvent.END_PAGE;
import static com.itextpdf.kernel.geom.PageSize.A4;
import static java.lang.String.format;
import static java.lang.String.valueOf;

@Slf4j
public class PdfGenerator {

    public FileResource generateMonthlyPdfInvoice(MonthlyEntity monthlyEntity, List<InvoiceEntity> invoices) {
        final String pdfName = monthlyEntity.getDescription();
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        PdfDocument pdf = new PdfDocument(new PdfWriter(stream));
        Document doc = new Document(pdf, A4);
        doc.setTopMargin(140);
        doc.setBottomMargin(65);
        pdf.addEventHandler(END_PAGE, new PdfPageEvent(monthlyEntity.getDentist(), pdfName, true));
//        addTablesToPdf(doc, new MonthlyPdfInvoiceGenerator().generateTables(monthlyEntity, invoices));
        new MonthlyPdfInvoiceGenerator().generateTables(monthlyEntity, invoices)
                .forEach(doc::add);
        doc.close();
        return new FileResource(new ByteArrayResource(stream.toByteArray()), format("%s - %s.pdf", invoices.get(0).getDentist().getLastName(), pdfName));
    }

    public FileResource generatePdfInvoice(InvoiceEntity invoice) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        PdfDocument pdf = new PdfDocument(new PdfWriter(stream));
        Document doc = new Document(pdf, A4);
        doc.setTopMargin(140);
        doc.setBottomMargin(65);
        pdf.addEventHandler(END_PAGE, new PdfPageEvent(invoice.getDentist(), valueOf(invoice.getId()), false));
        new PdfInvoiceGenerator().generateTables(invoice)
                .forEach(doc::add);
//        addTablesToPdf(doc, new PdfInvoiceGenerator().generateTables(invoice));
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

}
